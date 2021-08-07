package com.fureniku.econ.store.management;

import com.fureniku.econ.store.SlotGhostItem;
import com.fureniku.econ.store.SlotStockChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class StockChestContainer extends Container {
	
	public StockChestEntity tileEntity;
	
	public static final int displaySlotId = 0;
	
	public StockChestContainer(InventoryPlayer invPlayer, StockChestEntity tileEntity) {
		this.tileEntity = tileEntity;
		
		IItemHandler itemHandler = this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int slotId = 1;
		addSlotToContainer(new SlotGhostItem(itemHandler, displaySlotId, 179, -5, false));
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 12; j++) {
				addSlotToContainer(new SlotStockChest(itemHandler, slotId, -19+(j*18), 17+(i*18)));
				slotId++;
			}
		}
		addPlayerSlots(invPlayer);
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = i * 18 + 129;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
            }
        }

        for (int i = 0; i < 9; i++) {
            int x = 8 + i * 18;
            int y = 58 + 129;
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
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (tileEntity.hasManager() && tileEntity.ownerUuid.equalsIgnoreCase(player.getCachedUniqueIdString())) {
			if (slotId == displaySlotId) {
				System.out.println("Clicked slot " + slotId);
				SlotGhostItem slot = (SlotGhostItem) this.getSlot(slotId);
				InventoryPlayer inventoryplayer = player.inventory;
				
				ItemStack stack = inventorySlots.get(slotId).getStack();
				if (stack != ItemStack.EMPTY) {
					slot.decrStackSize(stack.getCount());
				}
				
				ItemStack playerHeld = inventoryplayer.getItemStack();
				if (!playerHeld.isEmpty()) {
					slot.putStack(playerHeld);
				}
				
				return ItemStack.EMPTY;
				
			} else {
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			}
		}
		return ItemStack.EMPTY;	
	}
}
