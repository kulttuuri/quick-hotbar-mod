package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.NetworkConnectionHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IProxy
{
	@Override
	public void registerEvents()
	{
		//this.simpleNetworkWrapper.registerMessage(PacketChangeCurrentRow.PacketChangeCurrentRowHandler.class, PacketChangeCurrentRow.class, 0, Side.CLIENT);
		//this.simpleNetworkWrapper.registerMessage(PacketChangeCurrentRow.PacketChangeCurrentRowHandler.class, PacketChangeCurrentRow.class, 1, Side.SERVER);
        NetworkRegistry.instance().registerConnectionHandler(new NetworkConnectionHandler());
	}
}