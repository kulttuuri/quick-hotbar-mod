package com.kulttuuri.quickhotbar.settings;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.lwjgl.input.Keyboard;

public class SettingsClient extends SettingsGlobal
{
	/** Key that needs to be held down for user to be able to scroll the inventory rows. */
	public int SCROLLING_KEY = Keyboard.KEY_LCONTROL;
    /** Key that needs to be held down for user to be able to scroll the inventory row columns. */
    public int SCROLLING_KEY_SWITCH_MODE = Keyboard.KEY_C;
    /** Key that opens settings menu for the mod. User needs to also hold down the SCROLLING_KEY. */
    public int KEY_OPEN_MOD_SETTINGS_MENU = Keyboard.KEY_M;
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
	/** Key used for scrolling inventory rows up with keyboard. */
	public int SCROLLING_KEY_UP = Keyboard.KEY_UP;
	/** Key used for scrolling inventory rows down with keyboard. */
	public int SCROLLING_KEY_DOWN = Keyboard.KEY_DOWN;
	/** Should we announce when player joins into server that this mod has been loaded? */
	public boolean ANNOUNCE_MOD_LOADED = true;
    /** Is user able to open mod settings menu? */
    public boolean ENABLE_SETTING_MENU = true;

    /** When connecting to a server, server syncs if it has mod installed and if functionality should be handled serverside. */
    public static boolean handleInventorySwitchInServer = false;

    private static boolean SETTINGS_LOADED = false;

    private Configuration config;
    private Property announceModLoaded;
    private Property enableSettingMenu;
    private Property keyBindingsScroll;
    private Property keyBindingSwitchMode;
    private Property keyBindingOpenSettingsMenu;
    private Property allowModeSwitching;
    private Property modeSwitchingIsDefaultMode;
    private Property reverseMouseWheelScrolling;
    private Property immediately_show_popup_menu;
    private Property allowKeyboardScroll;
    private Property scrollKeyUp;
    private Property scrollKeyDown;

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

		// Load settings
		ANNOUNCE_MOD_LOADED = announceModLoaded.getBoolean(true);
        ENABLE_SETTING_MENU = enableSettingMenu.getBoolean(true);
		SCROLLING_KEY = loadKeybindingFromFile(keyBindingsScroll.getString().trim(), Keyboard.KEY_LCONTROL);
        SCROLLING_KEY_SWITCH_MODE = loadKeybindingFromFile(keyBindingSwitchMode.getString().trim(), Keyboard.KEY_LCONTROL);
        KEY_OPEN_MOD_SETTINGS_MENU = loadKeybindingFromFile(keyBindingOpenSettingsMenu.getString().trim(), Keyboard.KEY_M);
        ALLOW_MODE_SWITCHING = allowModeSwitching.getBoolean(true);
        CURRENT_SWITCH_MODE_ROW = modeSwitchingIsDefaultMode.getBoolean(true);
		IMMEDIATELY_SHOW_POPUP_MENU = immediately_show_popup_menu.getBoolean(false);
        REVERSE_MOUSEWHEEL_SCROLLING = reverseMouseWheelScrolling.getBoolean(false);
		ALLOW_SCROLLING_WITH_KEYBOARD = allowKeyboardScroll.getBoolean(true);
		SCROLLING_KEY_UP = loadKeybindingFromFile(scrollKeyUp.getString().trim(), Keyboard.KEY_UP);
		SCROLLING_KEY_DOWN = loadKeybindingFromFile(scrollKeyDown.getString().trim(), Keyboard.KEY_DOWN);
		
		// Save comments for settings
		announceModLoaded.comment = "When you join a game, this mod prints out that is has been loaded and it's keybindings. Set to false to disable this behavior. Default: true";
		enableSettingMenu.comment = "If this is true, you are able to open mod settings menu. Default: true";
        keyBindingsScroll.comment = "Key which you need to hold down to scroll between inventory rows. Default: KEY_LCONTROL. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html";
        keyBindingSwitchMode.comment = "Key which you can use to switch between row and column switching while also holding the scrolling key. Default: KEY_C. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html";
        allowModeSwitching.comment = "Should you be able to change between row and column switching modes. Default: true";
        keyBindingOpenSettingsMenu.comment = "Keybinding which opens the settings menu. Note that you will also need to hold down the scrolling key to open the menu, for ex. ctrl + m. Default: KEY_M";
        modeSwitchingIsDefaultMode.comment = "If true, by default you will browse between rows. If false by default you will browse through columns. Default: true";
        immediately_show_popup_menu.comment = "If this is true, popup menu will be shown immediately instead of waiting till user scrolls. Default: false";
		reverseMouseWheelScrolling.comment = "If this is true, mousewheel scrolling will be reversed. Default: false";
        allowKeyboardScroll.comment = "If this is true, user can scroll inventory rows with bind keyboard keys. Default: true";
		scrollKeyUp.comment = "Keyboard key which can be used to scroll inventory rows up. Default: KEY_UP. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html";
		scrollKeyDown.comment = "Keyboard key which can be used to scroll inventory rows down. Default: KEY_DOWN. Should you wish to change this key, you can find all supported keys from here: http://www.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html";
		
		config.save();
	}

    public void saveSettings()
    {
        announceModLoaded.set(ANNOUNCE_MOD_LOADED);
        keyBindingsScroll.set(Keyboard.getKeyName(SCROLLING_KEY));
        keyBindingSwitchMode.set(Keyboard.getKeyName(SCROLLING_KEY_SWITCH_MODE));
        keyBindingOpenSettingsMenu.set(Keyboard.getKeyName(KEY_OPEN_MOD_SETTINGS_MENU));
        allowModeSwitching.set(ALLOW_MODE_SWITCHING);
        modeSwitchingIsDefaultMode.set(CURRENT_SWITCH_MODE_ROW);
        reverseMouseWheelScrolling.set(REVERSE_MOUSEWHEEL_SCROLLING);
        immediately_show_popup_menu.set(IMMEDIATELY_SHOW_POPUP_MENU);
        allowKeyboardScroll.set(ALLOW_SCROLLING_WITH_KEYBOARD);
        scrollKeyUp.set(Keyboard.getKeyName(SCROLLING_KEY_UP));
        scrollKeyDown.set(Keyboard.getKeyName(SCROLLING_KEY_DOWN));

        config.save();
    }
	
	private int loadKeybindingFromFile(String keyBindingKey, int defaultKey)
	{
		if (keyBindingKey == null || keyBindingKey.equals(""))
		{
			return defaultKey;
		}
		
		if (keyBindingKey.startsWith("KEY_")) keyBindingKey = keyBindingKey.substring(4, keyBindingKey.length());

		if (Keyboard.getKeyIndex(keyBindingKey) == Keyboard.KEY_NONE)
		{
			return defaultKey;
		}
		else
		{
			return Keyboard.getKeyIndex(keyBindingKey);
		}
	}

    public void setCurrentSwitchMode(boolean rowSwitchMode)
    {
        modeSwitchingIsDefaultMode.set(rowSwitchMode);
        CURRENT_SWITCH_MODE_ROW = rowSwitchMode;
        config.save();
    }
}