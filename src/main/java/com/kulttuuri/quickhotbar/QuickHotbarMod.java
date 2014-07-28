package com.kulttuuri.quickhotbar;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.proxy.IProxy;
import com.kulttuuri.quickhotbar.settings.SettingsClient;
import com.kulttuuri.quickhotbar.settings.SettingsServer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = QuickHotbarModInfo.MODID, version = QuickHotbarModInfo.VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels={"invpacket"}, packetHandler = QuickHotbarMod.class)
public class QuickHotbarMod implements IPacketHandler
{
	private static final int ITEMS_IN_ROW = 9;

    @Instance(QuickHotbarModInfo.MODID)
    public static QuickHotbarMod instance;
    
    public static SettingsClient clientSettings = new SettingsClient();
    
    @SidedProxy(clientSide = "com.kulttuuri.quickhotbar.proxy.clientProxy", serverSide = "com.kulttuuri.quickhotbar.proxy.serverProxy")
    public static IProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	if (event.getSide() == Side.CLIENT)
    	{
    		clientSettings.loadSettingsFromFile(event.getSuggestedConfigurationFile());
            if (!clientSettings.MODE_SWITCHING_DEFAULT_ROW) QuickHotbarEventHandler.currentSwitchMode = QuickHotbarEventHandler.ENUM_CURRENT_SWITCH_MODE_COLUMN;
        }
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerEvents();
    }

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (packet.channel.equals("invpacket"))
		{
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			boolean goingUp = false;
			try {
				goingUp = inputStream.readBoolean();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try
			{
				InventoryPlayer playerInventory = ((EntityPlayerMP)player).inventory;
				//System.out.println("HANDLING PACKET!");
				ItemStack[][] items = new ItemStack[4][9];
				items[0] = getItemsInRow(0, playerInventory);
				items[1] = getItemsInRow(1, playerInventory);
				items[2] = getItemsInRow(2, playerInventory);
				items[3] = getItemsInRow(3, playerInventory);
				
				if (goingUp)
				{
					setItemsForRow(0, items[1], playerInventory);
					setItemsForRow(1, items[2], playerInventory);
					setItemsForRow(2, items[3], playerInventory);
					setItemsForRow(3, items[0], playerInventory);
				}
				else
				{
					setItemsForRow(0, items[3], playerInventory);
					setItemsForRow(1, items[0], playerInventory);
					setItemsForRow(2, items[1], playerInventory);
					setItemsForRow(3, items[2], playerInventory);
				}
				
				playerInventory.inventoryChanged = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private ItemStack[] getItemsInRow(int row, InventoryPlayer inventory) throws Exception
	{
		ItemStack[] items = new ItemStack[9];
    	for (int i = 0; i < 9; i++)
    	{
    		int stack = i + (row * ITEMS_IN_ROW);
    		//System.out.println("GETTING STACK: " + stack);
    		items[i] = inventory.getStackInSlot(i + (row * ITEMS_IN_ROW));
    		//System.out.println(items[i]);
    	}
    	return items;
	}
	
	private void setItemsForRow(int row, ItemStack[] items, InventoryPlayer inventory) throws Exception
	{
    	for (int i = 0; i < 9; i++)
    	{
    		inventory.setInventorySlotContents(i + (row * ITEMS_IN_ROW), items[i]);
    	}
	}
}