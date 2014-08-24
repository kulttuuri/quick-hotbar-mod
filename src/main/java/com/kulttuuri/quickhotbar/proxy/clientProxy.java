package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import com.kulttuuri.quickhotbar.gui.GuiSettingsInformation;
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

		TickRegistry.registerTickHandler(new QuickHotbarEventHandler(), Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(new QuickHotbarEventHandler());

        GuiSettingsBase.currentGuiScreen = new GuiSettingsInformation();
	}
}