package com.builtbroken.deadmanssatchel.item;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SatchelCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

	private ItemStackHandler handler;
	private ItemStack stack;
	private NBTTagCompound compound;
	
	public SatchelCapabilityProvider(ItemStack stack, @Nullable NBTTagCompound compound, int slotCount) {
		this.handler = new ItemStackHandler(slotCount);
		this.stack = stack;
		this.compound = compound;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !stack.isEmpty();
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (hasCapability(capability, facing)) {
			return (T) handler;
		}

		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return handler.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(nbt != null) {
			handler.deserializeNBT(nbt);
		}
	}

}
