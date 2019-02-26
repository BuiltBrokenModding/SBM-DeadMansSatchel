package com.builtbroken.deadmanssatchel.config;

import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;

import net.minecraftforge.common.config.Configuration;

public class SatchelConfiguration extends Configuration {


	public static void loadConfig(Configuration cfg){
		cfg.load();
	}

	public static SatchelWorldData populate(ItemDeadMansSatchel satchel, Configuration cfg) {
		String cg = satchel.getRegistryName().getPath();
		int openTimer = cfg.getInt("death_opening_delay", cg, 0, 0, Integer.MAX_VALUE, "The amount of time in seconds after death that the bag cannot be opened for. Set to 0 for none.");
		int dropTimer = cfg.getInt("re_drop_timer", cg, 0, 0, Integer.MAX_VALUE, "If the player dies again within this many seconds the bag will drop. Set to 0 to disable dropping.");
		float randomBagDropChance = cfg.getFloat("random_bag_drop_chance", cg, 0.0F, 0.0F, 1.0F, "The chance that the bag will drop on death. Example: 0.5 is 50% chance, or dropping half of the time. 1.0 for all the time.");
		float randomBagItemDropChance = cfg.getFloat("random_item_drop_chance", cg, 0.0F, 0.0F, 1.0F, "The chance that an individual item inside the bag will drop on death. Multiple items may drop if set higher. Same as bag drop chance value wise.");
		boolean onlyOwner = cfg.getBoolean("only_owner_use", cg, true, "Setting to false will allow players to open bags they don't own");
		
		return new SatchelWorldData(openTimer, dropTimer, randomBagDropChance, randomBagItemDropChance, onlyOwner);
	}
	
	public static void saveConfig(Configuration cfg) {
		if(cfg.hasChanged()){
			cfg.save();
		}
	}

	public static SatchelGlobalData populateGlobal(Configuration cfg, ItemDeadMansSatchel bag) {
		String cg = bag.getRegistryName().getPath();
		int slotCount = cfg.getInt("slot_count", cg, bag.defaultSlotCount, 1, 27, "The amount of slots this bag has. Values larger than 27 would overflow the GUI.");
		String[] itemList = cfg.getStringList("item_list", cg, new String[0], "List of either accepted or blocked items.");
		boolean isBlacklist = cfg.getBoolean("is_blacklist", cg, true, "If set to true the list of items will be treated as a blacklist.");
		SatchelWorldData data = SatchelConfiguration.populate(bag, cfg);
		return new SatchelGlobalData(data, slotCount, itemList, isBlacklist);
	}

}
