package com.builtbroken.deadmanssatchel.network;

import com.builtbroken.deadmanssatchel.SatchelMod;
import com.builtbroken.deadmanssatchel.config.SatchelGlobalData;
import com.builtbroken.deadmanssatchel.network.packet.SatchelGlobalConfigurationPacket;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SatchelGlobalConfigPacketHandler implements IMessageHandler<SatchelGlobalConfigurationPacket, IMessage> {

	@Override
	public IMessage onMessage(SatchelGlobalConfigurationPacket message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			return null; // Client should only be receiving packets
		}
		
		SatchelGlobalData data = message.data;
		String bagName = message.satchelName;
		// Put on main thread
		Minecraft.getMinecraft().addScheduledTask(() -> {
			SatchelMod.loadClientConfigFromNetwork(data, bagName);
		});
		
		// No response
		return null;
	}

}
