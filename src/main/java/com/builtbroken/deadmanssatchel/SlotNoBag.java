package com.builtbroken.deadmanssatchel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotNoBag extends Slot {

	public SlotNoBag(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack.getItem() == SatchelMod.satchel) {
			return false;
		}
		return super.isItemValid(stack);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		if(this.getStack().getItem() == SatchelMod.satchel) {
			return false;
		}
		return super.canTakeStack(playerIn);
	}
	
	

}
