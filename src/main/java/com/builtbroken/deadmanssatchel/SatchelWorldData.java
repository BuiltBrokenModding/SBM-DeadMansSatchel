package com.builtbroken.deadmanssatchel;

public class SatchelWorldData {
	
	
	public final String[] itemList;
	public final boolean isBlacklist;
	public final int openTimer;
	public final int dropTimer;
	public final float randomBagDropChance;
	public final float randomBagItemDropChance;
	public final boolean onlyOwner;
	
	public SatchelWorldData(String[] itemList, boolean isBlacklist, int openTimer, int dropTimer, float randomBagDropChance, float randomBagItemDropChance, boolean onlyOwner) {
		this.itemList = itemList;
		this.isBlacklist = isBlacklist;
		this.openTimer = openTimer;
		this.dropTimer = dropTimer;
		this.randomBagDropChance = randomBagDropChance;
		this.randomBagItemDropChance = randomBagItemDropChance;
		this.onlyOwner = onlyOwner;
	}
	
	
}
