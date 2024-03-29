package com.fureniku.econ.store.shops;

import javax.annotation.Nonnull;

import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class StandaloneShopBase extends ShopBaseEntity {
	
	long shopBalance = 0;
	int storageSize;
	boolean sellSingle = false;


	public StandaloneShopBase(int shopSize) {
		super(shopSize);
		inventory = new ItemStackHandler(shopSize) {
			
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return true;
			}
			
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
			}
		};
	}
	
	public void readNBT(NBTTagCompound nbt) {
		shopBalance = nbt.getLong("shopBalance");
		sellSingle = nbt.getBoolean("sellSingle");
		super.readNBT(nbt);
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setLong("shopBalance", shopBalance);
		nbt.setBoolean("sellSingle", sellSingle);
		return super.writeNBT(nbt);
	}
	
	/**
	 * Performs an instant transaction (usually a sale).
	 * Checks the buyer has enough money and that the item is in-stock, and then takes their money and gives them the item.
	 * This doesn't use the shopping cart system.
	 * @param int The ID of the slot we're transacting
	 * @param EntityPlayer the player making the purchase
	 * @param ItemStackHandler the available items in this shop interface
	 * @return
	 */
	public boolean performInstantTransaction(int slotId, EntityPlayer buyer, boolean stockedInternally) {
		if (slotId <= shopSize) {
			int price = priceList[slotId];
			
			if (utils.getBalance(buyer) >= price) {
				if (storageSize > 0) {
					if (!takeStockFromInternal(inventory.getStackInSlot(slotId), slotId)) {
						return false;
					} else {
						if (utils.chargePlayerAnywhere(buyer, price)) {
							buyer.addItemStackToInventory(inventory.getStackInSlot(slotId));
							payShop(price);
						}
					}	
				} else {
					if (utils.chargePlayerAnywhere(buyer, price)) {
						ItemStack sellStack = inventory.getStackInSlot(slotId);
						if (sellSingle) {
							buyer.addItemStackToInventory(sellStack);
							sellStack.setCount(sellStack.getCount()-1);
							inventory.setStackInSlot(slotId, sellStack);
						} else {
							buyer.addItemStackToInventory(inventory.getStackInSlot(slotId));
							inventory.setStackInSlot(slotId, ItemStack.EMPTY);
						}
						payShop(price);
					}
				}
				
				return true;
			}
		}
		return false;
	}
	
	public void payShop(long amount) {
		if (hasManager()) {
			StoreManagerEntity e = getManager();
			e.payStoreManager(amount);
			//If there's money saved in the shop, but now there's a manager, move existing money to shop.
			if (shopBalance > 0) {
				e.payStoreManager(shopBalance);
				shopBalance = 0;
			}
		} else {
			shopBalance += amount;
		}
	}
	
	public boolean takeStockFromInternal(ItemStack stack, int slot) {
		for (int i = 0; i < storageSize; i++) {
			ItemStack slotItem = inventory.getStackInSlot(shopSize+i);
			
			if (slotItem.getItem() == stack.getItem()) {
				if (slotItem.getCount() >= stack.getCount()) {
					if ((slotItem.getItemDamage() == stack.getItemDamage())) {
						if ((slotItem.getTagCompound() == stack.getTagCompound()) || !match_nbt[slot]) {
						
							slotItem.setCount(slotItem.getCount() - stack.getCount());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
