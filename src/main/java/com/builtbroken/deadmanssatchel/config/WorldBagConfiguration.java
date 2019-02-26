package com.builtbroken.deadmanssatchel.config;

import java.util.ArrayList;
import java.util.HashMap;

import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;
import com.google.common.collect.Lists;

import akka.util.Collections;
import net.minecraftforge.common.config.Configuration;

public class WorldBagConfiguration {
	
	public final Configuration config;
	public HashMap<ItemDeadMansSatchel, SatchelWorldData> satchels;
	
	public WorldBagConfiguration(Configuration config, ItemDeadMansSatchel... satchels) {
		this.config = config;
		this.satchels = new HashMap<ItemDeadMansSatchel, SatchelWorldData>();
		SatchelConfiguration.loadConfig(config);
		for(ItemDeadMansSatchel satchel : satchels) {
			this.satchels.put(satchel, SatchelConfiguration.populate(satchel, config));
		}
		SatchelConfiguration.saveConfig(config);
	}
	
}
