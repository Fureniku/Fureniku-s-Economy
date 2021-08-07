package com.fureniku.econ.store;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotDisplay extends SlotItemHandler {
	
	public int slotIndex;
	public boolean stackSize;

	public SlotDisplay(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		slotIndex = index;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public void putStack(ItemStack stack) {
		return;
	}
}
