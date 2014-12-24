package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import com.kulttuuri.quickhotbar.gui.GuiSettingsInformation;

import com.kulttuuri.quickhotbar.QuickHotbarEventHandler;

import net.minecraftforge.fml.common.FMLCommonHandler;

import net.minecraftforge.common.MinecraftForge;

public class clientProxy extends CommonProxy
{
	@Override
	public void registerEvents()
	{
		super.registerEvents();
		
		FMLCommonHandler.instance().bus().register(new QuickHotbarEventHandler());
		MinecraftForge.EVENT_BUS.register(new QuickHotbarEventHandler());

        GuiSettingsBase.currentGuiScreen = new GuiSettingsInformation();
	}
}