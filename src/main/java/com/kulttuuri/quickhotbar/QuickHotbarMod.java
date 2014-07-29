package com.kulttuuri.quickhotbar;

import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.proxy.IProxy;
import com.kulttuuri.quickhotbar.settings.SettingsClient;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = QuickHotbarModInfo.MODID, version = QuickHotbarModInfo.VERSION, acceptableRemoteVersions="*")
public class QuickHotbarMod
{
    @Instance(QuickHotbarModInfo.MODID)
    public static QuickHotbarMod instance;
    
    public static SettingsClient clientSettings = new SettingsClient();
    
    @SidedProxy(clientSide = "com.kulttuuri.quickhotbar.proxy.clientProxy", serverSide = "com.kulttuuri.quickhotbar.proxy.serverProxy")
    public static IProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	if (event.getSide() == Side.CLIENT)
    	{
    		clientSettings.loadSettingsFromFile(event.getSuggestedConfigurationFile());
            if (!clientSettings.MODE_SWITCHING_DEFAULT_ROW) QuickHotbarEventHandler.currentSwitchMode = QuickHotbarEventHandler.ENUM_CURRENT_SWITCH_MODE_COLUMN;
    	}
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerEvents();
    }
}
