package com.fureniku.econ.store;

import javax.annotation.Nonnull;

import com.fureniku.econ.store.management.StockChestContainer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotStockChest extends SlotItemHandler {

	public SlotStockChest(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		ItemStack displaySlot = this.getItemHandler().getStackInSlot(StockChestContainer.displaySlotId);
		
		if (stack.isItemEqual(displaySlot)) {
			return true;
		} 
	
		return false;
	}

}
