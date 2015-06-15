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

package com.kulttuuri.quickhotbar.packets;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This packet is being sent from client to server to change inventory rows.
 * NOTE: Only being used if serverside assistance is enabled.
 */
public class PacketChangeCurrentRow implements IMessage
{
	public boolean directionUp = false;
    public boolean changeRow = true;
	
	public PacketChangeCurrentRow()
	{
	}
	
	public PacketChangeCurrentRow(boolean directionUp, boolean changeRow)
	{
		this.directionUp = directionUp;
        this.changeRow = changeRow;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.directionUp = buf.getBoolean(0);
        this.changeRow = buf.getBoolean(1);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBoolean(directionUp);
        buf.writeBoolean(changeRow);
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

        private void setItemForRowSlot(int row, int slot, ItemStack item, InventoryPlayer inventory) throws Exception
        {
            inventory.setInventorySlotContents(row * ITEMS_IN_ROW + slot, item);
        }
		
		@Override
		public IMessage onMessage(PacketChangeCurrentRow message, MessageContext ctx)
		{
			if (ctx.side == ctx.side.SERVER)
			{
				try
				{
					InventoryPlayer playerInventory = ctx.getServerHandler().playerEntity.inventory;
                    int currentItem = ctx.getServerHandler().playerEntity.inventory.currentItem;
					
					ItemStack[][] items = new ItemStack[4][9];
					items[1] = getItemsInRow(1, playerInventory);
					items[2] = getItemsInRow(2, playerInventory);
					items[3] = getItemsInRow(3, playerInventory);
                    items[0] = getItemsInRow(0, playerInventory); // Hotbar, lowest one because of that
					
					if (message.directionUp)
					{
                        if (message.changeRow)
                        {
                            setItemsForRow(1, items[2], playerInventory);
                            setItemsForRow(2, items[3], playerInventory);
                            setItemsForRow(3, items[0], playerInventory);
                            setItemsForRow(0, items[1], playerInventory);
                        }
                        else
                        {
                            setItemForRowSlot(1, currentItem, items[2][currentItem], playerInventory);
                            setItemForRowSlot(2, currentItem, items[3][currentItem], playerInventory);
                            setItemForRowSlot(3, currentItem, items[0][currentItem], playerInventory);
                            setItemForRowSlot(0, currentItem, items[1][currentItem], playerInventory);
                        }
					}
					else
					{
                        if (message.changeRow)
                        {
                            setItemsForRow(1, items[0], playerInventory);
                            setItemsForRow(2, items[1], playerInventory);
                            setItemsForRow(3, items[2], playerInventory);
                            setItemsForRow(0, items[3], playerInventory);
                        }
                        else
                        {
                            setItemForRowSlot(1, currentItem, items[0][currentItem], playerInventory);
                            setItemForRowSlot(2, currentItem, items[1][currentItem], playerInventory);
                            setItemForRowSlot(3, currentItem, items[2][currentItem], playerInventory);
                            setItemForRowSlot(0, currentItem, items[3][currentItem], playerInventory);
                        }
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
