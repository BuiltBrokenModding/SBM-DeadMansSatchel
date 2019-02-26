package com.builtbroken.deadmanssatchel.network.packet;

import java.nio.charset.StandardCharsets;

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
		buf.writeBoolean(data.onlyOwner);
		buf.writeBoolean(data.isBlacklist);
		buf.writeInt(data.dropTimer);
		buf.writeInt(data.openTimer);
		buf.writeFloat(data.randomBagDropChance);
		buf.writeFloat(data.randomBagItemDropChance);

		buf.writeInt(data.slotCount);
		// Write itemlist
		buf.writeInt(data.itemList.length);
		for(String item : data.itemList) {
			buf.writeInt(item.length());
			buf.writeCharSequence(item, StandardCharsets.UTF_8);
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
		float randomBagItemDropChance = buf.readFloat(); // data.randomBagItemDropChance


		int slotCount = buf.readInt();
		// Read itemlist
		final int listSize = buf.readInt();
		String[] itemList = new String[listSize];
		for(int i = 0; i < listSize; i++) {
			final int stringLength = buf.readInt();
			itemList[i] = buf.readCharSequence(stringLength, StandardCharsets.UTF_8).toString();
		}
		
		this.data = new SatchelGlobalData(itemList, isBlackList, openTimer, dropTimer, slotCount, randomBagDropChance, randomBagItemDropChance, onlyOwner);
	}

}
