package com.kulttuuri.quickhotbar.gui;

import com.kulttuuri.quickhotbar.QuickHotbarMod;
import com.kulttuuri.quickhotbar.QuickHotbarModInfo;
import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import com.kulttuuri.quickhotbar.gui.components.GuiButtonBetter;
import com.kulttuuri.quickhotbar.settings.SettingsClient;
import com.kulttuuri.quickhotbar.settings.SettingsServer;
import net.minecraft.client.gui.GuiButton;

/**
 * GUI for information screen.
 * @author Aleksi Postari / Kulttuuri
 */
public class GuiSettingsInformation extends GuiSettingsBase
{
    private static final int BUTTON_MOD_NETWORK_POST_THREAD = 0;

    public GuiSettingsInformation()
    {
        currentGuiScreen = this;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.clear();
        buttonList.add(new GuiButtonBetter(BUTTON_MOD_NETWORK_POST_THREAD, width / 2, height / 2, 100, 20, "Mod Information"));
    }

    @Override protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);

        if (guibutton.id == BUTTON_MOD_NETWORK_POST_THREAD)
        {
            System.out.println("Pressed OPEN URL button");
            String url = "http://adf.ly/7344368/minecraft-forum-post";
            // TODO: Open url in browser, use MC vanilla method for that.
        }
    }

    @Override public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);

        fontRenderer.drawString("Quick Hotbar Mod version " + QuickHotbarModInfo.VERSION, width / 2 - 160, height / 2 - 40, -1);

        if (SettingsClient.handleInventorySwitchInServer)
        {
            fontRenderer.drawString("Server has mod installed.", width / 2 - 160, height / 2 - 25, -1);
        }
        else
        {
            fontRenderer.drawString("Server does not have mod installed.", width / 2 - 160, height / 2 - 25, -1);
        }
    }
}