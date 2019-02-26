package com.builtbroken.deadmanssatchel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.builtbroken.deadmanssatchel.config.SatchelConfiguration;
import com.builtbroken.deadmanssatchel.config.SatchelGlobalData;
import com.builtbroken.deadmanssatchel.config.SatchelWorldData;
import com.builtbroken.deadmanssatchel.config.WorldBagConfiguration;
import com.builtbroken.deadmanssatchel.gui.GuiProxy;
import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;
import com.builtbroken.deadmanssatchel.network.SatchelGlobalConfigPacketHandler;
import com.builtbroken.deadmanssatchel.network.SatchelWorldConfigPacketHandler;
import com.builtbroken.deadmanssatchel.network.packet.SatchelGlobalConfigurationPacket;
import com.builtbroken.deadmanssatchel.network.packet.SatchelWorldConfigurationPacket;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = SatchelMod.MODID)
@Mod(modid = SatchelMod.MODID, name = SatchelMod.NAME, version = SatchelMod.VERSION)
public class SatchelMod {

	@Instance(SatchelMod.MODID) 
	public static SatchelMod mod;

	public static final String MODID = "deadmanssatchel";
	public static final String NAME = "Dead Man's Satchel";
	public static final String VERSION = "1.0.0";

	private static Map<Integer, WorldBagConfiguration> configs = new HashMap<Integer, WorldBagConfiguration>();
	private static Map<ItemDeadMansSatchel, SatchelGlobalData> globalData = new HashMap<ItemDeadMansSatchel, SatchelGlobalData>();

	private static File directory = null;
	public static final SimpleNetworkWrapper WRAPPER_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SatchelMod.MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		directory = event.getModConfigurationDirectory();
		String dir = directory.getPath() + "/deadmanssatchel/";
		String fileName = "global.json";
		String path = dir + fileName;
		File global = new File(dir, fileName);
		if(!global.exists()) {
			global.mkdirs();
			try {
				global.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(path));
			JsonReader reader = new JsonReader(new FileReader(path));
			for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
				globalData.put(satchel, SatchelConfiguration.populateGlobal(path, satchel, writer, reader));
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, new GuiProxy());
		WRAPPER_INSTANCE.registerMessage(SatchelGlobalConfigPacketHandler.class, SatchelGlobalConfigurationPacket.class, 0, Side.CLIENT);
		WRAPPER_INSTANCE.registerMessage(SatchelWorldConfigPacketHandler.class, SatchelWorldConfigurationPacket.class, 1, Side.CLIENT);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}


	/*                      
	 * ###################
	 *   CONFIG HANDLING
	 * ###################
	 */


	/* Using this event to load per-world configs on server. Will be sent to clients. */
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		// Reload global data
		String dir = directory.getPath() + "/deadmanssatchel/";
		String fileName = "global.json";
		String path = dir + fileName;
		File global = new File(dir, fileName);
		if(!global.exists()) {
			global.mkdirs();
			try {
				global.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(path));
			JsonReader reader = new JsonReader(new FileReader(path));
			for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
				globalData.put(satchel, SatchelConfiguration.populateGlobal(path, satchel, writer, reader));
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create per-world configs
		for(WorldServer world : DimensionManager.getWorlds()) {
			int dimID = world.provider.getDimension();
			String dirw = directory.getPath() + "/deadmanssatchel/";
			String fileNamew = "DIM" + dimID + ".json";
			String pathw = dirw + fileNamew; 
			if(new File(dirw, fileNamew).exists()) {
				configs.put(dimID, new WorldBagConfiguration(pathw, SatchelMod.getBags())); // Config is loaded via worldbagconfiguration
			} else {
				configs.put(dimID, new WorldBagConfiguration(path, SatchelMod.getBags())); // Load global instead
			}
		}
	}

	/* Using this event to sync the configs from server to client */
	@SubscribeEvent
	public static void syncConfigs(PlayerLoggedInEvent event) {
		for(ItemDeadMansSatchel satchel : globalData.keySet()) {
			WRAPPER_INSTANCE.sendTo(new SatchelGlobalConfigurationPacket(globalData.get(satchel), satchel.getRegistryName().getPath()), (EntityPlayerMP) event.player);
		}
		for(int dimID : configs.keySet()) {
			Map<ItemDeadMansSatchel, SatchelWorldData> dataMap = configs.get(dimID).satchels;
			for(ItemDeadMansSatchel satchel : dataMap.keySet()) {
				WRAPPER_INSTANCE.sendTo(new SatchelWorldConfigurationPacket(dataMap.get(satchel), dimID, satchel.getRegistryName().getPath()), (EntityPlayerMP) event.player);
			}
		}
	}

	/** Get per-world config for a satchel. Works on both sides. **/
	public static SatchelWorldData getConfig(World world, ItemDeadMansSatchel satchel) {
		if(FMLCommonHandler.instance().getSide() == Side.SERVER) {
			int dimID = world.provider.getDimension();
			WorldBagConfiguration config = configs.get(dimID);
			return config.satchels.get(satchel);
		} else {
			return clientWorldConfig.get(world.provider.getDimension()).get(satchel.getRegistryName().getPath());
		}
	}


	/** Holder field for client - uses strings instead of item instances **/
	private static Map<Integer, Map<String, SatchelWorldData>> clientWorldConfig = new HashMap<Integer, Map<String, SatchelWorldData>>();
	/** Holder field for client - uses strings instead of item instances **/
	private static Map<String, SatchelGlobalData> globalClientData = new HashMap<String, SatchelGlobalData>();


	/** Method used to load per-world config data into client **/
	public static void loadClientConfigFromNetwork(SatchelWorldData data, int dimID, String bag) {
		if(!clientWorldConfig.containsKey(dimID)) {
			clientWorldConfig.put(dimID, new HashMap<String, SatchelWorldData>());
		}
		Map<String, SatchelWorldData> bagList = clientWorldConfig.get(dimID);
		bagList.put(bag, data);
	}

	/** Method used to load global config data into client **/
	public static void loadClientConfigFromNetwork(SatchelGlobalData data, String bagName) {
		globalClientData.put(bagName, data);
	}

	/** Retrieves global data for both sides **/
	public static SatchelGlobalData getGlobalData(ItemDeadMansSatchel satchel) {
		if(FMLCommonHandler.instance().getSide() == Side.SERVER) {
			return globalData.get(satchel);
		} else {
			return globalClientData.get(satchel.getRegistryName().getPath());
		}
	}

	/** Helper function to get slot count **/
	public static int getSlotCount(ItemDeadMansSatchel satchel) {
		try {
			return getGlobalData(satchel).slotCount;
		} catch(NullPointerException e) { // During init the client hasn't recieved key config data, resort to default
			return satchel.defaultSlotCount;
		}
	}

	/*                      
	 * ###################
	 *      REGISTRY
	 * ###################
	 */

	public static final ItemDeadMansSatchel satchelBasic = new ItemDeadMansSatchel("deadmanssatchel", 6);

	public static ItemDeadMansSatchel[] getBags() {
		return new ItemDeadMansSatchel[] {satchelBasic};
	}
	
	public static Set<String> getBagRegistryNames() {
		Set<String> set = new HashSet<String>();
		for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
			set.add(satchel.getRegistryName().toString());
		}
		return set;
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(SatchelMod.getBags());
	}


	/*                      
	 * ###################
	 *      EVENTS
	 * ###################
	 */

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void playerDrop(PlayerDropsEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		ArrayList<EntityItem> toRemove = new ArrayList<EntityItem>();
		for(EntityItem drop : event.getDrops()) {
			if(drop.getItem().getItem() instanceof ItemDeadMansSatchel) {
				toRemove.add(drop);
			}
		}
		for(int i = 0; i < toRemove.size(); i++) {
			ItemDeadMansSatchel satchel = (ItemDeadMansSatchel) toRemove.get(i).getItem().getItem();
			SatchelWorldData bagCfg = SatchelMod.getConfig(player.getEntityWorld(), satchel);
			int openTimer = bagCfg.openTimer;
			if(openTimer != 0) {
				Pair<String, String> newPair = Pair.<String, String>of(player.getGameProfile().getId().toString(), satchel.getRegistryName().getPath());
				openTimers.put(newPair, openTimer * 20);
			}

			int reDropTimer = bagCfg.dropTimer;
			Pair<String, String> pair = null;
			for(Pair<String, String> pairI : reDropTimers.keySet()) {
				if(pairI.getLeft().equals(player.getGameProfile().getId().toString()) && pairI.getRight().equals(satchel.getRegistryName().getPath())) {
					pair = pairI;
				}
			}

			boolean doRemove = pair == null;

			if(doRemove) {
				if(Math.random() > bagCfg.randomBagDropChance) {
					if(reDropTimer > 0) {
						Pair<String, String> newPair = Pair.<String, String>of(player.getGameProfile().getId().toString(), satchel.getRegistryName().getPath());
						reDropTimers.put(newPair, reDropTimer * 20);
					}
					ItemStack stack = toRemove.get(i).getItem();
					NBTTagList tagList = new NBTTagList();
					tagList.appendTag(stack.writeToNBT(new NBTTagCompound()));
					player.getEntityData().setTag("satchelData", tagList);
					if(i == 0) {
						player.getEntityData().setBoolean("hasSatchel", true);
					}
					event.getDrops().remove(toRemove.get(i));
				}
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

	public static HashMap<Pair<String, String>, Integer> openTimers = new HashMap<Pair<String, String>, Integer>();
	public static HashMap<Pair<String, String>, Integer> reDropTimers = new HashMap<Pair<String, String>, Integer>(); 

	@SubscribeEvent
	public static void onTick(ServerTickEvent event) {
		HashSet<Pair<String, String>> toRemove = new HashSet<Pair<String, String>>();
		for(Pair<String, String> names : openTimers.keySet()) {
			int time = openTimers.get(names);
			if(time > 0) {
				time--;
				openTimers.put(names, time);
			} else if(time <= 0) {
				toRemove.add(names);
			}
		}

		for(Pair<String, String> remove : toRemove) {
			openTimers.remove(remove);
		}

		HashSet<Pair<String, String>> toRemove2 = new HashSet<Pair<String, String>>();
		for(Pair<String, String> names : reDropTimers.keySet()) {
			int time = reDropTimers.get(names);
			if(time > 0) {
				time--;
				reDropTimers.put(names, time);
			} else if(time <= 0) {
				toRemove2.add(names);
			}
		}

		for(Pair<String, String> remove : toRemove2) {
			reDropTimers.remove(remove);
		}
	}

	public static boolean canOpenBag(EntityPlayer player, ItemDeadMansSatchel satchel) {
		/*if(SatchelMod.getConfig(player.getEntityWorld(), satchel).openTimer == 0) {
			return true;
		}*/ // cannot do this because otherwise if you change worlds you can avoid the timer
		Pair<String, String> pair = null;
		for(Pair<String, String> pairI : openTimers.keySet()) {
			if(pairI.getLeft().equals(player.getGameProfile().getId().toString()) && pairI.getRight().equals(satchel.getRegistryName().getPath())) {
				pair = pairI;
			}
		}

		return pair == null;
	}

}
