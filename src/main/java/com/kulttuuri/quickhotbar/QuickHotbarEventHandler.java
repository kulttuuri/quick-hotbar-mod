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

package com.kulttuuri.quickhotbar;

import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import com.kulttuuri.quickhotbar.gui.TranslationHelper;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kulttuuri.quickhotbar.packets.PacketChangeCurrentRow;
import com.kulttuuri.quickhotbar.settings.SettingsClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class QuickHotbarEventHandler
{
	private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
	private static final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

	private static boolean announceWelcomeMessage = false;
	public static boolean renderQuickHotbarPreview = false;
	private static boolean isUpKeyDown = false;
	private static boolean isDownKeyDown = false;
    private static boolean isModeSwitchKeyDown = false;
    private static boolean isNumberKeyDown = false;

    private static final int ITEMS_IN_ROW = 9;

    private int whichNumberKeyIsDown()
    {
        if (!QuickHotbarMod.clientSettings.ENABLE_NUMBER_SCROLLING) return 0;

        if (org.lwjgl.input.Keyboard.isKeyDown(2)) return 1;
        else if (org.lwjgl.input.Keyboard.isKeyDown(3)) return 2;
        else if (org.lwjgl.input.Keyboard.isKeyDown(4)) return 3;
        else if (org.lwjgl.input.Keyboard.isKeyDown(5)) return 4;
        else if (org.lwjgl.input.Keyboard.isKeyDown(6)) return 5;
        else if (org.lwjgl.input.Keyboard.isKeyDown(7)) return 6;
        else if (org.lwjgl.input.Keyboard.isKeyDown(8)) return 7;
        else if (org.lwjgl.input.Keyboard.isKeyDown(9)) return 8;
        else if (org.lwjgl.input.Keyboard.isKeyDown(10)) return 9;
        else return 0;
    }

	@SubscribeEvent
	public void clientJoinedEvent(ClientConnectedToServerEvent event)
	{
		announceWelcomeMessage = true;
	}

    @SubscribeEvent
    public void disconnectEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        QuickHotbarMod.clientSettings.handleInventorySwitchInServer = false;
    }
	
	private void announceModWelcomeMessage()
	{
		SettingsClient settings = QuickHotbarMod.clientSettings;
		if (settings.ANNOUNCE_MOD_LOADED)
		{
			String keyNameScrolling = settings.SCROLLING_KEY.getEventNameShort().equals("LCONTROL") ? "left ctrl" : settings.SCROLLING_KEY.getEventNameShort().toLowerCase();
			String keyNameUp = settings.SCROLLING_KEY_UP.getEventNameShort().toLowerCase();
			String keyNameDown = settings.SCROLLING_KEY_DOWN.getEventNameShort().toLowerCase();
            String keyNameOpenmenu = settings.KEY_OPEN_MOD_SETTINGS_MENU.getEventNameShort().toLowerCase();
            String keyNameSwitchMode = settings.SCROLLING_KEY_SWITCH_MODE.getEventNameShort().toLowerCase();

			String orText = settings.ALLOW_SCROLLING_WITH_KEYBOARD ? " ("+ TranslationHelper.translateString("quickhotbarmod.chat.or")+" " + keyNameUp.toLowerCase() + " & " + keyNameDown.toLowerCase() + ")" : "";
            //String switchModeText = settings.ALLOW_MODE_SWITCHING ? keyNameScrolling + " + " + keyNameSwitchMode + " to switch mode. " : "";
			String numberSwitchText = settings.ENABLE_NUMBER_SCROLLING ? TranslationHelper.translateString("quickhotbarmod.chat.numberkeyandscroll") + " " : "";
            String openMenuText = settings.ENABLE_SETTING_MENU ? keyNameScrolling + " + " + keyNameOpenmenu + " " + TranslationHelper.translateString("quickhotbarmod.chat.toviewmodsettings") : "";

            String msg = "Quick Hotbar " + QuickHotbarModInfo.VERSION + " "+TranslationHelper.translateString("quickhotbarmod.chat.loaded")+" ";
			msg = msg + keyNameScrolling + " + " + TranslationHelper.translateString("quickhotbarmod.chat.mousewheel") + orText + " "+TranslationHelper.translateString("quickhotbarmod.chat.toscroll")+". ";
            msg = msg + numberSwitchText;
            msg = msg + openMenuText;
			Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentTranslation(msg, new Object[0]));
		}
	}
	
	/**
	 * To render chat messages above the inventory slots preview.
	 * @param event
	 */
    @SubscribeEvent
    public void renderChatMessagesAboveInventorySlotsPreview(RenderGameOverlayEvent.Chat event)
    {
        if (Minecraft.getMinecraft().currentScreen != null) return;

        if ((whichNumberKeyIsDown() != 0 || QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown()) && renderQuickHotbarPreview)
        {
        	event.setPosY(60);
        }
    }
	
    @SubscribeEvent
	public void handleKeyboardPresses(RenderGameOverlayEvent.Pre event)
	{
        if (Minecraft.getMinecraft().currentScreen != null) return;

        if (!QuickHotbarMod.clientSettings.SCROLLING_KEY_SWITCH_MODE.isKeyDown()) isModeSwitchKeyDown = false;
        if (!QuickHotbarMod.clientSettings.SCROLLING_KEY_UP.isKeyDown()) isUpKeyDown = false;
        if (!QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN.isKeyDown()) isDownKeyDown = false;
        if (whichNumberKeyIsDown() == 0) isNumberKeyDown = false;

        if (!isModeSwitchKeyDown &&
        	QuickHotbarMod.clientSettings.SCROLLING_KEY_SWITCH_MODE.isKeyDown() &&
        	QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() &&
            QuickHotbarMod.clientSettings.ALLOW_MODE_SWITCHING)
        {
            isModeSwitchKeyDown = true;
            QuickHotbarMod.clientSettings.setCurrentSwitchMode(!QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW);

            String msg = "";
            if (QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW) msg = TranslationHelper.translateString("quickhotbarmod.chat.rowscrolling");
            else msg = TranslationHelper.translateString("quickhotbarmod.chat.columnscrolling");
            Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentTranslation(msg, new Object[0]));
        }

        if (QuickHotbarMod.clientSettings.ENABLE_SETTING_MENU
            && QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown()
            && QuickHotbarMod.clientSettings.KEY_OPEN_MOD_SETTINGS_MENU.isKeyDown())
        {
            Minecraft.getMinecraft().displayGuiScreen(GuiSettingsBase.currentGuiScreen);
        }

		if (QuickHotbarMod.clientSettings.IMMEDIATELY_SHOW_POPUP_MENU && QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown()) renderQuickHotbarPreview = true;
		
		if (QuickHotbarMod.clientSettings.ALLOW_SCROLLING_WITH_KEYBOARD)
		{
            // Number key down with arrow up key
            if (!isUpKeyDown && whichNumberKeyIsDown() != 0 && QuickHotbarMod.clientSettings.SCROLLING_KEY_UP.isKeyDown())
            {
                isNumberKeyDown = true;
                isUpKeyDown = true;
                renderQuickHotbarPreview = true;
                try
                {
                    switchItemRows(true, true, false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            // Number key down with arrow down key
            if (!isDownKeyDown && whichNumberKeyIsDown() != 0 && QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN.isKeyDown())
            {
                isNumberKeyDown = true;
                isDownKeyDown = true;
                renderQuickHotbarPreview = true;
                try
                {
                    switchItemRows(false, true, false);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

			if (!isUpKeyDown && QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() && QuickHotbarMod.clientSettings.SCROLLING_KEY_UP.isKeyDown())
			{
				isUpKeyDown = true;
				try
				{
					//Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(1);
					switchItemRows(true, true, QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if (!isDownKeyDown && QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() && QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN.isKeyDown())
			{
				isDownKeyDown = true;
				try
				{
					//Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(-1);
					switchItemRows(false, true, QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
    
    @SubscribeEvent
    public void hideInGameGuiElementsWhenPreviewIsOpen(RenderGameOverlayEvent.Pre event)
    {
    	if (renderQuickHotbarPreview && (QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() || whichNumberKeyIsDown() != 0))
    	{
	    	if (event.getType() == event.getType().FOOD || event.getType() == event.getType().HEALTH || event.getType() == event.getType().EXPERIENCE || event.getType() == event.getType().ARMOR)
	    	{
	    		event.setCanceled(true);
	    	}
    	}
    }
    
	@SubscribeEvent
	public void handleGameUpdate(TickEvent.RenderTickEvent event)
	{
		if (announceWelcomeMessage && Minecraft.getMinecraft() != null && Minecraft.getMinecraft().player != null)
		{
			announceWelcomeMessage = false;
			announceModWelcomeMessage();
		}
		
		if (renderQuickHotbarPreview && (QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() || whichNumberKeyIsDown() != 0))
		{
			if (Minecraft.getMinecraft().ingameGUI == null || !Minecraft.getMinecraft().inGameHasFocus) return;
			Minecraft mc = Minecraft.getMinecraft();
			mc.gameSettings.heldItemTooltips = false; // Disable the selected item name rendering in the screen while showing preview of itemslots
			
			ScaledResolution res = new ScaledResolution(mc);
	        int width = res.getScaledWidth();
	        int height = res.getScaledHeight();
			renderHotbar(mc.ingameGUI, 3, 43, width, height, event.renderTickTime);
			renderHotbar(mc.ingameGUI, 2, 63, width, height, event.renderTickTime);
			renderHotbar(mc.ingameGUI, 1, 83, width, height, event.renderTickTime);
		}
		else if ((!QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() && !isNumberKeyDown) && renderQuickHotbarPreview)
		{
			renderQuickHotbarPreview = false;
			// Enable back rendering of item name user changed slot into
			Minecraft.getMinecraft().gameSettings.heldItemTooltips = true;
		}
	}
	
    /**
     * Renders the hotbar. Code copied from GuiIngameForge.
     * @param gui
     * @param width
     * @param height
     * @param partialTicks
     * @since 1.7.2
     */
    private void renderHotbar(GuiIngame gui, int itemRowNumber, int yPosFromBottom, int width, int height, float partialTicks)
    {
    	Minecraft mc = Minecraft.getMinecraft();
        mc.mcProfiler.startSection("actionBar");

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        mc.renderEngine.bindTexture(WIDGETS);

        InventoryPlayer inv = mc.player.inventory;
        GL11.glPushMatrix();
        	GL11.glTranslatef(1f, 1f, 100f);
        	gui.drawTexturedModalRect(width / 2 - 92, height - yPosFromBottom, 0, 0, 182, 22);
        	//gui.drawTexturedModalRect(width / 2 - 91 - 1 + inv.currentItem * 20, height - yPosFromBottom - 1, 0, 22, 24, 22); Renders the selected slot, we don't want that.
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        
        for (int i = 0; i < 9; ++i)
        {
            int x = width / 2 - 90 + i * 20 + 2;
            int z = height - yPosFromBottom;
            int renderSlot = itemRowNumber * 9 + i;
            renderInventorySlot(renderSlot, x, z + 3, partialTicks);
        }

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        mc.mcProfiler.endSection();
    }
    
    /**
     * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick.
     * Code copied from GuiInGame class.
     * @since 1.7.2
     */
    private void renderInventorySlot(int par1, int par2, int par3, float par4)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	
        ItemStack itemstack = mc.player.inventory.mainInventory.get(par1);

        if (itemstack != null)
        {
            float f1 = (float)itemstack.getAnimationsToGo() - par4;
            
            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float)(par2 + 8), (float)(par3 + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(par2 + 8)), (float)(-(par3 + 12)), 0.0F);
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            //itemRenderer.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.getTextureManager(), itemstack, par2, par3+1);
            itemRenderer.renderItemAndEffectIntoGUI(itemstack, par2, par3+1);

            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
            }

            //itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.getTextureManager(), itemstack, par2, par3+1);
            itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, itemstack, par2, par3+1, null);
        }
    }
	
	@SubscribeEvent
	public void handleMouseScroll(MouseEvent event)
	{
	    int dWheel = event.getDwheel();
	    if (QuickHotbarMod.clientSettings.SCROLLING_KEY.isKeyDown() || whichNumberKeyIsDown() != 0)
	    {
		    if (dWheel < 0)
		    {
		        try
		        {
		        	event.setCanceled(true);
		        	//Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(1);
		        	switchItemRows(false, false, whichNumberKeyIsDown() != 0 ? false : QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW);
		        }
		        catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
		    }
		    else if (dWheel > 0)
		    {
		        try
		        {
		        	event.setCanceled(true);
		        	//Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(-1);
		        	switchItemRows(true, false, whichNumberKeyIsDown() != 0 ? false : QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW);
		        }
		        catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
		    }
	    }
	}

    private void switchItemRows(boolean directionUp, boolean isScrollingWithKeyboard, boolean changeRow) throws Exception
    {    	
        boolean reverseScrolling = QuickHotbarMod.clientSettings.REVERSE_MOUSEWHEEL_SCROLLING;
        // Direction can only be reversed with mouse scrolling
        if (!isScrollingWithKeyboard)
        {
            directionUp = reverseScrolling ? !directionUp : directionUp;
        }

        //boolean changeRow = QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW;
        renderQuickHotbarPreview = true;

        // If server had the mod installed, we let server handle the row / column switching
        if (QuickHotbarMod.clientSettings.handleInventorySwitchInServer)
        {
            handleRowSwitchInServer(directionUp, changeRow);
            //System.out.println("Handling row switch in server.");
            return;
        }

        //System.out.println("Handling row switch in client.");
        if (!directionUp)
        {
            if (changeRow)
            {
                switchItemRows(3, 2, directionUp);
                switchItemRows(2, 1, directionUp);
                switchItemRows(1, 0, directionUp);
            }
            else
            {
                int currentSlot = getCurrentSlot(directionUp, reverseScrolling, isScrollingWithKeyboard);
                switchItemsInSlots(3 * ITEMS_IN_ROW + currentSlot, 2 * ITEMS_IN_ROW + currentSlot, directionUp);
                switchItemsInSlots(2 * ITEMS_IN_ROW + currentSlot, 1 * ITEMS_IN_ROW + currentSlot, directionUp);
                switchItemsInSlots(1 * ITEMS_IN_ROW + currentSlot, 0 * ITEMS_IN_ROW + currentSlot, directionUp);
            }
        }
        else
        {
            if (changeRow)
            {
                switchItemRows(0, 1, directionUp);
                switchItemRows(1, 2, directionUp);
                switchItemRows(2, 3, directionUp);
            }
            else
            {
                int currentSlot = getCurrentSlot(directionUp, reverseScrolling, isScrollingWithKeyboard);
                switchItemsInSlots(0 * ITEMS_IN_ROW + currentSlot, 1 * ITEMS_IN_ROW + currentSlot, directionUp);
                switchItemsInSlots(1 * ITEMS_IN_ROW + currentSlot, 2 * ITEMS_IN_ROW + currentSlot, directionUp);
                switchItemsInSlots(2 * ITEMS_IN_ROW + currentSlot, 3 * ITEMS_IN_ROW + currentSlot, directionUp);
            }
        }
    }

    private int getCurrentSlot(boolean directionUp, boolean reverseScrolling, boolean isScrollingWithKeyboard)
    {
        if ((!reverseScrolling && !directionUp) || (reverseScrolling && directionUp))
        {
            int currentSlot = Minecraft.getMinecraft().player.inventory.currentItem;
            //if (!isScrollingWithKeyboard) currentSlot = currentSlot + 1; // I don't know why this was here.
            if (currentSlot >= 9) currentSlot = 0;
            return currentSlot;
        }
        else
        {
            int currentSlot = Minecraft.getMinecraft().player.inventory.currentItem;
            //if (!isScrollingWithKeyboard) currentSlot = currentSlot - 1; // I don't know why this was here.
            if (currentSlot < 0) currentSlot = 8;
            return currentSlot;

        }
    }

    private void handleRowSwitchInServer(boolean directionUp, boolean changeRow)
    {
        QuickHotbarMod.instance.proxy.simpleNetworkWrapper.sendToServer(new PacketChangeCurrentRow(directionUp, changeRow));
    }

    private void switchItemRows(int row1, int row2, boolean directionUp)
    {
        for (int i = 0; i < ITEMS_IN_ROW; i++)
        {
            switchItemsInSlots(row1 * ITEMS_IN_ROW + i, row2 * ITEMS_IN_ROW + i, directionUp);
        }
    }

    /**
     * Switches place of two items in player inventory. Calls the playercontroller's windowClick method to
     * do it on client.
     * @param slot1 slot 1.
     * @param slot2 slot 2.
     * @param directionUp are we going up or down the inventory.
     */
    private void switchItemsInSlots(int slot1, int slot2, boolean directionUp)
    {
        int inventoryId = 0; /** The id of the window which was clicked. 0 for player inventory. */
        int rightClick = 0; /** 1 when right-clicking and otherwise 0 */
        int holdingShift = 0; /** Is player holding shift key */

        // We add 9 because first row is crafting stuff (yeah, frigging inventorycontainer ordering...)
        slot1 = slot1 + 9;
        slot2 = slot2 + 9;

        ClickType clickType = ClickType.SWAP;
        
        // Note: We prevent Minecraft 1.9 bug where slot 26 does not swap items correctly if that slot is empty and going up by doing this trickery.
        Minecraft.getMinecraft().playerController.windowClick(inventoryId, !directionUp ? slot1 : slot2, rightClick, clickType, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(inventoryId, !directionUp ? slot2 : slot1, rightClick, clickType, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(inventoryId, !directionUp ? slot1 : slot2, rightClick, clickType, Minecraft.getMinecraft().player);
        
        //Minecraft.getMinecraft().playerController.windowClick(inventoryId, slot1, rightClick, holdingShift, Minecraft.getMinecraft().thePlayer);
        //Minecraft.getMinecraft().playerController.windowClick(inventoryId, slot2, rightClick, holdingShift, Minecraft.getMinecraft().thePlayer);
        //Minecraft.getMinecraft().playerController.windowClick(inventoryId, slot1, rightClick, holdingShift, Minecraft.getMinecraft().thePlayer);
    }
}