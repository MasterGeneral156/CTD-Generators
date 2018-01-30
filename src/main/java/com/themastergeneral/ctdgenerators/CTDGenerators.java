package com.themastergeneral.ctdgenerators;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

import com.themastergeneral.ctdgenerators.proxy.Common;

@Mod(modid = CTDGenerators.MODID, name = CTDGenerators.NAME, version = CTDGenerators.VERSION)
public class CTDGenerators
{
    public static final String MODID = "ctdgenerators";
    public static final String NAME = "CTD Generators";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "required-after:redstoneflux;required-after:ctdcore@[1.2,];";
	public static final String updateJSON = "https://raw.githubusercontent.com/MasterGeneral156/Version/master/CTD-Tweaks.json";
	public static final String MCVersion = "1.12.2";
	public static final String FingerPrint = "1cd8befc36d6dedc5601d77a013f43afc71f899f";

	@Instance
	public static CTDGenerators instance = new CTDGenerators();
    public static Logger logger;
    
    @SidedProxy(clientSide = "com.themastergeneral.ctdgenerators.proxy.Client", serverSide = "com.themastergeneral.ctdgenerators.proxy.Server")
    public static Common proxy;

    @EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		logger = e.getModLog();
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
		logger.info("CTD Generators have loaded successfully.");
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent e) {
		FMLLog.warning("Invalid fingerprint detected for CTD Generators! TheMasterGeneral will not support this version!");
	}
}
