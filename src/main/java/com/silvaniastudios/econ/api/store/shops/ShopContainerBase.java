package com.silvaniastudios.econ.api.store.shops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ShopContainerBase extends Container {
	
	public ShopBaseEntity tileEntity;

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return false;
	}
}
