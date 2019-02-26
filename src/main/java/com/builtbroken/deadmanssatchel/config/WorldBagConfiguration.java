package com.builtbroken.deadmanssatchel.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.builtbroken.deadmanssatchel.SatchelMod;
import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class WorldBagConfiguration {
	
	public final String config;
	public HashMap<ItemDeadMansSatchel, SatchelWorldData> satchels;
	
	public WorldBagConfiguration(String config, ItemDeadMansSatchel... satchels) {
		this.config = config;
		this.satchels = new HashMap<ItemDeadMansSatchel, SatchelWorldData>();
		try {
			JsonWriter writer = new JsonWriter(new FileWriter(config));
			JsonReader reader = new JsonReader(new FileReader(config));
			for(ItemDeadMansSatchel satchel : satchels) {
				SatchelWorldData data = SatchelConfiguration.populate(satchel, config, writer, reader);
				if(data == null) {
					data = SatchelMod.getGlobalData(satchel).asWorldData();
				}
				this.satchels.put(satchel, data);
			}
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
