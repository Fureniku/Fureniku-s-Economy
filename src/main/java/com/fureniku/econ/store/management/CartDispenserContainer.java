package com.fureniku.econ.store.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CartDispenserContainer extends Container {
	
	public CartDispenserEntity tileEntity;
	
	public CartDispenserContainer(CartDispenserEntity tileEntity, int viewId) {
		this.tileEntity = tileEntity;
		
		IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int slotId = 0 + (viewId * 27);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new SlotItemHandler(itemHandler, slotId, 8+(j*18), 32+(i*18)));
				slotId++;
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.canInteractWith(playerIn);
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (tileEntity.isPlayerOwner(player)) {
			return super.slotClick(slotId, dragType, clickTypeIn, player);
		}
		return ItemStack.EMPTY;
	}
}
