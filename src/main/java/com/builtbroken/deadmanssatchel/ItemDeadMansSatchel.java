package com.builtbroken.deadmanssatchel;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemDeadMansSatchel extends Item {

	public ItemDeadMansSatchel(String registryName) {
		this.setMaxStackSize(1);
		this.setRegistryName(registryName);
		this.setTranslationKey("satchels." + registryName);
		this.setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound compound = stack.getTagCompound();
		UUID playerUUID = player.getUUID(player.getGameProfile());
		if(compound.hasKey("Owner") && SatchelMod.getConfig(world, this).onlyOwner) {
			UUID storedUUID = UUID.fromString(compound.getString("Owner"));
			if(!playerUUID.equals(storedUUID)) {
				return super.onItemRightClick(world, player, hand);
			}
		} else {
			compound.setString("Owner", playerUUID.toString());
		}

		player.openGui(SatchelMod.mod, 0, world, hand.ordinal(), 0, 0);
		return super.onItemRightClick(world, player, hand);
	}



	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new SatchelCapabilityProvider(SatchelMod.getSlotCount(this));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.hasTagCompound()) {
			tooltip.add(stack.getTagCompound().getString("Owner"));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}





}
