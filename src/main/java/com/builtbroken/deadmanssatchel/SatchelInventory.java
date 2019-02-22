package com.builtbroken.deadmanssatchel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class SatchelInventory extends ItemStackHandler {
	
	
	public SatchelInventory() {
		super(6);
	}

}
