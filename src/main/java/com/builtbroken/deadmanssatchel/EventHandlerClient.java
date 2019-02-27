package com.builtbroken.deadmanssatchel;

import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = SatchelMod.MODID, value = Side.CLIENT)
public class EventHandlerClient {
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event) {
		for(ItemDeadMansSatchel satchel : SatchelMod.getBags()) {
			initModel(satchel, 0);;
		}
	}
	
	public static void initModel(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
}