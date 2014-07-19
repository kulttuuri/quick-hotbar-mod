package com.kulttuuri.inventoryscroll.proxy;

import com.kulttuuri.quickhotbar.QuickHotbarMod;

import cpw.mods.fml.common.network.NetworkRegistry;

public interface IProxy
{
	//public final SimpleNetworkWrapper simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(InventoryScrollMod.MODID);
	
	/**
	 * Register all Forge Events here.
	 */
	public void registerEvents();
}