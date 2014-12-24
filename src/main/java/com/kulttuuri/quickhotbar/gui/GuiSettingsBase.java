package com.kulttuuri.quickhotbar.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kulttuuri.quickhotbar.QuickHotbarEventHandler;
import com.kulttuuri.quickhotbar.QuickHotbarModInfo;
import com.kulttuuri.quickhotbar.gui.components.GuiButtonBetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;

/**
 * Base class for GUIs.
 * @author Aleksi Postari / Kulttuuri
 * @since 1.03
 */
public abstract class GuiSettingsBase extends GuiScreen
{
    protected List<GuiButtonBetter> tabList = new ArrayList<GuiButtonBetter>();
    public static GuiSettingsBase currentGuiScreen;
    private GuiButtonBetter buttonCloseGUI;
    public FontRenderer fontRenderer;

    private static final int TAB_INFORMATION = 0;
    private static final int TAB_SETTINGS = 1;
    private static final int TAB_KEYBINDINGS = 2;

    /** Overridden to store custom GuiButtons instead of stupid vanilla ones. */
    protected List<GuiButtonBetter> buttonList = new ArrayList<GuiButtonBetter>();

    public GuiSettingsBase()
    {
        this.fontRenderer = fontRendererObj;
        QuickHotbarEventHandler.renderQuickHotbarPreview = false;
    }

    /**
     * To initialize tab system in the admin menu.
     */
    public void initTabs()
    {
        // Initialize Close GUI Button
        /*buttonCloseGUI = new GuiButtonBetter(0, width / 2 + 154, height / 2 - 100, 21, 20, "");
        buttonCloseGUI.buttonTextureFile = new ResourceLocation(QuickHotbarModInfo.MODID, "gui/buttonClose.png");
        buttonCloseGUI.setTextureRenderPositions(20, -20);
        buttonCloseGUI.isSelected = false;*/

        // Clear tablist
        tabList.clear();

        // Add tab buttons
        int bSize = 110;
        int nextButtonX = width / 2 - 171;
        int buttonY = height / 2 - 76;

        tabList.add(new GuiButtonBetter(0, nextButtonX, buttonY, bSize, 20, "Information"));
        tabList.add(new GuiButtonBetter(1, nextButtonX+=113, buttonY, bSize, 20, "Settings"));
        tabList.add(new GuiButtonBetter(2, nextButtonX+=113, buttonY, bSize, 20, "Keybindings"));

        for (GuiButtonBetter button : tabList)
        {
            button.alignToLeft = true;
            button.leftPadding = 24;
        }

        // Set current tab selected
        setTabSelected();
    }

    // Override mouseClicked to check if tab was selected
    @Override
    protected void mouseClicked(int i, int j, int k)
    {
        if (k == 0)
        {
            // Go through tabList and check if tab was changed
            for (int l = 0; l < tabList.size(); l++)
            {
                GuiButtonBetter guibutton = tabList.get(l);
                if (guibutton.mousePressed(mc, i, j))
                {
                    actionPerformedTab(guibutton);
                    return;
                }
            }

            // Check if close GUI button was pressed
            /*if (buttonCloseGUI.mousePressed(mc, i, j))
            {
                closeGui();
                return;
            }*/

            // If not, then go thru controlList
            for(int l = 0; l < buttonList.size(); l++)
            {
                GuiButtonBetter guibutton = buttonList.get(l);
                if(guibutton.mousePressed(mc, i, j))
                {
                    try
                    {
                        actionPerformed(guibutton);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Overrided setWorldAndResolution to execute initTabs everytime
    @Override
    public void setWorldAndResolution(Minecraft minecraft, int i, int j)
    {
        mc = minecraft;
        fontRenderer = minecraft.fontRendererObj;
        width = i;
        height = j;
        buttonList.clear();
        initGui();
        initTabs();
    }

    /**
     * Sets current tab selected.
     */
    public void setTabSelected()
    {
        if (currentGuiScreen instanceof GuiSettingsInformation)
            tabList.get(TAB_INFORMATION).drawTransparent = false;
        else
            tabList.get(TAB_INFORMATION).drawTransparent = true;

        if (currentGuiScreen instanceof GuiSettingsGeneral)
            tabList.get(TAB_SETTINGS).drawTransparent = false;
        else
            tabList.get(TAB_SETTINGS).drawTransparent = true;

        if (currentGuiScreen instanceof GuiSettingsKeyBindings)
            tabList.get(TAB_KEYBINDINGS).drawTransparent = false;
        else
            tabList.get(TAB_KEYBINDINGS).drawTransparent = true;
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        // Close menu on ESC press / last edu admin GUI binded key
        if (i == Keyboard.KEY_ESCAPE)
        {
            closeGui();
        }
    }

    /**
     * If tab button was pressed, check what was pressed and make action.
     * @param guibutton Pressed GuiButton.
     */
    protected void actionPerformedTab(GuiButton guibutton)
    {
        // Information
        if (guibutton.id == TAB_INFORMATION)
        {
            mc.displayGuiScreen(new GuiSettingsInformation());
        }
        if (guibutton.id == TAB_SETTINGS)
        {
            mc.displayGuiScreen(new GuiSettingsGeneral());
        }
        if (guibutton.id == TAB_KEYBINDINGS)
        {
            mc.displayGuiScreen(new GuiSettingsKeyBindings());
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        // Draw gui background
        drawGuiBackground(2);

        // Draw controls
        for (int k = 0; k < buttonList.size(); k++)
        {
            GuiButton guibutton = buttonList.get(k);
            guibutton.drawButton(mc, i, j);
        }

        // Draw close & tab size buttons
        //buttonCloseGUI.drawButton(mc, i, j);

        // Draw tabs
        for (int k = 0; k < tabList.size(); k++)
        {
            GuiButton guibutton = tabList.get(k);
            guibutton.drawButton(mc, i, j);
        }

        // Fixes icon blinking
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);

        IconFactory.init(mc, this);

        // Render close button icon
        //IconFactory.renderIcon(IconFactory.ICON_CROSS, width / 2 + 155, height / 2 - 97);

        // Render tab icons
        IconFactory.renderIcon(IconFactory.ICON_APPLICATION_VIEW_ICONS, width / 2 - 216 + 51, height / 2 - 74);
        IconFactory.renderIcon(IconFactory.ICON_SETTINGS, width / 2 - 104 + 51, height / 2 - 74);
        IconFactory.renderIcon(IconFactory.ICON_BRICKS, width / 2 + 9 + 51, height / 2 - 74);
    }

    /**
     * Draws admin gui background.
     * @param f
     */
    protected void drawGuiBackground(float f)
    {
        int xSize = 256;
        int ySize = 166;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation(QuickHotbarModInfo.MODID, "gui/settingsmenubackground.png"));
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        // Render left side
        drawTexturedModalRect(j - 50, k, 0, 0, xSize, ySize);
        // Render center
        //drawTexturedModalRect(j, k, 5, 0, xSize, ySize);
        // Render right side
        drawTexturedModalRect(j + 50, k, 5, 0, xSize-5, ySize);
        //drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Sets button state (Checked or not checked).
     * Will just return if state was same.
     * @param button EduGuiButton
     * @param state State that needs to be set.
     */
    protected void setButtonState(GuiButtonBetter button, boolean state)
    {
        // If button will need to be enabled
        if (state)
        {
            // If state was already set, return
            if (button.buttonTextureFile.getResourcePath().equals("gui/buttonEnabled.png")) return;
            // Otherwise set the state texture for the button
            button.buttonTextureFile = new ResourceLocation(QuickHotbarModInfo.MODID, "gui/buttonEnabled.png");
        }
        // If button will need to be disableda
        else
        {
            // If state was already set, return
            if (button.buttonTextureFile.getResourcePath().equals("gui/buttonDisabled.png")) return;
            // Otherwise set the state texture for the button
            button.buttonTextureFile = new ResourceLocation(QuickHotbarModInfo.MODID, "gui/buttonDisabled.png");
        }
    }

    /**
     * Closes current GUI and goes back to game.
     */
    public void closeGui()
    {
        this.mc.displayGuiScreen((GuiScreen)null);
        this.mc.setIngameFocus();
    }
}