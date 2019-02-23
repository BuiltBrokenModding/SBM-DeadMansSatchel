package com.builtbroken.deadmanssatchel;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SatchelCapabilityProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	private ItemStack stack;
	private ItemStackHandler handler = null;
	
	public SatchelCapabilityProvider(ItemStack stack) {
		this.stack = stack;
		this.handler = new ItemStackHandler(6);
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
		if (nbt != null) {
            handler.deserializeNBT(nbt);
        }
	}

}
