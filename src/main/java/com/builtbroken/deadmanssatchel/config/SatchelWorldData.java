package com.builtbroken.deadmanssatchel.config;

import java.io.Serializable;
import java.util.HashMap;

public class SatchelWorldData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1434944660839249064L;
	public final int re_open_timer;
	public final int death_drop_timer;
	public final float random_bag_drop_chance;
	public final HashMap<String, Float>  random_bag_items_drop_chance;
	public final boolean only_owner_open;
	
	public SatchelWorldData(int openTimer, int dropTimer, float randomBagDropChance, HashMap<String, Float>  randomBagItemDropChance, boolean onlyOwner) {
		this.re_open_timer = openTimer;
		this.death_drop_timer = dropTimer;
		this.random_bag_drop_chance = randomBagDropChance;
		this.random_bag_items_drop_chance = randomBagItemDropChance;
		this.only_owner_open = onlyOwner;
	}
	
	
}
