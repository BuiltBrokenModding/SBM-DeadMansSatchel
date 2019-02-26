package com.builtbroken.deadmanssatchel.config;

import java.io.Serializable;
import java.util.HashMap;

public class SatchelWorldData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1434944660839249064L;
	public final int openTimer;
	public final int dropTimer;
	public final float randomBagDropChance;
	public final HashMap<String, Float>  randomBagItemDropChance;
	public final boolean onlyOwner;
	
	public SatchelWorldData(int openTimer, int dropTimer, float randomBagDropChance, HashMap<String, Float>  randomBagItemDropChance, boolean onlyOwner) {
		this.openTimer = openTimer;
		this.dropTimer = dropTimer;
		this.randomBagDropChance = randomBagDropChance;
		this.randomBagItemDropChance = randomBagItemDropChance;
		this.onlyOwner = onlyOwner;
	}
	
	
}
