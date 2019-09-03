package com.silvaniastudios.econ.api.capability.cart;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface ICart {
	
	String getShopName();
	void setShopName(String name);
	BlockPos getShopManager();
	void setShopManager(BlockPos pos);
	int getSlots();
    ItemStack getStackInSlot(int slot);
    ItemStack addItem(int slot, ItemStack stack, boolean simulate);
    ItemStack removeItem(int slot, int amount, boolean simulate);
    void setStackInSlot(int slot, ItemStack stack);
    void sync();

}
