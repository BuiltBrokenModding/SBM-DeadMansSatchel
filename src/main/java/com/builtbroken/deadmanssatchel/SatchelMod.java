package com.builtbroken.deadmanssatchel;

import java.io.File;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = SatchelMod.MODID)
@Mod(modid = SatchelMod.MODID, name = SatchelMod.NAME, version = SatchelMod.VERSION)
public class SatchelMod {

	@Instance(SatchelMod.MODID) 
	public static SatchelMod mod;

	public static final String MODID = "deadmanssatchel";
	public static final String NAME = "Dead Man's Satchel";
	public static final String VERSION = "1.0.0";

	private static File directory = null;
	public static final SimpleNetworkWrapper WRAPPER_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SatchelMod.MODID);
	public static File globalFile = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		directory = event.getModConfigurationDirectory();
		globalFile = new File(directory.getPath() + "/deadmanssatchel/global.json");
		SatchelMod.loadGlobalConfig();
		SatchelConfiguration.genDefaultWorld(new File(directory.getPath() + "/deadmanssatchel/", "DIM#.example.json"));
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

	private static Map<Integer, WorldBagConfiguration> configs = new HashMap<Integer, WorldBagConfiguration>();
	private static Map<String, SatchelGlobalData> globalData = new HashMap<String, SatchelGlobalData>();


	/* Using this event to load per-world configs on server. Will be sent to clients. */
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		// Reload global data
		SatchelMod.loadGlobalConfig();

		// Create per-world configs
		for(WorldServer world : DimensionManager.getWorlds()) {
			int dimID = world.provider.getDimension();
			String dir = directory.getPath() + "/deadmanssatchel/";
			File worldFile = new File(dir, "DIM" + dimID + ".json");
			if(worldFile.exists()) {
				configs.put(dimID, SatchelConfiguration.loadWorld(worldFile)); // Config is loaded via worldbagconfiguration
			} else {
				configs.put(dimID, new WorldBagConfiguration(SatchelConfiguration.globalDataMap, false)); // Load global instead
			}
		}
	}

	private static void loadGlobalConfig() {
		String dir = directory.getPath() + "/deadmanssatchel/";
		File dirF = new File(dir);
		if(!dirF.exists()) {
			dirF.mkdirs();
		}

		if(!globalFile.exists()) {
			try {
				globalFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Generate default configs
			SatchelConfiguration.genDefaultGlobal(globalFile);
		}

		// Load global configs
		for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
			SatchelGlobalData data = SatchelConfiguration.loadGlobal(globalFile, satchel);
			globalData.put(satchel.getRegistryName().toString(), data);
		}
	}

	/* 
	 * ###############################
	 *              Getters
	 * ###############################
	 * */

	/** Get per-world config for a satchel. Works on both sides. **/
	public static SatchelWorldData getConfig(World world, ItemDeadMansSatchel satchel) {
		int dimID = world.provider.getDimension();
		WorldBagConfiguration config = configs.get(dimID);
		return config.satchels.get(satchel.getRegistryName().toString());
	}


	/** Retrieves global data for both sides **/
	public static SatchelGlobalData getGlobalData(ItemDeadMansSatchel satchel) {
		return globalData.get(satchel.getRegistryName().toString());
	}

	/** Helper function to get slot count **/
	public static int getSlotCount(ItemDeadMansSatchel satchel) {
		try {
			return getGlobalData(satchel).slot_count;
		} catch(NullPointerException e) { // During init the client hasn't recieved key config data, resort to default
			return satchel.defaultSlotCount;
		}
	}

	/* 
	 * ###############################
	 *           Client Sync
	 * ###############################
	 * */

	/* Using this event to sync the configs from server to client */
	@SubscribeEvent
	public static void syncConfigs(PlayerLoggedInEvent event) {
		for(String name : globalData.keySet()) {
			WRAPPER_INSTANCE.sendTo(new SatchelGlobalConfigurationPacket(globalData.get(name), name), (EntityPlayerMP) event.player);
		}
		for(int dimID : configs.keySet()) {
			WorldBagConfiguration cfg = configs.get(dimID);
			Map<String, SatchelWorldData> dataMap = cfg.satchels;
			for(String name : dataMap.keySet()) {
				WRAPPER_INSTANCE.sendTo(new SatchelWorldConfigurationPacket(dataMap.get(name), dimID, name), (EntityPlayerMP) event.player);
			}
		}
	}


	/** Method used to load per-world config data into client **/
	public static void loadClientConfigFromNetwork(SatchelWorldData data, int dimID, String bag) {
		if(!configs.containsKey(dimID)) {
			configs.put(dimID, new WorldBagConfiguration(new HashMap<String, SatchelWorldData>()));
		}
		Map<String, SatchelWorldData> bagList = configs.get(dimID).satchels;
		bagList.put(bag, data);
	}

	/** Method used to load global config data into client **/
	public static void loadClientConfigFromNetwork(SatchelGlobalData data, String bagName) {
		globalData.put(bagName, data);
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
		for(int i = 0; i < 1; i++) { // limit 1, staying in loop for future changes
			ItemStack stack = toRemove.get(i).getItem();
			ItemDeadMansSatchel satchel = (ItemDeadMansSatchel) stack.getItem();
			SatchelWorldData bagCfg = SatchelMod.getConfig(player.getEntityWorld(), satchel);
			int openTimer = bagCfg.re_open_timer;
			if(openTimer != 0) {
				Pair<String, String> newPair = Pair.<String, String>of(player.getGameProfile().getId().toString(), satchel.getRegistryName().getPath());
				openTimers.put(newPair, openTimer * 20);
			}

			int reDropTimer = bagCfg.death_drop_timer;
			Pair<String, String> pair = null;
			for(Pair<String, String> pairI : reDropTimers.keySet()) {
				if(pairI.getLeft().equals(player.getGameProfile().getId().toString()) && pairI.getRight().equals(satchel.getRegistryName().getPath())) {
					pair = pairI;
				}
			}

			boolean doRemove = pair == null;

			if(doRemove) {
				if(Math.random() > bagCfg.random_bag_drop_chance) {
					ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					for(int j = 0; j < handler.getSlots(); j++) {
						ItemStack stackJ = handler.getStackInSlot(j);
						String key = stackJ.getItem().getRegistryName().toString();
						if(bagCfg.random_bag_items_drop_chance.containsKey(key)) {
							float dropChance = bagCfg.random_bag_items_drop_chance.get(key);
							if(Math.random() < dropChance) {
								ItemStack drop = handler.extractItem(j, stackJ.getCount(), false);
								player.dropItem(drop, true, false);
							}
						}
					}

					// Drop
					if(reDropTimer > 0) {
						Pair<String, String> newPair = Pair.<String, String>of(player.getGameProfile().getId().toString(), satchel.getRegistryName().getPath());
						reDropTimers.put(newPair, reDropTimer * 20);
					}
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
