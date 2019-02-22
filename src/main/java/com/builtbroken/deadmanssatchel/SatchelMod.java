package com.builtbroken.deadmanssatchel;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
    
}
