package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.QuickHotbarEventHandlerServer;
import com.kulttuuri.quickhotbar.packets.PacketAnnounceServerAssist;
import com.kulttuuri.quickhotbar.packets.PacketChangeCurrentRow;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements IProxy
{
	@Override
	public void registerEvents()
	{
		this.simpleNetworkWrapper.registerMessage(PacketChangeCurrentRow.PacketChangeCurrentRowHandler.class, PacketChangeCurrentRow.class, 0, Side.CLIENT);
		this.simpleNetworkWrapper.registerMessage(PacketChangeCurrentRow.PacketChangeCurrentRowHandler.class, PacketChangeCurrentRow.class, 1, Side.SERVER);

        this.simpleNetworkWrapper.registerMessage(PacketAnnounceServerAssist.PacketAnnounceServerAssistHandler.class, PacketAnnounceServerAssist.class, 2, Side.CLIENT);
        this.simpleNetworkWrapper.registerMessage(PacketAnnounceServerAssist.PacketAnnounceServerAssistHandler.class, PacketAnnounceServerAssist.class, 3, Side.SERVER);

        FMLCommonHandler.instance().bus().register(new QuickHotbarEventHandlerServer());
        MinecraftForge.EVENT_BUS.register(new QuickHotbarEventHandlerServer());
	}
}