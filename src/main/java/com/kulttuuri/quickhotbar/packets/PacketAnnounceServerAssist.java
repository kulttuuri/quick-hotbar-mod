package com.kulttuuri.quickhotbar.packets;

import com.kulttuuri.quickhotbar.QuickHotbarMod;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class PacketAnnounceServerAssist implements IMessage
{
	public boolean serverHasModInstalled = true;

	public PacketAnnounceServerAssist()
	{
	}

	public PacketAnnounceServerAssist(boolean serverHasModInstalled, String serverModVersion)
	{
		this.serverHasModInstalled = serverHasModInstalled;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.serverHasModInstalled = buf.getBoolean(0);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(serverHasModInstalled);
	}

	public static class PacketAnnounceServerAssistHandler implements IMessageHandler<PacketAnnounceServerAssist, IMessage>
	{
		@Override
		public IMessage onMessage(PacketAnnounceServerAssist message, MessageContext ctx)
		{
			if (ctx.side == ctx.side.CLIENT)
			{
                //System.out.println("Server synced that it has mod installed!");
                try
                {
                    QuickHotbarMod.clientSettings.handleInventorySwitchInServer = true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
			return null;
		}
	}
}
