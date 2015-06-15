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

import com.kulttuuri.quickhotbar.QuickHotbarMod;
import com.kulttuuri.quickhotbar.QuickHotbarModInfo;
import com.kulttuuri.quickhotbar.gui.components.GuiButtonBetter;
import com.kulttuuri.quickhotbar.gui.components.ToolTipHelper;
import com.kulttuuri.quickhotbar.settings.SettingsClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * GUI for settings screen.
 */
public class GuiSettingsGeneral extends GuiSettingsBase
{
    private static final int BUTTON_ANNOUNCE_MOD = 0;
    private static final int BUTTON_IMMEDIATELY_SHOW_POPUP = 1;
    private static final int BUTTON_ALLOW_NUMBER_COLUMN_SWITCH = 2;
    private static final int BUTTON_ALLOW_SCROLLING_WITH_KEYBOARD = 3;
    private static final int BUTTON_ALLOW_MODE_SWITCH = 4;
    private static final int BUTTON_REVERSE_SCROLLING = 5;

    public GuiSettingsGeneral()
    {
        currentGuiScreen = this;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.clear();

        // Adding settings buttons into screen
        int bSize = 150;
        int row1 = height / 2 - 40;
        int row2 = height / 2 - 10;
        int row3 = height / 2 + 20;
        int column1 = width / 2 - 160;
        int column2 = width / 2 + 5;

        buttonList.add(new GuiButtonBetter(BUTTON_ANNOUNCE_MOD, column1, row1, bSize, 20, "Announce Mod Loaded"));
        buttonList.add(new GuiButtonBetter(BUTTON_IMMEDIATELY_SHOW_POPUP, column2, row1, bSize, 20, "Immediately Show Popup"));

        buttonList.add(new GuiButtonBetter(BUTTON_ALLOW_NUMBER_COLUMN_SWITCH, column1, row2, bSize, 20, "Scroll with Numbers"));
        buttonList.add(new GuiButtonBetter(BUTTON_ALLOW_SCROLLING_WITH_KEYBOARD, column2, row2, bSize, 20, "Scroll with Keyboard"));

        buttonList.add(new GuiButtonBetter(BUTTON_ALLOW_MODE_SWITCH, column1, row3, bSize, 20, "Allow Switching Modes"));
        buttonList.add(new GuiButtonBetter(BUTTON_REVERSE_SCROLLING, column2, row3, bSize, 20, "Reverse Scrolling"));

        buttonList.get(BUTTON_ANNOUNCE_MOD).setHoveringText(width / 2, 5, "Should we announce in chat that mod has<br>been loaded while joining a server.", 0xffffff);
        buttonList.get(BUTTON_IMMEDIATELY_SHOW_POPUP).setHoveringText(width / 2, 5, "If enabled, hover popup menu will be<br>immediately shown when pressing the<br>scrolling key.", 0xffffff);
        buttonList.get(BUTTON_ALLOW_NUMBER_COLUMN_SWITCH).setHoveringText(width / 2, 5, "Should you able to switch<br>columns by holding down<br>number and scrolling.", 0xffffff);
        buttonList.get(BUTTON_ALLOW_SCROLLING_WITH_KEYBOARD).setHoveringText(width / 2, 5, "Should you be able to scroll<br>between inventory rows with<br>keyboard keys.", 0xffffff);
        buttonList.get(BUTTON_ALLOW_MODE_SWITCH).setHoveringText(width / 2, 5, "Should you be able to switch between<br>row and column switching.", 0xffffff);
        buttonList.get(BUTTON_REVERSE_SCROLLING).setHoveringText(width / 2, 5, "Should the mousewheel scrolling<br>direction be reversed.", 0xffffff);

        for (GuiButtonBetter but : buttonList)
        {
            but.buttonTextureFile = new ResourceLocation(QuickHotbarModInfo.MODID, "gui/buttonDisabled.png");
            but.alignToLeft = true;
            but.leftPadding = 20;
        }
    }

    @Override protected void actionPerformed(GuiButton button)
    {
        try
        {
            super.actionPerformed(button);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        SettingsClient clientSettings = QuickHotbarMod.clientSettings;

        if (button.id == BUTTON_ANNOUNCE_MOD)
        {
            clientSettings.ANNOUNCE_MOD_LOADED = !clientSettings.ANNOUNCE_MOD_LOADED;
        }
        else if (button.id == BUTTON_ALLOW_NUMBER_COLUMN_SWITCH)
        {
            clientSettings.ENABLE_NUMBER_SCROLLING = !clientSettings.ENABLE_NUMBER_SCROLLING;
        }
        else if (button.id == BUTTON_REVERSE_SCROLLING)
        {
            clientSettings.REVERSE_MOUSEWHEEL_SCROLLING = !clientSettings.REVERSE_MOUSEWHEEL_SCROLLING;
        }
        else if (button.id == BUTTON_IMMEDIATELY_SHOW_POPUP)
        {
            clientSettings.IMMEDIATELY_SHOW_POPUP_MENU = !clientSettings.IMMEDIATELY_SHOW_POPUP_MENU;
        }
        else if (button.id == BUTTON_ALLOW_SCROLLING_WITH_KEYBOARD)
        {
            clientSettings.ALLOW_SCROLLING_WITH_KEYBOARD = !clientSettings.ALLOW_SCROLLING_WITH_KEYBOARD;
        }
        else if (button.id == BUTTON_ALLOW_MODE_SWITCH)
        {
            clientSettings.ALLOW_MODE_SWITCHING = !clientSettings.ALLOW_MODE_SWITCHING;
        }

        clientSettings.saveSettings();
    }

    @Override public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);

        SettingsClient clientSettings = QuickHotbarMod.clientSettings;

        if (clientSettings.ANNOUNCE_MOD_LOADED) setButtonState(buttonList.get(BUTTON_ANNOUNCE_MOD), true);
        else setButtonState(buttonList.get(BUTTON_ANNOUNCE_MOD), false);

        if (clientSettings.REVERSE_MOUSEWHEEL_SCROLLING) setButtonState(buttonList.get(BUTTON_REVERSE_SCROLLING), true);
        else setButtonState(buttonList.get(BUTTON_REVERSE_SCROLLING), false);

        if (clientSettings.IMMEDIATELY_SHOW_POPUP_MENU) setButtonState(buttonList.get(BUTTON_IMMEDIATELY_SHOW_POPUP), true);
        else setButtonState(buttonList.get(BUTTON_IMMEDIATELY_SHOW_POPUP), false);

        if (clientSettings.ALLOW_SCROLLING_WITH_KEYBOARD) setButtonState(buttonList.get(BUTTON_ALLOW_SCROLLING_WITH_KEYBOARD), true);
        else setButtonState(buttonList.get(BUTTON_ALLOW_SCROLLING_WITH_KEYBOARD), false);

        if (clientSettings.ALLOW_MODE_SWITCHING) setButtonState(buttonList.get(BUTTON_ALLOW_MODE_SWITCH), true);
        else setButtonState(buttonList.get(BUTTON_ALLOW_MODE_SWITCH), false);

        if (clientSettings.ENABLE_NUMBER_SCROLLING) setButtonState(buttonList.get(BUTTON_ALLOW_NUMBER_COLUMN_SWITCH), true);
        else setButtonState(buttonList.get(BUTTON_ALLOW_NUMBER_COLUMN_SWITCH), false);

        ToolTipHelper.renderToolTipAndClear(this);
    }
}