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
	private Object handler = null;
	
	public SatchelCapabilityProvider(ItemStack stack) {
		this.stack = stack;
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !stack.isEmpty();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (hasCapability(capability, facing)) {
        	if(handler == null) {
        		handler = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackHandler(6));
        	}
            return (T) handler;
        }

        return null;
    }

	@Override
	public NBTTagCompound serializeNBT() {
		return ((ItemStackHandler) handler).serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("items")) {
            ((ItemStackHandler) handler).deserializeNBT((NBTTagCompound) nbt.getTag("items"));
        }
	}

}
