package com.kulttuuri.quickhotbar.extended;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryPlayerCustomHotbar extends InventoryPlayer
{
	private int inventoryRowToRenderInHotbar = 3;
	
	public InventoryPlayerCustomHotbar(EntityPlayer entityPlayer)
	{
		super(entityPlayer);
	}
	
	@Override
    public ItemStack getCurrentItem()
    {
    	int currentRow = inventoryRowToRenderInHotbar * 9;
    	if (inventoryRowToRenderInHotbar == 3) currentRow = 0;
    	
    	//System.out.println("Inventory row to render: " + inventoryRowToRenderInHotbar);
    	//System.out.println("Current item index: " + this.currentItem);
    	//System.out.println("currentRow: " + (currentRow));
    	
    	int curItem = this.currentItem + currentRow;
    	if (curItem > 35) curItem = 35;
    	return this.mainInventory[curItem];
    	
        //return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
    }
	
    /**
     * Switch the current item to the next one or the previous one
     */
	@Override
    @SideOnly(Side.CLIENT)
    public void changeCurrentItem(int p_70453_1_)
    {
        if (p_70453_1_ > 0)
        {
            p_70453_1_ = 1;
        }

        if (p_70453_1_ < 0)
        {
            p_70453_1_ = -1;
        }

        for (this.currentItem -= p_70453_1_; this.currentItem < 0; this.currentItem += 9)
        {
            ;
        }

        while (this.currentItem >= 9)
        {
            this.currentItem -= 9;
        }
        
        System.out.println("SET CURRENT ITEM TO: " + currentItem);
    	int currentRow = inventoryRowToRenderInHotbar * 9;
    	if (inventoryRowToRenderInHotbar == 3) currentRow = 0;
        this.currentItem = currentItem + currentRow;
    }
    
    @SideOnly(Side.CLIENT)
    private int func_146024_c(Item p_146024_1_, int p_146024_2_)
    {
        for (int j = 0; j < this.mainInventory.length; ++j)
        {
            if (this.mainInventory[j] != null && this.mainInventory[j].getItem() == p_146024_1_ && this.mainInventory[j].getItemDamage() == p_146024_2_)
            {
                return j;
            }
        }

        return -1;
    }
    
    public int getInventoryRowToRenderInHotbar()
    {
    	return inventoryRowToRenderInHotbar;
    }
    
    public void changeCurrentInventoryRowToRenderInHotbar(boolean directionUp)
    {
    	if (directionUp && inventoryRowToRenderInHotbar >= 3)
    	{
    		inventoryRowToRenderInHotbar = 0;
    		return;
    	}
    	else if (!directionUp && inventoryRowToRenderInHotbar <= 0)
    	{
    		inventoryRowToRenderInHotbar = 3;
    		return;
    	}
    	else if (directionUp)
    	{
    		inventoryRowToRenderInHotbar = inventoryRowToRenderInHotbar + 1;
    	}
    	else if (!directionUp)
    	{
    		inventoryRowToRenderInHotbar = inventoryRowToRenderInHotbar - 1;
    	}
    }
}