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

	private ItemStackHandler handler = null;

	public SatchelCapabilityProvider() {
		this.handler = new ItemStackHandler(ContainerSatchel.SIZE);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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
