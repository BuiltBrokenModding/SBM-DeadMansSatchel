package com.builtbroken.deadmanssatchel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotNoBagItemHandler extends SlotItemHandler {
	
	public SlotNoBagItemHandler(IItemHandler handler, int index, int xPosition, int yPosition) {
		super(handler, index, xPosition, yPosition);
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
