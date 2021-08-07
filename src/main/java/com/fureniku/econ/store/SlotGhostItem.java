package com.fureniku.econ.store;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotGhostItem extends SlotItemHandler {
	
	public int slotIndex;
	public boolean stackSize;

	public SlotGhostItem(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean stackSize) {
		super(itemHandler, index, xPosition, yPosition);
		this.stackSize = stackSize;
		slotIndex = index;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return true;
	}

	@Override
	public void putStack(ItemStack stack) {
		if (!isItemValid(stack)) {
			return;
		}
		if (getItemHandler().getStackInSlot(this.slotIndex).isEmpty()) {
			ItemStack is = stack.copy();
			if (!stackSize) {
				is.setCount(1);
			}
			this.getItemHandler().insertItem(this.slotIndex, is, false);
			onSlotChanged();
		}
	}
}
