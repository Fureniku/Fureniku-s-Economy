package com.silvaniastudios.econ.api.capability.cart;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class CartCapability implements ICart {
	
	public String shop_name;
	public BlockPos shop_manager;
	
	public ItemStackHandler inventory = new ItemStackHandler(getSlots()) {
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			sync();
		}
	};
	
	public String getShopName() {
		return shop_name;
	}
	
	public BlockPos getShopManager() {
		return shop_manager;
	}
	
	public void setShopName(String name) {
		shop_name = name;
	}

	public void setShopManager(BlockPos pos) {
		shop_manager = pos;
	}
	
	@Override
	public int getSlots() {
		return 27;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack addItem(int slot, ItemStack stack, boolean simulate) {
		return inventory.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack removeItem(int slot, int amount, boolean simulate) {
		return inventory.extractItem(slot, amount, simulate);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		inventory.setStackInSlot(slot, stack);
	}

	@Override
	public void sync() {
		// TODO Auto-generated method stub
		
	}
}
