package com.builtbroken.deadmanssatchel.config;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;
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

	public static SatchelGlobalData populateGlobal(String path, ItemDeadMansSatchel satchel, JsonReader reader) {
		try {
			SatchelGlobalData data = null;
			try {
				reader.hasNext();
			} catch(EOFException e) {
				return null;
			}
			int openTimer = 0;
			int dropTimer = 0;
			float randomBagDropChance = 0.0F;
			boolean onlyOwner = true;
			int slotCount = 6;
			boolean isBlacklist = true;
			ArrayList<String> itemList = new ArrayList<String>();
			HashMap<String, Float> dropChance = new HashMap<String, Float>();
			do {
				reader.beginArray();
				while(reader.hasNext() && !reader.nextName().equals(satchel.getRegistryName().toString())) {
					reader.skipValue();
				}
				if(reader.hasNext() && reader.nextName().equals(satchel.getRegistryName().toString())){
					reader.beginObject();
					if(reader.hasNext() && reader.nextName().equals("death_opening_delay")) {
						openTimer = reader.nextInt();
						if(reader.hasNext() && reader.nextName().equals("re_drop_timer")) {
							dropTimer = reader.nextInt();
							if(reader.hasNext() && reader.nextName().equals("random_bag_drop_chance")) {
								randomBagDropChance = (float) reader.nextDouble();
								if(reader.hasNext() && reader.nextName().equals("only_owner_use")) {
									onlyOwner = reader.nextBoolean();
									reader.beginObject();
									if(reader.hasNext() && reader.nextName().equals("per_item_drop_chance")) {
										reader.beginArray();
										while(reader.hasNext()) {
											reader.beginObject();
											if(reader.nextName().equals("minecraft:example_item")) {
												reader.skipValue();
											} else {
												if(reader.peek() == JsonToken.NUMBER) {
													dropChance.put(reader.nextName(), (float) reader.nextDouble());
												}
											}
											reader.endObject();
										}
										reader.endArray();
										reader.endObject();
										if(reader.hasNext() &&reader.nextName().equals("slot_count")) {
											slotCount = reader.nextInt();
											if(reader.hasNext() &&reader.nextName().equals("is_list_blacklist")) {
												isBlacklist = reader.nextBoolean();
												if(reader.hasNext() &&reader.nextName().equals("item_list")) {
													reader.beginArray();
													while(reader.hasNext()) {
														if(reader.nextName().startsWith("minecraft:example_item")) {
															reader.skipValue();
														} else {
															if(reader.peek() == JsonToken.STRING) {
																itemList.add(reader.nextString());
															}
														}
													}
													reader.endArray();




													// End
													reader.endObject();
													reader.endArray();
													data = new SatchelGlobalData(itemList.toArray(new String[itemList.size()]), isBlacklist, openTimer, dropTimer, slotCount, randomBagDropChance, dropChance, onlyOwner);

												}
											}
										}
									}
								}
							}
						}
					}
				}
			} while (data == null);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


		/*String cg = bag.getRegistryName().getPath();
		int slotCount = cfg.getInt("slot_count", cg, bag.defaultSlotCount, 1, 27, "The amount of slots this bag has. Values larger than 27 would overflow the GUI.");
		String[] itemList = cfg.getStringList("item_list", cg, new String[0], "List of either accepted or blocked items.");
		boolean isBlacklist = cfg.getBoolean("is_blacklist", cg, true, "If set to true the list of items will be treated as a blacklist.");
		SatchelWorldData data = SatchelConfiguration.populate(bag, path);
		return new SatchelGlobalData(data, slotCount, itemList, isBlacklist);*/
	}

	public static void writeGlobal(String path, ItemDeadMansSatchel satchel, JsonWriter writer) {

		try {
			writer.beginObject();
			writer.name(satchel.getRegistryName().toString());
			writer.beginObject();
			
			//writer.beginObject();
			writer.name("death_opening_delay");
			writer.value(0);
			//writer.endObject();
			
			//writer.beginObject();
			writer.name("re_drop_timer");
			writer.value(0);
			//writer.endObject();
			
			//writer.beginObject();
			writer.name("random_bag_drop_chance");
			writer.value(0.0D);
			//writer.endObject();
			
			//writer.beginObject();
			writer.name("only_owner_use");
			writer.value(true);
			//writer.endObject();
			
			writer.name("per_item_drop_chance");
			writer.beginArray();
			writer.beginObject();
			writer.name("minecraft:example_item");
			writer.value(0.3D);
			writer.endObject();
			writer.beginObject();
			writer.name("minecraft:example_item2");
			writer.value(0.3D);
			writer.endObject();
			writer.endArray();
			
			writer.name("slot_count");
			writer.value(satchel.defaultSlotCount);
			
			writer.name("is_list_blacklist");
			writer.value(true);
			
			writer.name("item_list");
			writer.beginArray();
			writer.value("minecraft:example_item");
			writer.value("minecraft:example_item2");
			writer.endArray();
			
			writer.endObject();
			writer.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
			/*if(writer.hasNext() && writer.nextName().equals("death_opening_delay")) {
				openTimer = writer.nextInt();
				if(writer.hasNext() && writer.nextName().equals("re_drop_timer")) {
					dropTimer = writer.nextInt();
					if(writer.hasNext() && writer.nextName().equals("random_bag_drop_chance")) {
						randomBagDropChance = (float) writer.nextDouble();
						if(writer.hasNext() && writer.nextName().equals("only_owner_use")) {
							onlyOwner = writer.nextBoolean();
							writer.beginObject();
							if(writer.hasNext() && writer.nextName().equals("per_item_drop_chance")) {
								writer.beginArray();
								while(writer.hasNext()) {
									writer.beginObject();
									if(writer.nextName().equals("minecraft:example_item")) {
										writer.skipValue();
									} else {
										if(writer.peek() == JsonToken.NUMBER) {
											dropChance.put(writer.nextName(), (float) writer.nextDouble());
										}
									}
									writer.endObject();
								}
								writer.endArray();
								writer.endObject();
								if(writer.hasNext() &&writer.nextName().equals("slot_count")) {
									slotCount = writer.nextInt();
									if(writer.hasNext() &&writer.nextName().equals("is_list_blacklist")) {
										isBlacklist = writer.nextBoolean();
										if(writer.hasNext() &&writer.nextName().equals("item_list")) {
											writer.beginArray();
											while(writer.hasNext()) {
												if(writer.nextName().startsWith("minecraft:example_item")) {
													writer.skipValue();
												} else {
													if(writer.peek() == JsonToken.STRING) {
														itemList.add(writer.nextString());
													}
												}
											}
											writer.endArray();




											// End
											writer.endObject();
											writer.endArray();
											data = new SatchelGlobalData(itemList.toArray(new String[itemList.size()]), isBlacklist, openTimer, dropTimer, slotCount, randomBagDropChance, dropChance, onlyOwner);

										}
									}
								}
							}
						}
					}
				}
			}
		}*/
	}

}
