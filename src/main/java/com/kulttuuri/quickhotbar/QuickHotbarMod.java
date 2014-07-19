package com.kulttuuri.quickhotbar;

import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.proxy.IProxy;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = QuickHotbarModInfo.MODID, version = QuickHotbarModInfo.VERSION)
public class QuickHotbarMod
{
    @Instance(QuickHotbarModInfo.MODID)
    public static QuickHotbarMod instance;
    
    @SidedProxy(clientSide = "com.kulttuuri.quickhotbar.proxy.clientProxy", serverSide = "com.kulttuuri.quickhotbar.proxy.serverProxy")
    public static IProxy proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerEvents();
    }
}
