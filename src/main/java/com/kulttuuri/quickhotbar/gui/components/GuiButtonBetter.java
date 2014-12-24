package com.kulttuuri.quickhotbar.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiButtonBetter extends GuiButton
{
    // Align text to left instead of centering
    public boolean alignToLeft = false;
    // If aligned to left, you can add left padding with this
    public int leftPadding = 0;
    // Hovering text properties
    private int hoverX = 0;
    private int hoverY = 0;
    private int hoverTextColor = 0xffffff;
    private String hoverText = "";
    // Graphics
    public ResourceLocation buttonTextureFile = new ResourceLocation("textures/gui/widgets.png");
	// Positions where guiButton will be rendered from the textureFile
    private int texturePosX = 200;
    private int texturePosY = 46;
    
    public boolean isSelected = false;
    public boolean isVisible = false;
    public boolean drawTransparent = false;
	
    public GuiButtonBetter(int i, int j, int k, String s)
    {
        super(i, j, k, s);
    }

    public GuiButtonBetter(int i, int j, int k, int l, int i1, String s)
    {
        super(i, j, k, l, i1, s);
    }
    
    // New constructor to create button and set also hovering text properties
    public GuiButtonBetter(int i, int j, int k, int l, int i1, String s, int hoverX, int hoverY, String hoverText, int hoverTextColor)
    {
        super(i, j, k, l, i1, s);
        this.hoverX = hoverX;
        this.hoverY = hoverY;
        this.hoverText = hoverText;
        this.hoverTextColor = hoverTextColor;
    }

    /**
     * Sets text for this button.
     * @param text text to set for the button.
     */
    public void setText(String text)
    {
        this.displayString = text;
    }

    // Sets hovering text properties
    public void setHoveringText(int hoverX, int hoverY, String hoverText, int hoverTextColor)
    {
        this.hoverX = hoverX;
        this.hoverY = hoverY;
        this.hoverText = hoverText;
        this.hoverTextColor = hoverTextColor;
    }
    
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if(!visible)
        {
            return;
        }
        FontRenderer fontrenderer = minecraft.fontRendererObj;
        //GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture(buttonTextureFile));
        minecraft.renderEngine.bindTexture(buttonTextureFile);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, this.drawTransparent ? 0.5f : 1.0f);
        boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
        int k = getHoverState(flag);
        drawTexturedModalRect(xPosition, yPosition, 0, texturePosY + k * 20, width / 2, height);
        drawTexturedModalRect(xPosition + width / 2, yPosition, texturePosX - width / 2, texturePosY + k * 20, width / 2, height);
        mouseDragged(minecraft, i, j);
        if (!enabled)
        {
            if (alignToLeft)
                drawString(fontrenderer, displayString, xPosition + leftPadding, yPosition + (height - 8) / 2, 0xffa0a0a0);
            else
                drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xffa0a0a0);
        }
        else if (flag)
        {
            if (!hoverText.equals("")) ToolTipHelper.setTooltip(hoverText, i, j);
        	//drawCenteredString(fontrenderer, hoverText, hoverX, hoverY, hoverTextColor); // Show hovertext
            if (alignToLeft)
                drawString(fontrenderer, displayString, xPosition + leftPadding, yPosition + (height - 8) / 2, 0xffffa0);
            else
                drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xffffa0);
        }
        else
        {
            if (alignToLeft)
                drawString(fontrenderer, displayString, xPosition + leftPadding, yPosition + (height - 8) / 2, drawTransparent ? 0xffa0a0a0 : 0xe0e0e0);
            else
                drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, drawTransparent ? 0xffa0a0a0 : 0xe0e0e0);
        }
    }

    // Sets the position where the button will be rendered from the textureFile
    public void setTextureRenderPositions(int posX, int posY)
    {
        this.texturePosX = posX;
        this.texturePosY = posY;
    }
    protected void drawHoveringText(String par1Str, int par2, int par3, Minecraft minecraft)
    {
        if (hoverText.equals("")) return;
        
    	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        int var4 = minecraft.fontRendererObj.getStringWidth(par1Str);
        int var5 = par2 + 12;
        int var6 = par3 - 12;
        byte var8 = 8;
        this.zLevel = 300.0F;
        //itemRenderer.zLevel = 300.0F;
        int var9 = -267386864;
        this.drawGradientRect(var5 - 3, var6 - 4, var5 + var4 + 3, var6 - 3, var9, var9);
        this.drawGradientRect(var5 - 3, var6 + var8 + 3, var5 + var4 + 3, var6 + var8 + 4, var9, var9);
        this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 + var8 + 3, var9, var9);
        this.drawGradientRect(var5 - 4, var6 - 3, var5 - 3, var6 + var8 + 3, var9, var9);
        this.drawGradientRect(var5 + var4 + 3, var6 - 3, var5 + var4 + 4, var6 + var8 + 3, var9, var9);
        int var10 = 1347420415;
        int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
        this.drawGradientRect(var5 - 3, var6 - 3 + 1, var5 - 3 + 1, var6 + var8 + 3 - 1, var10, var11);
        this.drawGradientRect(var5 + var4 + 2, var6 - 3 + 1, var5 + var4 + 3, var6 + var8 + 3 - 1, var10, var11);
        this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 - 3 + 1, var10, var10);
        this.drawGradientRect(var5 - 3, var6 + var8 + 2, var5 + var4 + 3, var6 + var8 + 3, var11, var11);
        minecraft.fontRendererObj.drawString(par1Str, var5, var6, -1);
        this.zLevel = 0.0F;
        //itemRenderer.zLevel = 0.0F;
    }
}