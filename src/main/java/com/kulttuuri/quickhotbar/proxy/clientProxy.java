package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import com.kulttuuri.quickhotbar.gui.GuiSettingsInformation;
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

        GuiSettingsBase.currentGuiScreen = new GuiSettingsInformation();
	}
}