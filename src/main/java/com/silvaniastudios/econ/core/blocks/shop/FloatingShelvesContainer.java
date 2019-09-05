package com.silvaniastudios.econ.core.blocks.shop;

import com.silvaniastudios.econ.api.store.shops.CartShopBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FloatingShelvesContainer extends Container {
	
	CartShopBase entity;
	
	public FloatingShelvesContainer(InventoryPlayer invPlayer, CartShopBase tileEntity) {
		this.entity = tileEntity;
		
		IItemHandler itemHandler = this.entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 74, 8));
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 21, 36));
		addSlotToContainer(new SlotItemHandler(itemHandler, 2, 74, 30));
		addSlotToContainer(new SlotItemHandler(itemHandler, 3, 74, 52));
		
		addPlayerSlots(invPlayer);
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = i * 18 + 124;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
            }
        }

        for (int i = 0; i < 9; i++) {
            int x = 8 + i * 18;
            int y = 58 + 124;
            this.addSlotToContainer(new Slot(playerInventory, i, x, y));
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return entity.canInteractWith(playerIn);
	}
}
