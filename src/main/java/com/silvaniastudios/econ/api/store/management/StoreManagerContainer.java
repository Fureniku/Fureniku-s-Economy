package com.silvaniastudios.econ.api.store.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class StoreManagerContainer extends Container {
	
	StoreManagerEntity tileEntity;
	
	public StoreManagerContainer(StoreManagerEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.canInteractWith(playerIn);
	}

}
