package com.builtbroken.deadmanssatchel.config;

import java.io.Serializable;
import java.util.HashMap;

public class SatchelGlobalData extends SatchelWorldData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7162301192278269430L;
	public final int slot_count;
	public final String[] item_list;
	public final boolean is_blacklist;
	
	public SatchelGlobalData(String[] itemList, boolean isBlacklist, int openTimer, int dropTimer, int slotCount,
			float randomBagDropChance, HashMap<String, Float> randomBagItemDropChance, boolean onlyOwner) {
		super(openTimer, dropTimer, randomBagDropChance, randomBagItemDropChance, onlyOwner);
		this.slot_count = slotCount;
		this.item_list = itemList;
		this.is_blacklist = isBlacklist;
	}
	
	public SatchelGlobalData(SatchelWorldData data, int slotCount, String[] itemList, boolean isBlacklist) {
		this(itemList, isBlacklist, data.re_open_timer, data.death_drop_timer, slotCount, data.random_bag_drop_chance, data.random_bag_items_drop_chance, data.only_owner_open);
	}
	
	public SatchelWorldData asWorldData() {
		return new SatchelWorldData(this.re_open_timer, this.death_drop_timer, this.random_bag_drop_chance, this.random_bag_items_drop_chance, this.only_owner_open);
	}

}
