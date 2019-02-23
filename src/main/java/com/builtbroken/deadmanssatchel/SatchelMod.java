package com.builtbroken.deadmanssatchel;

import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber(modid = SatchelMod.MODID)
@Mod(modid = SatchelMod.MODID, name = SatchelMod.NAME, version = SatchelMod.VERSION)
public class SatchelMod {

	@Instance(SatchelMod.MODID) 
	public static SatchelMod mod;

	public static final String MODID = "deadmanssatchel";
	public static final String NAME = "Dead Man's Satchel";
	public static final String VERSION = "1.0.0";

	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, new GuiProxy());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	public static final ItemDeadMansSatchel satchel = new ItemDeadMansSatchel();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(satchel);
	}

	@SubscribeEvent
	public static void playerDrop(PlayerDropsEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		ArrayList<EntityItem> toRemove = new ArrayList<EntityItem>();
		for(EntityItem drop : event.getDrops()) {
			if(drop.getItem().getItem() == SatchelMod.satchel) {
				toRemove.add(drop);
			}
		}
		for(int i = 0; i < toRemove.size(); i++) {
			System.out.println("test100");
			event.getDrops().remove(toRemove.get(i));
			ItemStack stack = toRemove.get(i).getItem();
			stack.writeToNBT(player.getEntityData().getCompoundTag("satchelstack" + i));
			if(i == 0) {
				player.getEntityData().setBoolean("hasSatchel", true);
			}
		}
	}
	
	@SubscribeEvent
	public static void playerCloned(PlayerEvent.Clone event) {
		if(event.isWasDeath()) { // oh god the tenses
			EntityPlayer player = event.getEntityPlayer();
			if(player.getEntityData().getBoolean("hasSatchel")) {
				for(String key : player.getEntityData().getKeySet()) {
					if(key.startsWith("satchelstack")) {
						ItemStack stack = new ItemStack(SatchelMod.satchel);
						stack.deserializeNBT(player.getEntityData().getCompoundTag(key));
					}
				}
			}
		}
	}

}
