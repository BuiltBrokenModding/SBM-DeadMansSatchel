package com.builtbroken.deadmanssatchel;

public class SatchelGlobalData extends SatchelWorldData {
	
	public final int slotCount;
	
	public SatchelGlobalData(String[] itemList, boolean isBlacklist, int openTimer, int dropTimer, int slotCount,
			float randomBagDropChance, float randomBagItemDropChance, boolean onlyOwner) {
		super(itemList, isBlacklist, openTimer, dropTimer, randomBagDropChance, randomBagItemDropChance, onlyOwner);
		this.slotCount = slotCount;
	}
	
	public SatchelGlobalData(SatchelWorldData data, int slotCount) {
		this(data.itemList, data.isBlacklist, data.openTimer, data.dropTimer, slotCount, data.randomBagDropChance, data.randomBagItemDropChance, data.onlyOwner);
	}

}
