package com.silvaniastudios.econ.core.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ATMContainer extends Container {
	
	public ATMEntity tileEntity;
	
	public ATMContainer(InventoryPlayer invPlayer, ATMEntity tileEntity) {
		this.tileEntity = tileEntity;
		
		IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 310-93, 35-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 36-93,  156-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 2, 54-93,  156-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 3, 72-93,  156-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 4, 90-93,  156-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 5, 108-93, 156-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 6, 126-93, 156-45));
		addSlotToContainer(new SlotItemHandler(itemHandler, 7, 144-93, 156-45));
		
		addPlayerSlots(invPlayer);
	}

	private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 101 + j * 18;
                int y = i * 18 + 129;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
            }
        }

        for (int i = 0; i < 9; i++) {
            int x = 101 + i * 18;
            int y = 58 + 129;
            this.addSlotToContainer(new Slot(playerInventory, i, x, y));
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.canInteractWith(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(slotId);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();
			
			if (slotId < tileEntity.inventory.getSlots()) {
				if (!this.mergeItemStack(stack1, tileEntity.inventory.getSlots(), this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(stack1, 0, tileEntity.inventory.getSlots(), false)) {
				return ItemStack.EMPTY;
			}
			
			if (stack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}

}
