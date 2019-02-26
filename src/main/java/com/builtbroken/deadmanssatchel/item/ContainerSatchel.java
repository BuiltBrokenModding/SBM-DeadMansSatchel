package com.builtbroken.deadmanssatchel.item;

import com.builtbroken.deadmanssatchel.util.SlotNoBag;
import com.builtbroken.deadmanssatchel.util.SlotSatchel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerSatchel extends Container {

	private ItemStack stack;
	private int slotCount = 6;

	public ContainerSatchel(IInventory playerInventory, ItemStack stack, int slotCount) {
		this.stack = stack;
		this.slotCount = slotCount;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory) {
		// Slots for the main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 69;
				this.addSlotToContainer(new SlotNoBag(playerInventory, col + row * 9 + 10, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 69;
			this.addSlotToContainer(new SlotNoBag(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots() {
		IItemHandler itemHandler = this.stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for(int i = 0; i < itemHandler.getSlots(); i++) {
        	int yCoord = i/9 * 18; // 9 slots fit per row, 18 is size of the slot texture
        	int xCoord = i%9 * 18; // 0, 1*18, 2*18, 3*18, loop per row
        	this.addSlotToContainer(new SlotSatchel(itemHandler, i, 8 + xCoord, 6 + yCoord, stack));
        }
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < slotCount) {
				if (!this.mergeItemStack(itemstack1, slotCount, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}