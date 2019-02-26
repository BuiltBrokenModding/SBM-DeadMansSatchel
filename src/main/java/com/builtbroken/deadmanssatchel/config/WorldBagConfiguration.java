package com.builtbroken.deadmanssatchel.config;

import java.util.HashMap;
import java.util.Map;

public class WorldBagConfiguration {
	
	public final Map<String, SatchelWorldData> satchels;
	
	public WorldBagConfiguration(Map<String, SatchelWorldData> map) {
		this.satchels = map;
	}
	
	public WorldBagConfiguration(Map<String, SatchelGlobalData> map, boolean s) {
		HashMap<String, SatchelWorldData> map2 = new HashMap<String, SatchelWorldData>();
		for(String key : map.keySet()) {
			map2.put(key, map.get(key).asWorldData());
		}
		this.satchels = map2;
	}
	
}
