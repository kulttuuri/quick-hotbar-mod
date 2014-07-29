package com.kulttuuri.quickhotbar.proxy;

import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.QuickHotbarEventHandler;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

public class clientProxy extends CommonProxy
{
	@Override
	public void registerEvents()
	{
		super.registerEvents();
		
		FMLCommonHandler.instance().bus().register(new QuickHotbarEventHandler());
		MinecraftForge.EVENT_BUS.register(new QuickHotbarEventHandler());
	}
}