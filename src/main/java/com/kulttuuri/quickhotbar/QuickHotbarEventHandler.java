package com.kulttuuri.quickhotbar;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;

import com.kulttuuri.quickhotbar.gui.GuiSettingsBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class QuickHotbarEventHandler implements ITickHandler
{
	private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
	private static final RenderItem itemRenderer = new RenderItem();

	private static boolean isUpKeyDown = false;
	private static boolean isDownKeyDown = false;
    private static boolean isModeSwitchKeyDown = false;
	
	/** Should we render other item slots in the ingame gui. */
	public static boolean renderQuickHotbarPreview = false;

    private static final int ITEMS_IN_ROW = 9;

	@ForgeSubscribe
	public void handleKeyboardPresses(RenderGameOverlayEvent.Pre event)
	{
        if (Minecraft.getMinecraft().currentScreen != null) return;

        if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_SWITCH_MODE)) isModeSwitchKeyDown = false;
        if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_UP)) isUpKeyDown = false;
        if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN)) isDownKeyDown = false;

        if (!isModeSwitchKeyDown &&
                Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_SWITCH_MODE) &&
                Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) &&
                QuickHotbarMod.clientSettings.ALLOW_MODE_SWITCHING)
        {
            isModeSwitchKeyDown = true;
            QuickHotbarMod.clientSettings.setCurrentSwitchMode(!QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW);

            String msg = "";
            if (QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW) msg = "Switched to row scrolling.";
            else msg = "Switched to column scrolling.";
            Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
        }

        if (QuickHotbarMod.clientSettings.ENABLE_SETTING_MENU
            && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY)
            && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.KEY_OPEN_MOD_SETTINGS_MENU))
        {
            Minecraft.getMinecraft().displayGuiScreen(GuiSettingsBase.currentGuiScreen);
        }

		if (QuickHotbarMod.clientSettings.IMMEDIATELY_SHOW_POPUP_MENU && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY)) renderQuickHotbarPreview = true;

		if (QuickHotbarMod.clientSettings.ALLOW_SCROLLING_WITH_KEYBOARD)
		{
			if (!isUpKeyDown && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_UP))
			{
				isUpKeyDown = true;
				try
				{
					//Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(1);
					switchItemRows(true, true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if (!isDownKeyDown && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN))
			{
				isDownKeyDown = true;
				try
				{
					//Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(-1);
					switchItemRows(false, true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * To render chat messages above the inventory slots preview.
	 * @param event
	 */
	@ForgeSubscribe
    public void renderChatMessagesAboveInventorySlotsPreview(RenderGameOverlayEvent.Chat event)
    {
        if (Minecraft.getMinecraft().currentScreen != null) return;

        if (Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && renderQuickHotbarPreview)
        {
            event.posY -= 60;
        }
    }
	
	@ForgeSubscribe
    public void hideInGameGuiElementsWhenPreviewIsOpen(RenderGameOverlayEvent.Pre event)
    {
        if (Minecraft.getMinecraft().currentScreen != null) return;

    	if (renderQuickHotbarPreview && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
    	{
	    	if (event.type == event.type.FOOD || event.type == event.type.HEALTH || event.type == event.type.EXPERIENCE || event.type == event.type.ARMOR)
	    	{
	    		event.setCanceled(true);
	    	}
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
        
        InventoryPlayer inv = mc.thePlayer.inventory;
        GL11.glPushMatrix();
        	//GL11.glTranslatef(1f, 1f, 50f);
        	gui.drawTexturedModalRect(width / 2 - 91, height - yPosFromBottom, 0, 0, 182, 22);
        	//gui.drawTexturedModalRect(width / 2 - 91 - 1 + inv.currentItem * 20, height - yPosFromBottom - 1, 0, 22, 24, 22); Renders the selected slot, we don't want that.
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        
        for (int i = 0; i < 9; ++i)
        {
            int x = width / 2 - 90 + i * 20 + 2;
            int z = height - yPosFromBottom - 1;
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
    	
        ItemStack itemstack = mc.thePlayer.inventory.mainInventory[par1];

        if (itemstack != null)
        {
            float f1 = (float)itemstack.animationsToGo - par4;
            
            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float)(par2 + 8), (float)(par3 + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(par2 + 8)), (float)(-(par3 + 12)), 0.0F);
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, par2, par3+1);

            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, par2, par3+1);
        }
    }
	
    @ForgeSubscribe
	public void handleMouseScroll(MouseEvent event)
	{
	    int dWheel = event.dwheel;
	    if (Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
	    {
		    if (dWheel < 0)
		    {
		        try
		        {
		        	Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(1);
		        	switchItemRows(false, false);
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
		        	Minecraft.getMinecraft().thePlayer.inventory.changeCurrentItem(-1);
		        	switchItemRows(true, false);
		        }
		        catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
		    }
	    }
	}
	
	private void switchItemRows(boolean directionUp, boolean isScrollingWithKeyboard) throws Exception
	{
        boolean reverseScrolling = QuickHotbarMod.clientSettings.REVERSE_MOUSEWHEEL_SCROLLING;
        // Direction can only be reversed with mouse scrolling
        if (!isScrollingWithKeyboard)
        {
            directionUp = reverseScrolling ? !directionUp : directionUp;
        }

        boolean changeRow = QuickHotbarMod.clientSettings.CURRENT_SWITCH_MODE_ROW;

		renderQuickHotbarPreview = true;

        // If server had the mod installed, we let server handle the row / column switching
        if (QuickHotbarMod.clientSettings.handleInventorySwitchInServer)
        {
            handleRowSwitchInServer(directionUp, changeRow);
            return;
        }

        if (!directionUp)
        {
            if (changeRow)
            {
                switchItemRows(3, 2);
                switchItemRows(2, 1);
                switchItemRows(1, 0);
            }
            else
            {
                int currentSlot = getCurrentSlot(directionUp, reverseScrolling, isScrollingWithKeyboard);
                switchItemsInSlots(3 * ITEMS_IN_ROW + currentSlot, 2 * ITEMS_IN_ROW + currentSlot);
                switchItemsInSlots(2 * ITEMS_IN_ROW + currentSlot, 1 * ITEMS_IN_ROW + currentSlot);
                switchItemsInSlots(1 * ITEMS_IN_ROW + currentSlot, 0 * ITEMS_IN_ROW + currentSlot);
            }
        }
        else
        {
            if (changeRow)
            {
                switchItemRows(0, 1);
                switchItemRows(1, 2);
                switchItemRows(2, 3);
            }
            else
            {
                int currentSlot = getCurrentSlot(directionUp, reverseScrolling, isScrollingWithKeyboard);
                switchItemsInSlots(0 * ITEMS_IN_ROW + currentSlot, 1 * ITEMS_IN_ROW + currentSlot);
                switchItemsInSlots(1 * ITEMS_IN_ROW + currentSlot, 2 * ITEMS_IN_ROW + currentSlot);
                switchItemsInSlots(2 * ITEMS_IN_ROW + currentSlot, 3 * ITEMS_IN_ROW + currentSlot);
            }
        }
	}

    private int getCurrentSlot(boolean directionUp, boolean reverseScrolling, boolean isScrollingWithKeyboard)
    {
        if ((!reverseScrolling && !directionUp) || (reverseScrolling && directionUp))
        {
            int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
            if (!isScrollingWithKeyboard) currentSlot = currentSlot + 1;
            if (currentSlot >= 9) currentSlot = 0;
            return currentSlot;
        }
        else
        {
            int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
            if (!isScrollingWithKeyboard) currentSlot = currentSlot - 1;
            if (currentSlot < 0) currentSlot = 8;
            return currentSlot;

        }
    }

    private void handleRowSwitchInServer(boolean directionUp, boolean changeRow)
    {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try
		{
	        outputStream.writeBoolean(directionUp);
            outputStream.writeBoolean(changeRow);
		}
		catch (Exception ex)
		{
	        ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "invpacket";
		packet.data = bos.toByteArray();
		packet.length = bos.size();

		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }

    private void switchItemRows(int row1, int row2)
    {
        for (int i = 0; i < ITEMS_IN_ROW; i++)
        {
            switchItemsInSlots(row1 * ITEMS_IN_ROW + i, row2 * ITEMS_IN_ROW + i);
        }
    }

    /**
     * Switches place of two items in player inventory. Calls the playercontroller's windowClick method to
     * do it on client.
     * @param slot1 slot 1.
     * @param slot2 slot 2.
     */
    private void switchItemsInSlots(int slot1, int slot2)
    {
        int inventoryId = 0; /** The id of the window which was clicked. 0 for player inventory. */
        int rightClick = 0; /** 1 when right-clicking and otherwise 0 */
        int holdingShift = 0; /** Is player holding shift key */

        // We add 9 because first row is crafting stuff (yeah, frigging inventorycontainer ordering...)
        slot1 = slot1 + 9;
        slot2 = slot2 + 9;
        Minecraft.getMinecraft().playerController.windowClick(inventoryId, slot1, rightClick, holdingShift, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(inventoryId, slot2, rightClick, holdingShift, Minecraft.getMinecraft().thePlayer);
        Minecraft.getMinecraft().playerController.windowClick(inventoryId, slot1, rightClick, holdingShift, Minecraft.getMinecraft().thePlayer);
    }

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if ((renderQuickHotbarPreview) && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
		{
			if (Minecraft.getMinecraft().ingameGUI == null || !Minecraft.getMinecraft().inGameHasFocus) return;
			
			Minecraft mc = Minecraft.getMinecraft();
			mc.gameSettings.heldItemTooltips = false; // Disable the selected item name rendering in the screen while showing preview of itemslots
			
			ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
	        int width = res.getScaledWidth();
	        int height = res.getScaledHeight();
			renderHotbar(mc.ingameGUI, 3, 43, width, height, 1);
			renderHotbar(mc.ingameGUI, 2, 63, width, height, 1);
			renderHotbar(mc.ingameGUI, 1, 83, width, height, 1);
		}
		else if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && renderQuickHotbarPreview)
		{
			renderQuickHotbarPreview = false;
			// Enable back rendering of item name user changed slot into
			Minecraft.getMinecraft().gameSettings.heldItemTooltips = true;
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel()
	{
		return "tickhandler";
	}
}