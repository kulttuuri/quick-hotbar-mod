package com.kulttuuri.quickhotbar;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HOTBAR;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kulttuuri.quickhotbar.extended.InventoryPlayerCustomHotbar;
import com.kulttuuri.quickhotbar.packets.PacketChangeCurrentRow;
import com.kulttuuri.quickhotbar.settings.SettingsClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class QuickHotbarEventHandler
{
	private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
	private static final RenderItem itemRenderer = new RenderItem();
	
	private static boolean announceWelcomeMessage = false;
	private static boolean renderQuickHotbarPreview = false;
	private static boolean isUpKeyDown = false;
	private static boolean isDownKeyDown = false;
	
	@SubscribeEvent
	public void clientJoinedEvent(ClientConnectedToServerEvent event)
	{
		announceWelcomeMessage = true;
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
		if (QuickHotbarMod.clientSettings.IMMEDIATELY_SHOW_POPUP_MENU && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY)) renderQuickHotbarPreview = true;
		
		if (QuickHotbarMod.clientSettings.ALLOW_SCROLLING_WITH_KEYBOARD)
		{
			if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_UP)) isUpKeyDown = false;
			if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY_DOWN)) isDownKeyDown = false;
			
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
    	if (event.type == event.type.HOTBAR)
    	{
    		event.setCanceled(true);
    	}
    	
    	if (renderQuickHotbarPreview && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
    	{
	    	if (event.type == event.type.FOOD || event.type == event.type.HEALTH || event.type == event.type.EXPERIENCE)
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
			System.out.println("REPLACING PLAYER INVENTORY!");
			
			EntityClientPlayerMP currentPlayer = Minecraft.getMinecraft().thePlayer;
			
			ItemStack[] oldInventoryPlayerItems = currentPlayer.inventory.mainInventory;
			ItemStack[] oldInventoryPlayerArmors = currentPlayer.inventory.armorInventory;
			int oldInventoryCurrentItem = currentPlayer.inventory.currentItem;
			
			currentPlayer.inventory = new InventoryPlayerCustomHotbar(Minecraft.getMinecraft().thePlayer);
			currentPlayer.inventory.mainInventory = oldInventoryPlayerItems;
			currentPlayer.inventory.armorInventory = oldInventoryPlayerArmors;
			currentPlayer.inventory.currentItem = oldInventoryCurrentItem;
			
			announceModWelcomeMessage();
		}
		
		if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().currentScreen == null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	        int width = res.getScaledWidth();
	        int height = res.getScaledHeight();
	        
			// We render current hotbar row ourself manually
	        int invRowToRender = ((InventoryPlayerCustomHotbar)mc.thePlayer.inventory).getInventoryRowToRenderInHotbar();
	        //System.out.println("Row to render: " + invRowToRender);
			renderHotbarNormal(mc.ingameGUI, invRowToRender == 4 ? 0 : invRowToRender, 23, width, height, event.renderTickTime);
			
			if (renderQuickHotbarPreview && Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY))
			{
				if (Minecraft.getMinecraft().ingameGUI == null || !Minecraft.getMinecraft().inGameHasFocus) return;
				
				mc.gameSettings.heldItemTooltips = false; // Disable the selected item name rendering in the screen while showing preview of itemslots
				
				renderHotbar(mc.ingameGUI, 1, 113, width, height, event.renderTickTime);
				renderHotbar(mc.ingameGUI, 2, 93, width, height, event.renderTickTime);
				renderHotbar(mc.ingameGUI, 3, 73, width, height, event.renderTickTime);
				renderHotbar(mc.ingameGUI, 0, 53, width, height, event.renderTickTime);
			}
			else if (!Keyboard.isKeyDown(QuickHotbarMod.clientSettings.SCROLLING_KEY) && renderQuickHotbarPreview)
			{
				renderQuickHotbarPreview = false;
				// Enable back rendering of item name user changed slot into
				Minecraft.getMinecraft().gameSettings.heldItemTooltips = true;
			}
		}
	}
	
    private void renderHotbarNormal(GuiIngame gui, int itemRowNumber, int yPosFromBottom, int width, int height, float partialTicks)
    {
    	Minecraft mc = Minecraft.getMinecraft();
        mc.mcProfiler.startSection("actionBar");

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(WIDGETS);
        
        InventoryPlayer inv = mc.thePlayer.inventory;
        GL11.glPushMatrix();
        	GL11.glTranslatef(1f, 1f, 100f);
        	gui.drawTexturedModalRect(width / 2 - 92, height - yPosFromBottom, 0, 0, 182, 22);
        	gui.drawTexturedModalRect(width / 2 - 91 - 1 + inv.currentItem * 20, height - yPosFromBottom - 1, 0, 22, 24, 22);
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
        int invRowToRender = ((InventoryPlayerCustomHotbar)mc.thePlayer.inventory).getInventoryRowToRenderInHotbar();
        if (invRowToRender == 4) invRowToRender = 0;
        if (invRowToRender == itemRowNumber)
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        else
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        mc.renderEngine.bindTexture(WIDGETS);
        
        InventoryPlayer inv = mc.thePlayer.inventory;
        GL11.glPushMatrix();
        	GL11.glTranslatef(1f, 1f, 100f);
        	gui.drawTexturedModalRect(width / 2 - 92, height - yPosFromBottom, 0, 0, 182, 22);
        	//gui.drawTexturedModalRect(width / 2 - 91 - 1 + inv.currentItem * 20, height - yPosFromBottom - 1, 0, 22, 24, 22); // Renders the selected slot, we don't want that.
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
		renderQuickHotbarPreview = true;
		((InventoryPlayerCustomHotbar)Minecraft.getMinecraft().thePlayer.inventory).changeCurrentInventoryRowToRenderInHotbar(!directionUp);
		//QuickHotbarMod.instance.proxy.simpleNetworkWrapper.sendToServer(new PacketChangeCurrentRow(directionUp));
	}
}