package com.builtbroken.deadmanssatchel.network.packet;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.builtbroken.deadmanssatchel.config.SatchelWorldData;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SatchelWorldConfigurationPacket implements IMessage {

	public SatchelWorldData data;
	public String satchelName;
	public int dimID;
	
	public SatchelWorldConfigurationPacket() {
		
	}
	

	public SatchelWorldConfigurationPacket(SatchelWorldData data, int dimID, String satchelName) {
		this.data = data;
		this.satchelName = satchelName;
		this.dimID = dimID;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(satchelName.length());
		buf.writeCharSequence(satchelName, StandardCharsets.UTF_8);
		buf.writeInt(dimID);
		buf.writeBoolean(data.only_owner_open);
		buf.writeInt(data.death_drop_timer);
		buf.writeInt(data.re_open_timer);
		buf.writeFloat(data.random_bag_drop_chance);
		
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
		dimID = buf.readInt();
		boolean onlyOwner = buf.readBoolean(); // data.onlyOwner
		int dropTimer = buf.readInt(); // data.dropTimer
		int openTimer = buf.readInt(); // data.openTimer
		float randomBagDropChance = buf.readFloat(); // data.randomBagDropChance
		
		// Read drop chances
		final int listSize = buf.readInt();
		HashMap<String, Float> map = new HashMap<String, Float>();
		for(int i = 0; i < listSize; i++) {
			final int stringLength = buf.readInt();
			String key = buf.readCharSequence(stringLength, StandardCharsets.UTF_8).toString();
			map.put(key, buf.readFloat());
		}
		
		this.data = new SatchelWorldData(openTimer, dropTimer, randomBagDropChance, map, onlyOwner);
	}

}
