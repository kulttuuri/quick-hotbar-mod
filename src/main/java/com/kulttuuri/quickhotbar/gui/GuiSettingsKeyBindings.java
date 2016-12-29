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
import com.kulttuuri.quickhotbar.gui.components.ToolTipHelper;
import com.kulttuuri.quickhotbar.settings.SettingsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import scala.Console;

import java.util.ArrayList;

/**
 * GUI for information screen.
 */
public class GuiSettingsKeyBindings extends GuiSettingsBase
{
    private ArrayList<GuiTextField> textfieldBindings = new ArrayList<GuiTextField>();
    private static boolean isEditingKeybinding = false;

    private String errorText = "";
    private int errorX = 0;
    private int errorY = 0;
    private long errorSetTime = 0;

    private static final int TEXTFIELD_MODIFIER = 0;
    private static final int TEXTFIELD_SWITCH_MODE = 1;
    private static final int TEXTFIELD_OPEN_MENU = 2;
    private static final int TEXTFIELD_SCROLL_UP = 3;
    private static final int TEXTFIELD_SCROLL_DOWN = 4;

    public GuiSettingsKeyBindings()
    {
        currentGuiScreen = this;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        Keyboard.enableRepeatEvents(true);

        textfieldBindings.clear();

        int fieldWidth = 100;
        int fieldHeight = 15;
        textfieldBindings.add(TEXTFIELD_MODIFIER, new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 159, height / 2 - 34, fieldWidth, fieldHeight));
        textfieldBindings.add(TEXTFIELD_SWITCH_MODE, new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 159, height / 2 + 15, fieldWidth, fieldHeight));
        textfieldBindings.add(TEXTFIELD_OPEN_MENU, new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 159, height / 2 + 45, fieldWidth, fieldHeight));

        textfieldBindings.add(TEXTFIELD_SCROLL_UP, new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 30, height / 2 + 15, fieldWidth, fieldHeight));
        textfieldBindings.add(TEXTFIELD_SCROLL_DOWN, new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, width / 2 - 30, height / 2 + 45, fieldWidth, fieldHeight));

        updateValues();
    }

    private void updateValues()
    {
        SettingsClient set = QuickHotbarMod.clientSettings;

        textfieldBindings.get(TEXTFIELD_MODIFIER).setText(set.SCROLLING_KEY.getEventNameShort());
        textfieldBindings.get(TEXTFIELD_SWITCH_MODE).setText(set.SCROLLING_KEY_SWITCH_MODE.getEventNameShort());
        textfieldBindings.get(TEXTFIELD_OPEN_MENU).setText(set.KEY_OPEN_MOD_SETTINGS_MENU.getEventNameShort());
        textfieldBindings.get(TEXTFIELD_SCROLL_UP).setText(set.SCROLLING_KEY_UP.getEventNameShort());
        textfieldBindings.get(TEXTFIELD_SCROLL_DOWN).setText(set.SCROLLING_KEY_DOWN.getEventNameShort());
    }

    @Override
    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);

        updateValues();
        
        int m = 0;
        for (GuiTextField textfield : textfieldBindings)
        {
            textfield.mouseClicked(i, j, k);
            
            if (textfield.isFocused() && Mouse.getEventButton() != -1 && Mouse.getEventButton() != 0 && Mouse.getEventButton() !=  1)
            {
            	Console.out().println("Mouse pressed when binding key: " + i);
                if (!setKeyBinding(m, Mouse.getButtonName(k)))
                {
                    setErrorTooltip(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.reservederror"), textfield.xPosition - (textfield.width / 6), textfield.yPosition);
                }
                textfield.setFocused(false);
                updateValues();
            }
            
            if (textfield.isFocused())
            {
                textfield.setText("");
                isEditingKeybinding = true;
            }
            m += 1;
        }
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        boolean textfieldHasFocus = false;

        for (int m = 0; m < textfieldBindings.size(); m++)
        {
            GuiTextField textfield = textfieldBindings.get(m);
            if (textfield.isFocused()) textfieldHasFocus = true;
            textfield.textboxKeyTyped(c, i);

            if (textfield.isFocused() && i == Keyboard.KEY_ESCAPE)
            {
                textfield.setFocused(false);
                updateValues();
            }
            else if (textfield.isFocused() && i != Keyboard.KEY_NONE)
            {
                if (!setKeyBinding(m, Keyboard.getKeyName(i)))
                {
                    setErrorTooltip(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.reservederror"), textfield.xPosition - (textfield.width / 6), textfield.yPosition);
                }
                textfield.setFocused(false);
                updateValues();
            }
        }

        if (i == Keyboard.KEY_ESCAPE && !textfieldHasFocus)
        {
            closeGui();
        }
    }

    /**
     * 
     * @param textfieldNumber textfield id which user is editing.
     * @param eventName Key / mouse press event name.
     * @return true if keybinding was set to settings.
     */
    private boolean setKeyBinding(int textfieldNumber, String eventName)
    {
        SettingsClient set = QuickHotbarMod.clientSettings;
        
        boolean isKeyboardKey = eventName.startsWith("BUTTON");

        if (isKeybindingReserved(eventName)) return false;

        if (textfieldNumber == TEXTFIELD_MODIFIER)
        {
            set.SCROLLING_KEY.setKeyEventName(eventName);
        }
        else if (textfieldNumber == TEXTFIELD_SWITCH_MODE)
        {
            set.SCROLLING_KEY_SWITCH_MODE.setKeyEventName(eventName);
        }
        else if (textfieldNumber == TEXTFIELD_OPEN_MENU)
        {
            set.KEY_OPEN_MOD_SETTINGS_MENU.setKeyEventName(eventName);
        }
        else if (textfieldNumber == TEXTFIELD_SCROLL_UP)
        {
            set.SCROLLING_KEY_UP.setKeyEventName(eventName);
        }
        else if (textfieldNumber == TEXTFIELD_SCROLL_DOWN)
        {
            set.SCROLLING_KEY_DOWN.setKeyEventName(eventName);
        }

        set.saveSettings();
        return true;
    }

    private boolean isKeybindingReserved(String eventName)
    {
        SettingsClient set = QuickHotbarMod.clientSettings;

        if (eventName == set.SCROLLING_KEY.getEventName() ||
        		eventName == set.SCROLLING_KEY_SWITCH_MODE.getEventName() ||
        		eventName == set.KEY_OPEN_MOD_SETTINGS_MENU.getEventName() ||
        		eventName == set.SCROLLING_KEY_UP.getEventName() ||
                eventName == set.SCROLLING_KEY_DOWN.getEventName())
        {
            return true;
        }

        return false;
    }

    private void setErrorTooltip(String text, int x, int y)
    {
        errorSetTime = Minecraft.getMinecraft().getSystemTime();
        errorText = text;
        errorX = x;
        errorY = y;
    }

    @Override public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);

        fontRenderer.drawString(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.keymodifier")+":", width / 2 - 160, height / 2 - 45, 0x000000);

        fontRenderer.drawString(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.keyswitchmode")+":", width / 2 - 160, height / 2 + 5, 0x000000);
        fontRenderer.drawString(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.keyopeninventory")+":", width / 2 - 160, height / 2 + 35, 0x000000);

        fontRenderer.drawString(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.keymanualscrollup")+":", width / 2 - 30, height / 2 + 5, 0x000000);
        fontRenderer.drawString(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.keymanualscrolldown")+":", width / 2 - 30, height / 2 + 35, 0x000000);

        boolean updateCursor = Minecraft.getMinecraft().getSystemTime() % 3 == 0 ? true : false;
        for (GuiTextField textfield : textfieldBindings)
        {
            if (textfield != null) textfield.drawTextBox();
            if (updateCursor) textfield.updateCursorCounter();
            if (textfield.isFocused())
            {
                ToolTipHelper.setTooltip(TranslationHelper.translateString("quickhotbarmod.gui.keybindings.typekeyhelptext"), textfield.xPosition - (textfield.width / 6) + 1, textfield.yPosition);
                errorSetTime = 0;
            }
        }

        if (errorSetTime != 0 && Minecraft.getMinecraft().getSystemTime() - errorSetTime < 2400)
        {
            ToolTipHelper.setTooltip(errorText, errorX, errorY);
        }
        else
        {
            errorSetTime = 0;
        }

        ToolTipHelper.renderToolTipAndClear(this);
    }
}