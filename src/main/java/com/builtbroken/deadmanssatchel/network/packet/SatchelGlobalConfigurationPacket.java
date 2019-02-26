package com.builtbroken.deadmanssatchel.network.packet;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.builtbroken.deadmanssatchel.config.SatchelGlobalData;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SatchelGlobalConfigurationPacket implements IMessage {

	public SatchelGlobalData data;
	public String satchelName;

	public SatchelGlobalConfigurationPacket() {

	}

	public SatchelGlobalConfigurationPacket(SatchelGlobalData data, String satchelName) {
		this.data = data;
		this.satchelName = satchelName;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(satchelName.length());
		buf.writeCharSequence(satchelName, StandardCharsets.UTF_8);
		buf.writeBoolean(data.only_owner_open);
		buf.writeBoolean(data.is_blacklist);
		buf.writeInt(data.death_drop_timer);
		buf.writeInt(data.re_open_timer);
		buf.writeFloat(data.random_bag_drop_chance);

		buf.writeInt(data.slot_count);
		// Write itemlist
		buf.writeInt(data.item_list.length);
		for(String item : data.item_list) {
			buf.writeInt(item.length());
			buf.writeCharSequence(item, StandardCharsets.UTF_8);
		}

		// Write drop chances
		buf.writeInt(data.random_bag_items_drop_chance.size());
		for(String registry : data.random_bag_items_drop_chance.keySet()) {
			buf.writeInt(registry.length());
			buf.writeCharSequence(registry, StandardCharsets.UTF_8);
			
			buf.writeFloat(data.random_bag_items_drop_chance.get(registry));
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int nameLength = buf.readInt();
		satchelName = buf.readCharSequence(nameLength, StandardCharsets.UTF_8).toString();
		boolean onlyOwner = buf.readBoolean(); // data.onlyOwner
		boolean isBlackList = buf.readBoolean(); // data.isBlacklist
		int dropTimer = buf.readInt(); // data.dropTimer
		int openTimer = buf.readInt(); // data.openTimer
		float randomBagDropChance = buf.readFloat(); // data.randomBagDropChance


		int slotCount = buf.readInt();
		// Read itemlist
		final int listSize = buf.readInt();
		String[] itemList = new String[listSize];
		for(int i = 0; i < listSize; i++) {
			final int stringLength = buf.readInt();
			itemList[i] = buf.readCharSequence(stringLength, StandardCharsets.UTF_8).toString();
		}
		
		// Read drop chances
		final int listSize2 = buf.readInt();
		HashMap<String, Float> map = new HashMap<String, Float>();
		for(int i = 0; i < listSize2; i++) {
			final int stringLength = buf.readInt();
			String key = buf.readCharSequence(stringLength, StandardCharsets.UTF_8).toString();
			map.put(key, buf.readFloat());
		}

		this.data = new SatchelGlobalData(itemList, isBlackList, openTimer, dropTimer, slotCount, randomBagDropChance, map, onlyOwner);
	}

}
