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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * To render 16x16 icons from iconfactory.png file.
 */
public class IconFactory
{
	public static final int ICON_BRICKS = 1;
	public static final int ICON_APPLICATION_VIEW_ICONS = 2;
	public static final int ICON_SETTINGS = 3;
	
	/** Path to texture file. */
	private static final ResourceLocation texturePath = new ResourceLocation(QuickHotbarModInfo.MODID, "gui/iconfactory.png");
	/** Reference to current GuiScreen. */
	private static Gui gui;
	
	/**
	 * Initializes the IconFactory. Call this EVERY time you
	 * start to render using the {@link #renderIcon(int, int, int)}.
	 * @param mc Reference to running Minecraft.
	 * @param gui Reference to current GuiScreen.
	 */
	public static void init(Minecraft mc, Gui gui)
	{
		IconFactory.gui = gui;
		mc.renderEngine.bindTexture(texturePath);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(texturePath));
	}
	
	/**
	 * Renders the given icon from the file.<br>
	 * <b>Remember to make a call to init(Minecraft, Gui) before rendering!</b>
	 * @param iconID ID of the icon. check EduIconFactory.ICON_ enums.
	 * @param x X position in the screen where you want to render the icon.
	 * @param y Y position in the screen where you want to render the icon.
	 */
	public static void renderIcon(int iconID, int x, int y)
	{
		iconID--;
		int cell = (iconID % 16) + 1;
		int row = Math.round(iconID / 16);
        gui.drawTexturedModalRect(x, y, (cell * 16) - 16, row * 16, 16, 16);
	}
}