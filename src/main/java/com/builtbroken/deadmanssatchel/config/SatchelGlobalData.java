package com.builtbroken.deadmanssatchel.config;

import java.io.Serializable;
import java.util.HashMap;

public class SatchelGlobalData extends SatchelWorldData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7162301192278269430L;
	public final int slotCount;
	public final String[] itemList;
	public final boolean isBlacklist;
	
	public SatchelGlobalData(String[] itemList, boolean isBlacklist, int openTimer, int dropTimer, int slotCount,
			float randomBagDropChance, HashMap<String, Float> randomBagItemDropChance, boolean onlyOwner) {
		super(openTimer, dropTimer, randomBagDropChance, randomBagItemDropChance, onlyOwner);
		this.slotCount = slotCount;
		this.itemList = itemList;
		this.isBlacklist = isBlacklist;
	}
	
	public SatchelGlobalData(SatchelWorldData data, int slotCount, String[] itemList, boolean isBlacklist) {
		this(itemList, isBlacklist, data.openTimer, data.dropTimer, slotCount, data.randomBagDropChance, data.randomBagItemDropChance, data.onlyOwner);
	}
	
	public SatchelWorldData asWorldData() {
		return new SatchelWorldData(this.openTimer, this.dropTimer, this.randomBagDropChance, this.randomBagItemDropChance, this.onlyOwner);
	}

}
