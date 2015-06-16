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

package com.kulttuuri.quickhotbar.proxy;

import com.kulttuuri.quickhotbar.QuickHotbarEventHandlerServer;
import com.kulttuuri.quickhotbar.packets.PacketAnnounceServerAssist;
import com.kulttuuri.quickhotbar.packets.PacketChangeCurrentRow;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
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