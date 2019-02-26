package com.builtbroken.deadmanssatchel.network.packet;

import java.nio.charset.StandardCharsets;

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
		buf.writeBoolean(data.onlyOwner);
		buf.writeInt(data.dropTimer);
		buf.writeInt(data.openTimer);
		buf.writeFloat(data.randomBagDropChance);
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
		
		this.data = new SatchelWorldData(openTimer, dropTimer, randomBagDropChance, null, onlyOwner);
	}

}
