package com.kulttuuri.quickhotbar.gui;

import com.kulttuuri.quickhotbar.QuickHotbarModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * To render 16x16 icons from iconfactory.png file.
 * @author Aleksi Postari / Kulttuuri
 */
public class IconFactory
{
	public static final int ICON_UNHAPPYSMILEY = 1;
	public static final int ICON_SUIT = 2;
	public static final int ICON_APPLICATION_EDIT = 3;
	public static final int ICON_WORLD = 4;
	public static final int ICON_BASKET_GO = 5;
	public static final int ICON_FORM_EDIT = 6;
	public static final int ICON_GROUP = 7;
	public static final int ICON_HOME_GO = 8;
	public static final int ICON_BRICKS = 9;
	public static final int ICON_APPLICATION_VIEW_TILE = 10;
	public static final int ICON_APPLICATION_VIEW_ICONS = 11;
	public static final int ICON_CANCEL = 12;
	public static final int ICON_CROSS = 13;
	public static final int ICON_USER = 14;
	public static final int ICON_USER_FEMALE = 15;
	public static final int ICON_SERVER = 16;
	public static final int ICON_STUDENT = 17;
	public static final int ICON_TEACHER = 18;
	public static final int ICON_SPECTATE_MODE = 19;
	public static final int ICON_GAMEMODE_NORMAL = 20;
	public static final int ICON_GAMEMODE_CREATIVE = 21;
	public static final int ICON_MUTE = 22;
	public static final int ICON_FREEZE = 23;
	public static final int ICON_BRICK = 24;
	public static final int ICON_BRICK_ONE = 25;
	public static final int ICON_COPY = 26;
	public static final int ICON_PASTE = 27;
	public static final int ICON_SETTINGS = 28;
	public static final int ICON_TELEPORT_PLAYER_HERE = 29;
	public static final int ICON_TELEPORT_SELF_TO_PLAYER = 30;	
	public static final int ICON_TELEPORT_ALL = 31;
	public static final int ICON_UNFREEZE = 32;
	public static final int ICON_MUTE_STUDENT = 33;
	public static final int ICON_UNMUTE_STUDENT = 34;
	public static final int ICON_UNMUTE_ALL_STUDENTS = 35;
	public static final int ICON_MUTE_ALL_STUDENTS = 36;
	public static final int ICON_UNFREEZE_ALL_STUDENTS = 37;
	public static final int ICON_FREEZE_ALL_STUDENTS = 38;
	public static final int ICON_ARROW_UP = 39;
	public static final int ICON_ARROW_DOWN = 40;
	public static final int ICON_BRICKS_DISABLED = 41;
	public static final int ICON_SIGN_BIG = 42;
	public static final int ICON_SIGN_SMALL = 43;
	public static final int ICON_SIGN = 44;
	public static final int ICON_FILL_CROSSHAIR = 45;
	public static final int ICON_BUILDMODE_CROSSHAIR = 46;
	public static final int ICON_FILL_CROSSHAIR_RED = 47;
	public static final int ICON_BUILDMODE_PICAXE = 48;
	public static final int ICON_CLEAR = 49;
	public static final int ICON_FILL = 50;
	public static final int ICON_CLEAR_RED = 51;
	public static final int ICON_FILL_RED = 52;
	public static final int ICON_ADD = 53;
	public static final int ICON_HOME = 54;
	public static final int ICON_TELEPORT_TO_SURFACE = 55;
	public static final int ICON_CHECKED = 56;
	public static final int ICON_UNCHECKED = 57;
	public static final int ICON_SCRIPT = 58;
	public static final int ICON_SPAWN = 59;
	
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