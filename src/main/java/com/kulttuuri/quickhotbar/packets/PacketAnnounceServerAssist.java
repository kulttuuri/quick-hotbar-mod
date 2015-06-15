/**
 * Copyright (C) 2015 Aleksi Postari (@Kulttuuri, aleksi@postari.net)
 * License type: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * This code is part of project Quick Hotbar Mod.
 * License in short: You can use this code as you wish, but please keep this license information intach or credit the original author in redistributions.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kulttuuri.quickhotbar.packets;

import com.kulttuuri.quickhotbar.QuickHotbarMod;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Packet for announcing from server to client that server has the mod installed for improved assistance with inventory management.
 */
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
