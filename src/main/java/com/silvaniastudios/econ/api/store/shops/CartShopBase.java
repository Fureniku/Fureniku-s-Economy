package com.silvaniastudios.econ.api.store.shops;

import com.silvaniastudios.econ.api.capability.cart.CartProvider;
import com.silvaniastudios.econ.api.capability.cart.ICart;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class CartShopBase extends ShopBaseEntity {

	public CartShopBase(int shopSize) {
		super(shopSize);
	}
	
	/**
	 * Adds an item to the players cart if it exists.
	 * Checks the buyer has enough money and that the item is in-stock, and then takes their money and gives them the item.
	 * This doesn't use the shopping cart system.
	 * @param int The ID of the slot we're transacting
	 * @param EntityPlayer the player making the purchase
	 * @param ItemStackHandler the available items in this shop interface
	 * @return
	 */
	public boolean addToCart(int slotId, EntityPlayer buyer, boolean stockedInternally) {
		if (slotId <= shopSize) {
			if (getManager() != null) {
				StoreManagerEntity e = getManager();
				if (e.activeCustomerUuids.contains(buyer.getCachedUniqueIdString())) {
					ItemStack item = inventory.getStackInSlot(slotId);
					ItemStack found = findStockInStockChests(e, item, slotId);
					if (found != ItemStack.EMPTY) { 
						ICart cart = buyer.getCapability(CartProvider.CART, null);
						for (int i = 0; i < cart.getSlots(); i++) {
							ItemStack cartSlot = cart.getStackInSlot(i);
							if (cartSlot == ItemStack.EMPTY) {
								cart.setStackInSlot(i, found);
								return true;
							} else {
								if (cartSlot.getItem() == found.getItem()) {
									if (cartSlot.getItemDamage() == found.getItemDamage()) {
										if (cartSlot.getTagCompound() == found.getTagCompound()) {
											if (cartSlot.getCount() + found.getCount() <= cartSlot.getMaxStackSize()) {
												cartSlot.setCount(cartSlot.getCount() + found.getCount());
												return true;
											}
										}
									}
								}
							}
						}
					}
				} else {
					buyer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.no_cart")));
				}
			}
		}
		return false;
	}

}
