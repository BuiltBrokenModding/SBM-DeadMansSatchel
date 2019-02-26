package com.builtbroken.deadmanssatchel.util;

import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNoBag extends Slot {

	public SlotNoBag(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack.getItem() instanceof ItemDeadMansSatchel) {
			return false;
		}
		return super.isItemValid(stack);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		if(this.getStack().getItem() instanceof ItemDeadMansSatchel) {
			return false;
		}
		return super.canTakeStack(playerIn);
	}
	
	

}
