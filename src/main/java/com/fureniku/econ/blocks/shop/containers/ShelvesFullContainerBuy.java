package com.fureniku.econ.blocks.shop.containers;

import com.fureniku.econ.store.SlotDisplay;
import com.fureniku.econ.store.shops.ShopBaseEntity;
import com.fureniku.econ.store.shops.ShopContainerBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ShelvesFullContainerBuy extends ShopContainerBase {
	
	public ShelvesFullContainerBuy(InventoryPlayer invPlayer, ShopBaseEntity tileEntity) {
		this.tileEntity = tileEntity;
		
		IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		addSlotToContainer(new SlotDisplay(itemHandler, 0, 9, 10));
		addSlotToContainer(new SlotDisplay(itemHandler, 1, 9, 34));
		addSlotToContainer(new SlotDisplay(itemHandler, 2, 9, 58));
		addSlotToContainer(new SlotDisplay(itemHandler, 3, 9, 82));
		
		addPlayerSlots(invPlayer);
	}

	private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = i * 18 + 105;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
            }
        }

        for (int i = 0; i < 9; i++) {
            int x = 8 + i * 18;
            int y = 163;
            this.addSlotToContainer(new Slot(playerInventory, i, x, y));
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.canInteractWith(playerIn);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		return ItemStack.EMPTY;
	}
}
