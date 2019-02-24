package com.builtbroken.deadmanssatchel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber(modid = SatchelMod.MODID)
@Mod(modid = SatchelMod.MODID, name = SatchelMod.NAME, version = SatchelMod.VERSION)
public class SatchelMod {

	@Instance(SatchelMod.MODID) 
	public static SatchelMod mod;

	public static final String MODID = "deadmanssatchel";
	public static final String NAME = "Dead Man's Satchel";
	public static final String VERSION = "1.0.0";

	private static Logger logger;
	
	private static Map<Integer, WorldBagConfiguration> configs = new HashMap<Integer, WorldBagConfiguration>();
	private static Configuration globalConfig;
	private static Map<ItemDeadMansSatchel, SatchelGlobalData> globalData = new HashMap<ItemDeadMansSatchel, SatchelGlobalData>();
	
	private static File directory = null;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		directory = event.getModConfigurationDirectory();
		globalConfig = new Configuration(new File(directory.getPath() + "/deadmanssatchel", "global.cfg")); 
		SatchelConfiguration.loadConfig(globalConfig);
		for(ItemDeadMansSatchel bag : SatchelMod.getBags()) {
			globalData.put(bag, SatchelConfiguration.populateGlobal(globalConfig, bag));
		}
		SatchelConfiguration.saveConfig(globalConfig);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, new GuiProxy());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(globalConfig.hasChanged()) {
			globalConfig.save();
		}
		for(WorldBagConfiguration config : configs.values()) {
			if(config.config.hasChanged()) {
				config.config.save();
			}
		}
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		// Reload global data
		SatchelConfiguration.loadConfig(globalConfig);
		for(ItemDeadMansSatchel bag : SatchelMod.getBags()) {
			globalData.put(bag, SatchelConfiguration.populateGlobal(globalConfig, bag));
		}
		SatchelConfiguration.saveConfig(globalConfig);
		
		// Create per-world configs
		for(WorldServer world : DimensionManager.getWorlds()) {
			int dimID = world.provider.getDimension();
			Configuration config = new Configuration(new File(directory.getPath() + "/deadmanssatchel", "DIM" + dimID + ".cfg")); 
			configs.put(dimID, new WorldBagConfiguration(config, SatchelMod.getBags())); // Config is loaded via worldbagconfiguration
		}
	}
	
	public static SatchelWorldData getConfig(World world, ItemDeadMansSatchel satchel) {
		int dimID = world.provider.getDimension();
		WorldBagConfiguration config = configs.get(dimID);
		return config.satchels.get(satchel);
	}
	
	public static int getSlotCount(ItemDeadMansSatchel satchel) {
		return globalData.get(satchel).slotCount;
	}
	
	public static final ItemDeadMansSatchel satchelBasic = new ItemDeadMansSatchel("deadmanssatchel");
	
	public static ItemDeadMansSatchel[] getBags() {
		return new ItemDeadMansSatchel[] {satchelBasic};
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(SatchelMod.getBags());
	}

	@SubscribeEvent
	public static void playerDrop(PlayerDropsEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		ArrayList<EntityItem> toRemove = new ArrayList<EntityItem>();
		for(EntityItem drop : event.getDrops()) {
			if(drop.getItem().getItem() == SatchelMod.satchelBasic) {
				toRemove.add(drop);
			}
		}
		for(int i = 0; i < toRemove.size(); i++) {
			event.getDrops().remove(toRemove.get(i));
			ItemStack stack = toRemove.get(i).getItem();
			NBTTagList tagList = new NBTTagList();
			tagList.appendTag(stack.writeToNBT(new NBTTagCompound()));
			player.getEntityData().setTag("satchelData", tagList);
			if(i == 0) {
				player.getEntityData().setBoolean("hasSatchel", true);
			}
		}
	}

	@SubscribeEvent
	public static void playerCloned(PlayerEvent.Clone event) {
		if(event.isWasDeath()) { // oh god the tenses
			EntityPlayer player = event.getOriginal();
			if(player.getEntityData().getBoolean("hasSatchel")) {
				NBTTagList tagList = player.getEntityData().getTagList("satchelData", Constants.NBT.TAG_COMPOUND);
				for(int i = 0; i < tagList.tagCount(); i++) {
					ItemStack stack = new ItemStack(tagList.getCompoundTagAt(i));
					event.getEntityPlayer().addItemStackToInventory(stack);
					tagList.removeTag(i);
				}
			}
		}
	}

}
