package com.kulttuuri.quickhotbar;

import cpw.mods.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kulttuuri.quickhotbar.packets.PacketChangeCurrentRow;
import com.kulttuuri.quickhotbar.settings.SettingsClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class QuickHotbarEventHandler
{
	private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
	private static final RenderItem itemRenderer = new RenderItem();

    public static final String ENUM_CURRENT_SWITCH_MODE_ROW = "switch_mode_row";
    public static final String ENUM_CURRENT_SWITCH_MODE_COLUMN = "switch_mode_column";
    public static String currentSwitchMode = ENUM_CURRENT_SWITCH_MODE_ROW;

	private static boolean announceWelcomeMessage = false;
	private static boolean renderQuickHotbarPreview = false;
	private static boolean isUpKeyDown = false;
	private static boolean isDownKeyDown = false;
    private static boolean isModeSwitchKeyDown = false;

    private static final int ITEMS_IN_ROW = 9;

	@SubscribeEvent
	public void clientJoinedEvent(ClientConnectedToServerEvent event)
	{
		announceWelcomeMessage = true;
	}

    @SubscribeEvent
    public void disconnectEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        System.out.println("CLIENT DISCONECTED EVENT!!!!");
        QuickHotbarMod.clientSettings.handleInventorySwitchInServer = false;
    }
	
	private void announceModWelcomeMessage()
	{
		SettingsClient settings = QuickHotbarMod.clientSettings;
		if (settings.ANNOUNCE_MOD_LOADED)
		{
			String keyNameScrolling = Keyboard.getKeyName(settings.SCROLLING_KEY).equals("LCONTROL") ? "left ctrl" : Keyboard.getKeyName(settings.SCROLLING_KEY).toLowerCase();
			String keyNameUp = Keyboard.getKeyName(settings.SCROLLING_KEY_UP);
			String keyNameDown = Keyboard.getKeyName(settings.SCROLLING_KEY_DOWN);
			String orText = settings.ALLOW_SCROLLING_WITH_KEYBOARD == true ? " (or keys " + keyNameUp.toLowerCase() + " & " + keyNameDown.toLowerCase() + ")" : "";
			String msg = "Quick Hotbar " + QuickHotbarModInfo.VERSION + " loaded. ";
			msg = msg + "Hold down " + keyNameScrolling + " and use mouse wheel" + orText + " to scroll between inventory rows.";
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation(msg, new Object[0]));
		}
	}
	
	/**
	 * To render chat messages above the inventory slots preview.
	 * @param event
	 */
    @SubscribeEvent
    public void renderChatMessagesAboveInventorySlotsPreview(RenderGameOverlayEvent.Chat event)
    {
        if (Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && renderQuickHotbarPreview)
        {
            event.posY -= 60;
        }
    }
	
    @SubscribeEvent
	public void handleKeyboardPresses(RenderGameOverlayEvent.Pre event)
	{
        if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_SWITCH_MODE)) isModeSwitchKeyDown = false;
        if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_UP)) isUpKeyDown = false;
        if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN)) isDownKeyDown = false;

        if (!isModeSwitchKeyDown &&
            Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_SWITCH_MODE) &&
            Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) &&
            QuickHotbarMod.clientSettings.ALLOW_MODE_SWITCHING)
        {
            isModeSwitchKeyDown = true;
            currentSwitchMode = currentSwitchMode.equals(ENUM_CURRENT_SWITCH_MODE_ROW) ? ENUM_CURRENT_SWITCH_MODE_COLUMN : ENUM_CURRENT_SWITCH_MODE_ROW;

            String msg = "";
            if (currentSwitchMode.equals(ENUM_CURRENT_SWITCH_MODE_ROW)) msg = "Switched to row scrolling.";
            else msg = "Switched to column scrolling.";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation(msg, new Object[0]));
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
					switchItemRows(true);
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
					switchItemRows(false);
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
    	if (renderQuickHotbarPreview && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
    	{
	    	if (event.type == event.type.FOOD || event.type == event.type.HEALTH || event.type == event.type.EXPERIENCE || event.type == event.type.ARMOR)
	    	{
	    		event.setCanceled(true);
	    	}
    	}
    }
    
	@SubscribeEvent
	public void handleGameUpdate(TickEvent.RenderTickEvent event)
	{
		if (announceWelcomeMessage && Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
		{
			announceWelcomeMessage = false;
			announceModWelcomeMessage();
		}
		
		if (renderQuickHotbarPreview && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
		{
			if (Minecraft.getMinecraft().ingameGUI == null || !Minecraft.getMinecraft().inGameHasFocus) return;
			
			Minecraft mc = Minecraft.getMinecraft();
			mc.gameSettings.heldItemTooltips = false; // Disable the selected item name rendering in the screen while showing preview of itemslots
			
			ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	        int width = res.getScaledWidth();
	        int height = res.getScaledHeight();
			renderHotbar(mc.ingameGUI, 3, 43, width, height, event.renderTickTime);
			renderHotbar(mc.ingameGUI, 2, 63, width, height, event.renderTickTime);
			renderHotbar(mc.ingameGUI, 1, 83, width, height, event.renderTickTime);
		}
		else if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && renderQuickHotbarPreview)
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
        
        InventoryPlayer inv = mc.thePlayer.inventory;
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
	
	@SubscribeEvent
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
		        	switchItemRows(false);
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
		        	switchItemRows(true);
		        }
		        catch (Exception e)
		        {
		        	e.printStackTrace();
		        }
		    }
	    }
	}

    private void switchItemRows(boolean directionUp) throws Exception
    {
        directionUp = QuickHotbarMod.clientSettings.REVERSE_MOUSEWHEEL_SCROLLING ? !directionUp : directionUp;
        boolean changeRow = currentSwitchMode.equals(ENUM_CURRENT_SWITCH_MODE_ROW);

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
                switchItemRows(3, 2);
                switchItemRows(2, 1);
                switchItemRows(1, 0);
            }
            else
            {
                int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                currentSlot = currentSlot + 1;
                if (currentSlot >= 9) currentSlot = 0;
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
                int currentSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
                currentSlot = currentSlot - 1;
                if (currentSlot < 0) currentSlot = 8;
                switchItemsInSlots(0 * ITEMS_IN_ROW + currentSlot, 1 * ITEMS_IN_ROW + currentSlot);
                switchItemsInSlots(1 * ITEMS_IN_ROW + currentSlot, 2 * ITEMS_IN_ROW + currentSlot);
                switchItemsInSlots(2 * ITEMS_IN_ROW + currentSlot, 3 * ITEMS_IN_ROW + currentSlot);
            }
        }
    }

    private void handleRowSwitchInServer(boolean directionUp, boolean changeRow)
    {
        QuickHotbarMod.instance.proxy.simpleNetworkWrapper.sendToServer(new PacketChangeCurrentRow(directionUp, changeRow));
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
}