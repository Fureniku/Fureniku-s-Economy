package com.fureniku.econ.store.shops;

import com.fureniku.econ.store.SlotGhostItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ShopContainerBase extends Container {
	
	public ShopBaseEntity tileEntity;

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Stop players interacting with slots if the block isn't registered, and if they aren't the owner.
	//Also handles the item cloning for ghost item slots.
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (tileEntity.hasManager() && tileEntity.ownerUuid.equalsIgnoreCase(player.getCachedUniqueIdString())) {
			if (slotId >= 0 && this.getSlot(slotId) instanceof SlotGhostItem) {
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
				
			}
			return super.slotClick(slotId, dragType, clickTypeIn, player);
		}
		return ItemStack.EMPTY;	
	}
}
