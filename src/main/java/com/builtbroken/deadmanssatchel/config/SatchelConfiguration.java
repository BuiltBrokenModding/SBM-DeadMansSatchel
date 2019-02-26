package com.builtbroken.deadmanssatchel.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.builtbroken.deadmanssatchel.SatchelMod;
import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class SatchelConfiguration {

	public static SatchelWorldData populate(ItemDeadMansSatchel satchel, String path, JsonWriter writer, JsonReader reader) {
		try {
			SatchelWorldData data = null;
			int openTimer = 0;
			int dropTimer = 0;
			float randomBagDropChance = 0.0F;
			boolean onlyOwner = true;
			HashMap<String, Float> dropChance = new HashMap<String, Float>();
			do {
				if(reader.nextName().equals("satchels")) {
					reader.beginArray();
					String satchelName = reader.nextName();
					while(!satchelName.equals(satchel.getRegistryName().toString())) {
						reader.skipValue();
						satchelName = reader.nextName();
					}
					if(satchelName.equals(satchel.getRegistryName().toString())){
						reader.beginObject();
						if(reader.nextName().equals("death_opening_delay")) {
							openTimer = reader.nextInt();
							if(reader.nextName().equals("re_drop_timer")) {
								dropTimer = reader.nextInt();
								if(reader.nextName().equals("random_bag_drop_chance")) {
									randomBagDropChance = (float) reader.nextDouble();
									if(reader.nextName().equals("only_owner_use")) {
										onlyOwner = reader.nextBoolean();
										if(reader.nextName().equals("per_item_drop_chance")) {
											reader.beginArray();
											while(reader.hasNext()) {
												if(reader.nextName().equals("minecraft:example_item")) {
													reader.skipValue();
												} else {
													if(reader.peek() == JsonToken.NUMBER) {
														dropChance.put(reader.nextName(), (float) reader.nextDouble());
													}
												}
											}
											reader.endArray();
											reader.endObject();
											reader.endArray();
											data = new SatchelWorldData(openTimer, dropTimer, randomBagDropChance, dropChance, onlyOwner);
										} else {
											writer.name("per_item_drop_chance");
											writer.beginArray();
											writer.name("minecraft:example_item");
											writer.value(0.3D);
											writer.endArray();
										}
									} else {
										writer.name("only_owner_use");
										writer.value(true);
									}
								} else {
									writer.name("random_bag_drop_chance");
									writer.value(0.0D);
								}
							} else {
								writer.name("death_opening_delay");
								writer.value(0);
							}
						} else {
							writer.name("death_opening_delay");
							writer.value(0);
						}
						reader.endObject();
					} 
					reader.endArray();
				} else {
					writer.name("satchels");
					writer.beginArray();
				}
			} while (data == null);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		/*int openTimer = cfg.getInt("death_opening_delay", cg, 0, 0, Integer.MAX_VALUE, "The amount of time in seconds after death that the bag cannot be opened for. Set to 0 for none.");
		int dropTimer = cfg.getInt("re_drop_timer", cg, 0, 0, Integer.MAX_VALUE, "If the player dies again within this many seconds the bag will drop. Set to 0 to disable dropping.");
		float randomBagDropChance = cfg.getFloat("random_bag_drop_chance", cg, 0.0F, 0.0F, 1.0F, "The chance that the bag will drop on death. Example: 0.5 is 50% chance, or dropping half of the time. 1.0 for all the time.");
		float randomBagItemDropChance = cfg.getFloat("random_item_drop_chance", cg, 0.0F, 0.0F, 1.0F, "The chance that an individual item inside the bag will drop on death. Multiple items may drop if set higher. Same as bag drop chance value wise.");
		boolean new SatchelWorldData(openTimer, dropTimer, randomBagDropChance, randomBagItemDropChance, onlyOwner) = cfg.getBoolean("only_owner_use", cg, true, "Setting to false will allow players to open bags they don't own");

		return new SatchelWorldData(openTimer, dropTimer, randomBagDropChance, randomBagItemDropChance, onlyOwner);*/
	}

	public static Map<String, SatchelGlobalData> globalDataMap = null;
	public static boolean globalLoaded = false;
	private static final Type TYPE = new TypeToken<Map<String, SatchelGlobalData>>() {
	}.getType();

	public static SatchelGlobalData loadGlobal(File global, ItemDeadMansSatchel satchel) {
		if(globalDataMap == null) {
			try {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonReader reader = new JsonReader(new FileReader(global));
				globalDataMap = gson.fromJson(reader, TYPE);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return globalDataMap.get(satchel.getRegistryName().toString());
	}

	public static void genDefaultGlobal(File global) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Map<String, SatchelGlobalData> configMap = new HashMap<String, SatchelGlobalData>();
		for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
			HashMap<String, Float> map = new HashMap<String, Float>();
			map.put("minecraft:example_item", (float) 0.3D);
			map.put("minecraft:example_thing", (float) 0.5D);
			configMap.put(satchel.getRegistryName().toString(), new SatchelGlobalData(new String[] {"minecraft:example_item", "minecraft:example_thing"}, true, 0, 0, satchel.defaultSlotCount, 0, map, true));
		}
		try (Writer writer = new FileWriter(global)) {
			gson.toJson(configMap, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
