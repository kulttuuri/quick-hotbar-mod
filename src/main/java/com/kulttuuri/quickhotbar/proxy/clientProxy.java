package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import com.kulttuuri.quickhotbar.gui.GuiSettingsInformation;

import com.kulttuuri.quickhotbar.QuickHotbarEventHandler;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraftforge.common.MinecraftForge;

public class clientProxy extends CommonProxy
{
	@Override
	public void registerEvents()
	{
		super.registerEvents();

		TickRegistry.registerTickHandler(new QuickHotbarEventHandler(), Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(new QuickHotbarEventHandler());

        GuiSettingsBase.currentGuiScreen = new GuiSettingsInformation();
	}
}