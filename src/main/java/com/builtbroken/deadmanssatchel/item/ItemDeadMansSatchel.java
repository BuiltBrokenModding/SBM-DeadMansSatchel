package com.builtbroken.deadmanssatchel.item;

import javax.annotation.Nullable;

import com.builtbroken.deadmanssatchel.SatchelCapabilityProvider;
import com.builtbroken.deadmanssatchel.SatchelMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemDeadMansSatchel extends Item {
	
	public ItemDeadMansSatchel() {
		this.setMaxStackSize(1);
		this.setRegistryName("deadmansstachel");
		this.setUnlocalizedName("deadmanssatchel");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		
		
        player.openGui(SatchelMod.mod, 0, world, hand.ordinal(), 0, 0);
		return super.onItemRightClick(world, player, hand);
	}
	
	
	
	@Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new SatchelCapabilityProvider(stack);
    }
	
	
	
}
