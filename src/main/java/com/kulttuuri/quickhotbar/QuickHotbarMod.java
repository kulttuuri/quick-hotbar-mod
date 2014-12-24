package com.kulttuuri.quickhotbar;

import com.kulttuuri.quickhotbar.settings.SettingsServer;

import com.kulttuuri.quickhotbar.proxy.IProxy;
import com.kulttuuri.quickhotbar.settings.SettingsClient;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = QuickHotbarModInfo.MODID, version = QuickHotbarModInfo.VERSION, acceptableRemoteVersions="*")
public class QuickHotbarMod
{
    @Instance(QuickHotbarModInfo.MODID)
    public static QuickHotbarMod instance;
    
    public static SettingsClient clientSettings = new SettingsClient();
    public static SettingsServer serverSettings = new SettingsServer();
    
    @SidedProxy(clientSide = "com.kulttuuri.quickhotbar.proxy.clientProxy", serverSide = "com.kulttuuri.quickhotbar.proxy.serverProxy")
    public static IProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	if (event.getSide() == Side.CLIENT)
    	{
    		clientSettings.loadSettingsFromFile(event.getSuggestedConfigurationFile());
    	}
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerEvents();
    }
}
