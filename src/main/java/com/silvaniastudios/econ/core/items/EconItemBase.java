package com.silvaniastudios.econ.core.items;

import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.item.Item;

public class EconItemBase extends Item {
	
	protected String name;
	
	public EconItemBase(String name, int stackSize) {
		this.name = name;
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.maxStackSize = stackSize;
	}

	public void registerItemModel() {
		FurenikusEconomy.proxy.registerItemRenderer(this, 0, name);
	}
}
