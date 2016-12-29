package com.kulttuuri.quickhotbar;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import scala.Console;

public class QuickHotbarKeyBinding
{
	// Key name
	private String eventName = "";
	// Key number value
	private int eventKey = 0;
	// Is this keyboard key?
	private boolean isKeyboardKey = true;
	
	/**
	 * Construct new quick hotbar mod keybinding.
	 * @param eventName Keyboard or mouse event name.
	 * @param defaultKey if passed key is empty or erroneous, we can revert to passed default key.
	 */
	public QuickHotbarKeyBinding(String eventName, int defaultKey)
	{
		setKeyProperties(eventName, defaultKey);
	}
	
	public void setKeyEventName(String eventName)
	{
		setKeyProperties(eventName, this.eventKey);
	}
	
	private void setKeyProperties(String eventName, int defaultKey)
	{
		if (eventName == null || eventName.equals(""))
		{
			this.isKeyboardKey = true;
			this.eventName = Keyboard.getKeyName(defaultKey);
			this.eventKey = defaultKey;
			return;
		}
		
		// Mouse press
		if (eventName.startsWith("BUTTON"))
		{
			this.isKeyboardKey = false;
			this.eventName = eventName;
			this.eventKey = Mouse.getButtonIndex(eventName);
		}
		// Keyboard key
		else
		{
			this.isKeyboardKey = true;

			if (Keyboard.getKeyIndex(eventName) == Keyboard.KEY_NONE)
			{
				this.eventName = Keyboard.getKeyName(defaultKey);
				this.eventKey = defaultKey;
			}
			else
			{
				this.eventName = eventName;
				this.eventKey = Keyboard.getKeyIndex(eventName);
			}
		}
	}
	
	/**
	 * Get Key event name. See Keyboard.getKeyName().
	 * @return key event name.
	 */
	public String getEventName()
	{
		return this.eventName;
	}
	
	/**
	 * Gets event name without the beginning KEY_ (if key name contains it)
	 * @return event name without KEY_ or the whole name.
	 */
	public String getEventNameShort()
	{
		if (this.eventName.startsWith("KEY_")) return this.eventName.substring(4, this.eventName.length());
		else return this.eventName;
	}
	
	/**
	 * Get event key index value. See Keyboard.getEventKey().
	 * @return Event key index value.
	 */
	public int getEventKey()
	{
		if (isKeyboardKey)
			return Keyboard.getKeyIndex(this.eventName);
		else
			return Mouse.getButtonIndex(this.eventName);
	}
	
	/**
	 * Is this keyboard or mouse key?
	 * @return true if keyboard press, false if mouse.
	 */
	public boolean isKeyboardKey()
	{
		return this.isKeyboardKey;
	}
	
	/**
	 * Checks is the keybinding key currently down.
	 * @return true if key is currently down, false otherwise.
	 */
	public boolean isKeyDown()
	{
		if (this.isKeyboardKey)
		{
			if (Keyboard.isKeyDown(this.eventKey)) return true;
		}
		else
		{
			if (Mouse.isButtonDown(this.eventKey)) return true;
		}
		return false;
	}
}
