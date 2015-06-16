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

package com.kulttuuri.quickhotbar.gui;

import com.kulttuuri.quickhotbar.QuickHotbarModInfo;
import com.kulttuuri.quickhotbar.gui.components.GuiButtonBetter;
import com.kulttuuri.quickhotbar.settings.SettingsClient;
import net.minecraft.client.gui.GuiButton;

/**
 * GUI for information screen.
 */
public class GuiSettingsInformation extends GuiSettingsBase
{
    private static final int BUTTON_MOD_NETWORK_POST_THREAD = 0;

    public GuiSettingsInformation()
    {
        currentGuiScreen = this;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.clear();
        buttonList.add(new GuiButtonBetter(BUTTON_MOD_NETWORK_POST_THREAD, width / 2, height / 2, 100, 20, "Mod Information"));
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);

        if (guibutton.id == BUTTON_MOD_NETWORK_POST_THREAD)
        {
            String url = "http://adf.ly/7344368/minecraft-forum-post";
            // Try to open browser with the URL
            try
            {
                java.net.URI uri = new java.net.URI(url);
                java.awt.Desktop.getDesktop().browse(uri);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);

        fontRenderer.drawString("Quick Hotbar Mod version " + QuickHotbarModInfo.VERSION, width / 2 - 160, height / 2 - 40, -1);

        if (SettingsClient.handleInventorySwitchInServer)
        {
            fontRenderer.drawString("Server has mod installed.", width / 2 - 160, height / 2 - 25, -1);
        }
        else
        {
            fontRenderer.drawString("Server does not have mod installed.", width / 2 - 160, height / 2 - 25, -1);
        }
    }
}