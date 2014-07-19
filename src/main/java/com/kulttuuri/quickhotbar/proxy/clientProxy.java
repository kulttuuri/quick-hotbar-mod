package com.kulttuuri.quickhotbar.proxy;

import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.NetworkConnectionHandler;
import com.kulttuuri.quickhotbar.QuickHotbarEventHandler;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

public class clientProxy extends CommonProxy
{
	@Override
	public void registerEvents()
	{
		super.registerEvents();
		
		//FMLCommonHandler.instance()register(new InventoryScrollEventHandler());
		//FMLCommonHandler.instance().re
		TickRegistry.registerTickHandler(new QuickHotbarEventHandler(), Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(new QuickHotbarEventHandler());
		
		NetworkRegistry.instance().registerConnectionHandler(new NetworkConnectionHandler());
	}
}