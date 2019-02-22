package com.builtbroken.deadmanssatchel;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SatchelCapabilityProvider implements ICapabilitySerializable {

	private ItemStack stack;
	
	public SatchelCapabilityProvider(ItemStack stack) {
		this.stack = stack;
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !stack.isEmpty();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound compound = stack.getTagCompound();
		if (compound.hasKey("items")) {
            ((ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if (hasCapability(capability, facing)) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackHandler(6));
        }

        return null;
    }

}
