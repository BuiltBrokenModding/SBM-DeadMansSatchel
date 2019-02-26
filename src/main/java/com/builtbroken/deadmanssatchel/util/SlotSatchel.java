package com.builtbroken.deadmanssatchel.util;

import java.util.ArrayList;

import com.builtbroken.deadmanssatchel.SatchelMod;
import com.builtbroken.deadmanssatchel.config.SatchelGlobalData;
import com.builtbroken.deadmanssatchel.item.ItemDeadMansSatchel;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSatchel extends SlotItemHandler {
	
	private ItemStack satchel;
	
	public SlotSatchel(IItemHandler handler, int index, int xPosition, int yPosition, ItemStack satchel) {
		super(handler, index, xPosition, yPosition);
		this.satchel = satchel;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if(stack.getItem() instanceof ItemDeadMansSatchel) {
			return false;
		}
		
		if(satchel.getItem() instanceof ItemDeadMansSatchel) {
			SatchelGlobalData data = SatchelMod.getGlobalData((ItemDeadMansSatchel) satchel.getItem());
			ArrayList<String> items = Lists.newArrayList(data.itemList);
			if(data.isBlacklist) {
				return !items.contains(stack.getItem().getRegistryName().toString());
			} else {
				return items.contains(stack.getItem().getRegistryName().toString());
			}
		} else {
			throw new RuntimeException("Invalid stack paramater! Must be a satchel! Got: " + stack.getItem().getRegistryName().toString());
		}
	}
}
