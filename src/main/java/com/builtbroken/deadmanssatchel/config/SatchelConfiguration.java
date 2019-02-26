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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class SatchelConfiguration {
	
	private static final Type TYPE_WORLD = new TypeToken<Map<String, SatchelWorldData>>() {
	}.getType();

	public static WorldBagConfiguration loadWorld(File world) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonReader reader = new JsonReader(new FileReader(world));
			return new WorldBagConfiguration(gson.fromJson(reader, TYPE_WORLD));
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JsonSyntaxException e) {
			System.out.println("Misconfigured option in file: " + world.getAbsolutePath());
			throw e;
		}
		return null;
	}
	
	public static void genDefaultWorld(File world) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Map<String, SatchelWorldData> configMap = new HashMap<String, SatchelWorldData>();
		for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
			HashMap<String, Float> map = new HashMap<String, Float>();
			map.put("minecraft:example_item", (float) 0.3D);
			map.put("minecraft:example_thing", (float) 0.5D);
			configMap.put(satchel.getRegistryName().toString(), new SatchelWorldData(0, 0, 0, map, true));
		}
		try (Writer writer = new FileWriter(world)) {
			gson.toJson(configMap, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, SatchelGlobalData> globalDataMap = null;
	public static boolean globalLoaded = false;
	private static final Type TYPE_GLOBAL = new TypeToken<Map<String, SatchelGlobalData>>() {
	}.getType();

	public static SatchelGlobalData loadGlobal(File global, ItemDeadMansSatchel satchel) {
		if(!globalLoaded) {
			try {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonReader reader = new JsonReader(new FileReader(global));
				globalDataMap = gson.fromJson(reader, TYPE_GLOBAL);
				globalLoaded = true;
			} catch(IOException e) {
				e.printStackTrace();
			} catch(JsonSyntaxException e) {
				System.out.println("Misconfigured option in file: " + global.getAbsolutePath());
				throw e;
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
