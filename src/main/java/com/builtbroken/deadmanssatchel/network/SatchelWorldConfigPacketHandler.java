package com.builtbroken.deadmanssatchel.network;

import com.builtbroken.deadmanssatchel.SatchelMod;
import com.builtbroken.deadmanssatchel.config.SatchelWorldData;
import com.builtbroken.deadmanssatchel.network.packet.SatchelWorldConfigurationPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SatchelWorldConfigPacketHandler implements IMessageHandler<SatchelWorldConfigurationPacket, IMessage> {

	@Override
	public IMessage onMessage(SatchelWorldConfigurationPacket message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			return null; // Client should only be receiving packets
		}
		
		SatchelWorldData data = message.data;
		String bagName = message.satchelName;
		int dimID = message.dimID;
		// Put on main thread
		Minecraft.getMinecraft().addScheduledTask(() -> {
			SatchelMod.loadClientConfigFromNetwork(data, dimID, bagName);
		});
		
		// No response
		return null;
	}

}
