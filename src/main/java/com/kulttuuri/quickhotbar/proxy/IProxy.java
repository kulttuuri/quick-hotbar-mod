package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.QuickHotbarMod;
import com.kulttuuri.quickhotbar.QuickHotbarModInfo;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public interface IProxy
{
	public final SimpleNetworkWrapper simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(QuickHotbarModInfo.MODID);
	
	/**
	 * Register all Forge Events here.
	 */
	public void registerEvents();
}