package com.fureniku.econ.store.shops;

import javax.annotation.Nonnull;

import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class DirectBuyShopBase extends ShopBaseEntity {

	public DirectBuyShopBase(int shopSize) {
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
	
	/**
	 * Performs a direct transaction (usually a sale).
	 * Checks the buyer has enough money and that the item is in-stock, and then takes their money and gives them the item.
	 * This doesn't use the shopping cart system.
	 * @param int The ID of the slot we're transacting
	 * @param EntityPlayer the player making the purchase
	 * @param ItemStackHandler the available items in this shop interface
	 * @return
	 */
	public boolean performDirectTransaction(int slotId, EntityPlayer buyer, boolean stockedInternally) {
		if (slotId <= shopSize) {
			int price = priceList[slotId];
			if (getManager() != null) {
				StoreManagerEntity e = getManager();
				if (utils.getBalance(buyer) >= price) {
					ItemStack item = inventory.getStackInSlot(slotId);
					ItemStack found = findStockInStockChests(e, item, slotId);
					if (found != ItemStack.EMPTY) { 
						if (utils.chargePlayerAnywhere(buyer, price)) {
							buyer.addItemStackToInventory(found);
							e.payStoreManager(price);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
}
