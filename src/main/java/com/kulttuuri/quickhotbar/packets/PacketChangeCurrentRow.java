package com.kulttuuri.quickhotbar.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketChangeCurrentRow implements IMessage
{
	public boolean directionUp = false;
	
	public PacketChangeCurrentRow()
	{
	}
	
	public PacketChangeCurrentRow(boolean directionUp)
	{
		this.directionUp = directionUp;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.directionUp = buf.getBoolean(0);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(directionUp);
	}

	
	public static class PacketChangeCurrentRowHandler implements IMessageHandler<PacketChangeCurrentRow, IMessage>
	{
		private static final int ITEMS_IN_ROW = 9;
		
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
		
		@Override
		public IMessage onMessage(PacketChangeCurrentRow message, MessageContext ctx)
		{
			if (ctx.side == ctx.side.SERVER)
			{
				try
				{
					InventoryPlayer playerInventory = ctx.getServerHandler().playerEntity.inventory;
					
					ItemStack[][] items = new ItemStack[4][9];
					items[0] = getItemsInRow(0, playerInventory);
					items[1] = getItemsInRow(1, playerInventory);
					items[2] = getItemsInRow(2, playerInventory);
					items[3] = getItemsInRow(3, playerInventory);
					
					if (message.directionUp)
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
			
			return null;
		}
		
	}
}
