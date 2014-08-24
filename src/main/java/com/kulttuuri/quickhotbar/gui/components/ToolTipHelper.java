package com.kulttuuri.quickhotbar.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Helps rendering tooltips in GUIs.
 * @author Aleksi Postari / Kulttuuri
 */
public class ToolTipHelper
{
	/** Text to be rendered */
	private static String text;
	/** Mouse x position */
	private static int mouseX;
	/** Mouse y position */
	private static int mouseY;
	private static boolean shouldRender;
	
	public ToolTipHelper()
	{
	}

	/**
	 * Sets tooltip text and mouse position
	 * @param text
	 * @param mouseX
	 * @param mouseY
	 */
	public static void setTooltip(String text, int mouseX, int mouseY)
	{
		ToolTipHelper.text = text;
		ToolTipHelper.mouseX = mouseX;
		ToolTipHelper.mouseY = mouseY;
		ToolTipHelper.shouldRender = true;
	}

	/**
	 * renders the actual tooltip text and background
	 * @param gui
	 */
	public static void renderToolTipAndClear(GuiScreen gui)
	{
		if (ToolTipHelper.shouldRender == false) return;

		try
		{
			String[] list = ToolTipHelper.text.split("<br>");
			String[] parseList = ToolTipHelper.text.split("<br>");
			int maxLength = 0;			
			// Height of the tooltip (all lines combined)
			int tooltipHeight = 15*list.length;
			int temp = 0;
			// Find out the longest line
			for (int o = 0; o < parseList.length; o++)
			{	
				// Remove colour tags so they don't affect line length
				parseList[o] = parseList[o].replaceAll("<red>", ""); parseList[o] = parseList[o].replaceAll("<white>", ""); parseList[o] = parseList[o].replaceAll("<green>", "");
				
				if (Minecraft.getMinecraft().fontRenderer.getStringWidth(parseList[o]) > maxLength)
					maxLength = Minecraft.getMinecraft().fontRenderer.getStringWidth(parseList[o]);
			}
			// Draw each line
			for (int i = 0; i < list.length; i++)
			{	
				renderTooltipLine(gui, list[i], ToolTipHelper.mouseX, ToolTipHelper.mouseY+30+temp, maxLength, tooltipHeight);
				// Draw next line 15 pixels down
				temp += 15;
			}

		}
		catch (Exception e)
		{
			
		}
		
        ToolTipHelper.clear();
	}
	/**
	 * Sets shouldrender to false after rendering
	 */
	private static void clear()
	{
		ToolTipHelper.shouldRender = false;
	}
	/**
	 * Renders given tooltip line
	 * @param gui Reference to Gui.
	 * @param text Text to be rendered.
	 * @param mouseX Mouse x coordinate.
	 * @param mouseY Mouse y coordinate.
	 * @param tooltipLength how long is the background of tooltip text.
	 */
	private static void renderTooltipLine(GuiScreen gui, String text, int mouseX, int mouseY, int tooltipLength, int tooltipHeight)
	{
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int var5 = mouseX + 12;
        int var6 = mouseY - 12;
        // If tooltip goes off screen horizontally, move it backwards
        if (gui.width - mouseX -15 < tooltipLength)
        {
        	int temp = gui.width - mouseX;     	
        	var5 -= tooltipLength - temp + 20;
        }
        // If tooltip goes off screen vertically, move it backwards
        if (gui.height - ToolTipHelper.mouseY - tooltipHeight -15 < 0)
        {
        	var6 -= Math.abs(gui.height - ToolTipHelper.mouseY - tooltipHeight -15);
        }
        
        byte var8 = 8;
        int var9 = -267386864;

        drawTooltipGradientRect(gui, var5 - 3, var6 - 4, var5 + tooltipLength + 3, var6 - 3, var9, var9);
        drawTooltipGradientRect(gui, var5 - 3, var6 - 3, var5 + tooltipLength + 3, var6 + var8 + 3, var9, var9);

        // Allows user to change tooltip color with tags
        text = text.replace("<red>", "\\247c");
        text = text.replace("<white>", "\\247f");
        text = text.replace("<green>", "\\2472");
        // Draws the string
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, var5, var6, - 1);
	}

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     * This is more see through than the original drawgradientrect.
     * This is used in tooltips.
     */
    private static void drawTooltipGradientRect(GuiScreen gui, int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float var7 = (float)(par5 >> 24 & 255) / 255.0F;
        float var8 = (float)(par5 >> 16 & 255) / 255.0F;
        float var9 = (float)(par5 >> 8 & 255) / 255.0F;
        float var10 = (float)(par5 & 255) / 255.0F;
        float var11 = (float)(par6 >> 24 & 255) / 255.0F;
        float var12 = (float)(par6 >> 16 & 255) / 255.0F;
        float var13 = (float)(par6 >> 8 & 255) / 255.0F;
        float var14 = (float)(par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        // Last parameter is the alpha value
        var15.setColorRGBA_F(var8, var9, var10, var7-0.3000000F);
        var15.addVertex((double)par3, (double)par2, 100);
        var15.addVertex((double)par1, (double)par2, 100);
        // Last parameter is the alpha value
        var15.setColorRGBA_F(var12, var13, var14, var11-0.3000000F);
        var15.addVertex((double)par1, (double)par4, 100);
        var15.addVertex((double)par3, (double)par4, 100);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}