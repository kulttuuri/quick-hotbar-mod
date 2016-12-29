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

package com.kulttuuri.quickhotbar.settings;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.QuickHotbarKeyBinding;

public class SettingsClient extends SettingsGlobal
{
	/** Key that needs to be held down for user to be able to scroll the inventory rows. */
	public QuickHotbarKeyBinding SCROLLING_KEY;
    /** Key that needs to be held down for user to be able to scroll the inventory row columns. */
    public QuickHotbarKeyBinding SCROLLING_KEY_SWITCH_MODE;
    /** Key that opens settings menu for the mod. User needs to also hold down the SCROLLING_KEY. */
    public QuickHotbarKeyBinding KEY_OPEN_MOD_SETTINGS_MENU;
	/** Key used for scrolling inventory rows up with keyboard. */
	public QuickHotbarKeyBinding SCROLLING_KEY_UP;
	/** Key used for scrolling inventory rows down with keyboard. */
	public QuickHotbarKeyBinding SCROLLING_KEY_DOWN;
	
    /** Should we reverse the mousewheel scrolling? */
    public boolean REVERSE_MOUSEWHEEL_SCROLLING = false;
	/** Should the item popup menu automatically come up once you press the scrolling key? */
	public boolean IMMEDIATELY_SHOW_POPUP_MENU = false;
    /** Do we allow user to switch between row and column switching? */
    public boolean ALLOW_MODE_SWITCHING = true;
    /** Is current switch mode rows? If false, we switch columns instead of rows. */
	public boolean CURRENT_SWITCH_MODE_ROW = true;
	/** Should we also allow user to scroll with keyboard keys? */
	public boolean ALLOW_SCROLLING_WITH_KEYBOARD = true;
	/** Should we announce when player joins into server that this mod has been loaded? */
	public boolean ANNOUNCE_MOD_LOADED = true;
    /** Is user able to open mod settings menu? */
    public boolean ENABLE_SETTING_MENU = true;
    /** Is user able to hold down numeric key and scroll that column immediately? */
    public boolean ENABLE_NUMBER_SCROLLING = true;

    /** When connecting to a server, server syncs if it has mod installed and if functionality should be handled serverside. */
    public static boolean handleInventorySwitchInServer = false;

    private static boolean SETTINGS_LOADED = false;

    private Configuration config;
    private Property keyBindingsScroll;
    private Property keyBindingSwitchMode;
    private Property keyBindingOpenSettingsMenu;
    private Property scrollKeyUp;
    private Property scrollKeyDown;
    private Property announceModLoaded;
    private Property enableSettingMenu;
    private Property allowModeSwitching;
    private Property modeSwitchingIsDefaultMode;
    private Property reverseMouseWheelScrolling;
    private Property immediately_show_popup_menu;
    private Property allowKeyboardScroll;
    private Property enableNumberScrolling;

	@Override
	public void loadSettingsFromFile(File configurationFile)
	{
        if (SETTINGS_LOADED) return;

        SETTINGS_LOADED = true;
		config = new Configuration(configurationFile);
		config.load();
		announceModLoaded = config.get("general", "announce_mod_loaded", true);
        enableSettingMenu = config.get("general", "enable_settings_menu_gui", true);
		keyBindingsScroll = config.get("keybindings", "scrolling_key", "KEY_LCONTROL");
        keyBindingSwitchMode = config.get("keybindings", "switch_scrolling_mode_key", "KEY_C");
        keyBindingOpenSettingsMenu = config.get("keybindings", "open_settings_menu_key", "KEY_M");
        allowModeSwitching = config.get("general", "allow_mode_switching", true);
        modeSwitchingIsDefaultMode = config.get("general", "mode_switching_default_mode", true);
        reverseMouseWheelScrolling = config.get("general", "reverse_mousewheel_scrolling", false);
		immediately_show_popup_menu = config.get("general", "immediately_show_popup_menu", false);
		allowKeyboardScroll = config.get("general", "allow_scrolling_with_keyboard_keys", true);
		scrollKeyUp = config.get("keybindings", "keyboard_scroll_key_up", "KEY_UP");
		scrollKeyDown = config.get("keybindings", "keyboard_scroll_key_down", "KEY_DOWN");
        enableNumberScrolling = config.get("general", "allow_number_scrolling", true);

		// Load settings
		SCROLLING_KEY = new QuickHotbarKeyBinding(keyBindingsScroll.getString().trim(), Keyboard.KEY_LCONTROL);
        SCROLLING_KEY_SWITCH_MODE = new QuickHotbarKeyBinding(keyBindingSwitchMode.getString().trim(), Keyboard.KEY_C);
        KEY_OPEN_MOD_SETTINGS_MENU = new QuickHotbarKeyBinding(keyBindingOpenSettingsMenu.getString().trim(), Keyboard.KEY_M);
		SCROLLING_KEY_UP = new QuickHotbarKeyBinding(scrollKeyUp.getString().trim(), Keyboard.KEY_UP);
		SCROLLING_KEY_DOWN = new QuickHotbarKeyBinding(scrollKeyDown.getString().trim(), Keyboard.KEY_DOWN);
		ANNOUNCE_MOD_LOADED = announceModLoaded.getBoolean(true);
        ENABLE_SETTING_MENU = enableSettingMenu.getBoolean(true);
        ALLOW_MODE_SWITCHING = allowModeSwitching.getBoolean(true);
        CURRENT_SWITCH_MODE_ROW = modeSwitchingIsDefaultMode.getBoolean(true);
		IMMEDIATELY_SHOW_POPUP_MENU = immediately_show_popup_menu.getBoolean(false);
        REVERSE_MOUSEWHEEL_SCROLLING = reverseMouseWheelScrolling.getBoolean(false);
		ALLOW_SCROLLING_WITH_KEYBOARD = allowKeyboardScroll.getBoolean(true);
        ENABLE_NUMBER_SCROLLING = enableNumberScrolling.getBoolean(true);
		
		// Save comments for settings
		announceModLoaded.setComment("When you join a game, this mod prints out that is has been loaded and it's keybindings. Set to false to disable this behavior. Default: true");
		enableSettingMenu.setComment("If this is true, you are able to open mod settings menu. Default: true");
        keyBindingsScroll.setComment("Key which you need to hold down to scroll between inventory rows. Default: KEY_LCONTROL. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html");
        keyBindingSwitchMode.setComment("Key which you can use to switch between row and column switching while also holding the scrolling key. Default: KEY_C. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html");
        allowModeSwitching.setComment("Should you be able to change between row and column switching modes. Default: true");
        keyBindingOpenSettingsMenu.setComment("Keybinding which opens the settings menu. Note that you will also need to hold down the scrolling key to open the menu, for ex. ctrl + m. Default: KEY_M");
        modeSwitchingIsDefaultMode.setComment("If true, by default you will browse between rows. If false by default you will browse through columns. Default: true");
        immediately_show_popup_menu.setComment("If this is true, popup menu will be shown immediately instead of waiting till user scrolls. Default: false");
		reverseMouseWheelScrolling.setComment("If this is true, mousewheel scrolling will be reversed. Default: false");
        allowKeyboardScroll.setComment("If this is true, user can scroll inventory rows with bind keyboard keys. Default: true");
		scrollKeyUp.setComment("Keyboard key which can be used to scroll inventory rows up. Default: KEY_UP. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html");
		scrollKeyDown.setComment("Keyboard key which can be used to scroll inventory rows down. Default: KEY_DOWN. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html");
		enableNumberScrolling.setComment("Should you be able to hold down inventory number slot and scroll that column. Default: true");

		config.save();
	}

    public void saveSettings()
    {
        announceModLoaded.set(ANNOUNCE_MOD_LOADED);
        keyBindingsScroll.set(SCROLLING_KEY.getEventName());
        keyBindingSwitchMode.set(SCROLLING_KEY_SWITCH_MODE.getEventName());
        keyBindingOpenSettingsMenu.set(KEY_OPEN_MOD_SETTINGS_MENU.getEventName());
        scrollKeyUp.set(SCROLLING_KEY_UP.getEventName());
        scrollKeyDown.set(SCROLLING_KEY_DOWN.getEventName());        
        allowModeSwitching.set(ALLOW_MODE_SWITCHING);
        modeSwitchingIsDefaultMode.set(CURRENT_SWITCH_MODE_ROW);
        reverseMouseWheelScrolling.set(REVERSE_MOUSEWHEEL_SCROLLING);
        immediately_show_popup_menu.set(IMMEDIATELY_SHOW_POPUP_MENU);
        allowKeyboardScroll.set(ALLOW_SCROLLING_WITH_KEYBOARD);
        enableNumberScrolling.set(ENABLE_NUMBER_SCROLLING);

        config.save();
    }

    public void setCurrentSwitchMode(boolean rowSwitchMode)
    {
        modeSwitchingIsDefaultMode.set(rowSwitchMode);
        CURRENT_SWITCH_MODE_ROW = rowSwitchMode;
        config.save();
    }
}