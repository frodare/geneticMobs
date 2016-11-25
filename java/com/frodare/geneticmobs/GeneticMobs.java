package com.frodare.geneticmobs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GeneticMobs.MODID, name = GeneticMobs.MODNAME, version = GeneticMobs.VERSION)
public class GeneticMobs {

	public static final String MODID = "geneticmobs";
	public static final String VERSION = "1.10.2-1";
	public static final String MODNAME = "GeneticMobs";

	@SidedProxy(clientSide = "com.frodare.geneticmobs.ClientProxy", serverSide = "com.frodare.geneticmobs.ServerProxy")
	public static CommonProxy proxy;

	@Instance(value = GeneticMobs.MODID)
	public static GeneticMobs INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
