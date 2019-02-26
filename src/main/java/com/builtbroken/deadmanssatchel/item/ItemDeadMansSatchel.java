package com.builtbroken.deadmanssatchel.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.builtbroken.deadmanssatchel.SatchelMod;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemDeadMansSatchel extends Item {

	public final int defaultSlotCount;

	public ItemDeadMansSatchel(String registryName, int defaultSlotCount) {
		this.setMaxStackSize(1);
		this.setRegistryName(registryName);
		this.setTranslationKey("satchels." + registryName);
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.defaultSlotCount = defaultSlotCount;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!SatchelMod.canOpenBag(player, (ItemDeadMansSatchel) stack.getItem())) {
			return super.onItemRightClick(world, player, hand);
		}

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
		return new SatchelCapabilityProvider(stack, nbt, SatchelMod.getSlotCount(this));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.hasTagCompound()) {
			if(stack.getTagCompound().hasKey("Owner")) {
				String uString = stack.getTagCompound().getString("Owner");
				tooltip.add("Owner: " + uString);
				
				if(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && (!SatchelMod.getConfig(worldIn, (ItemDeadMansSatchel) stack.getItem()).onlyOwner || Minecraft.getMinecraft().player.getGameProfile().getId().equals(UUID.fromString(uString)))) {
					ItemStackHandler handler = (ItemStackHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					Map<String, Integer> items = new HashMap<String, Integer>();
					for(int i = 0; i < handler.getSlots(); i++) {
						ItemStack slotstack = handler.getStackInSlot(i);
						String itemStr = slotstack.getItem().getTranslationKey(slotstack);
						if(!itemStr.equalsIgnoreCase("tile.air")) {
							if(!items.containsKey(itemStr)) {
								items.put(itemStr, slotstack.getCount());
							} else {
								items.put(itemStr, items.get(itemStr) + slotstack.getCount());
							}
						}
					}
					
					if(items.size() > 0) {
						tooltip.add("Contains:");
					} else {
						tooltip.add("No contents");
					}
					
					for(String key : items.keySet()) {
						int count = items.get(key);
						tooltip.add(count + "x" + I18n.format(key + ".name"));
					}
				}
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}





}
