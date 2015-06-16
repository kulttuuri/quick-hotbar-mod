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

package com.kulttuuri.quickhotbar;

import com.kulttuuri.quickhotbar.settings.SettingsServer;

import com.kulttuuri.quickhotbar.proxy.IProxy;
import com.kulttuuri.quickhotbar.settings.SettingsClient;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = QuickHotbarModInfo.MODID, version = QuickHotbarModInfo.VERSION, acceptableRemoteVersions="*")
public class QuickHotbarMod
{
    @Instance(QuickHotbarModInfo.MODID)
    public static QuickHotbarMod instance;
    
    public static SettingsClient clientSettings = new SettingsClient();
    public static SettingsServer serverSettings = new SettingsServer();
    
    @SidedProxy(clientSide = "com.kulttuuri.quickhotbar.proxy.clientProxy", serverSide = "com.kulttuuri.quickhotbar.proxy.serverProxy")
    public static IProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	if (event.getSide() == Side.CLIENT)
    	{
    		clientSettings.loadSettingsFromFile(event.getSuggestedConfigurationFile());
    	}
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerEvents();
    }
}
