package com.fureniku.econ.store;

import net.minecraft.item.ItemStack;

public class StockEntrySimple extends StockEntry {
	
	public StockEntrySimple(ItemStack stack, int id, int price, int cooldown, int linked[]) {
		super(stack, id, price, price, price, linked);
	}

	public StockEntrySimple(ItemStack stack, int id, int price, int cooldown) {
		super(stack, id, price, price, price);
	}

}
