package com.silvaniastudios.econ.core.blocks.shop;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.capability.cart.CartProvider;
import com.silvaniastudios.econ.api.capability.cart.ICart;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.shops.ShopBaseEntity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ShelvesFullEntity extends ShopBaseEntity {

	public ShelvesFullEntity() {
		super(EconConstants.Inventories.SHELVES_FULL_SIZE);
		this.shopName = "Shelves";
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

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		
		return super.getCapability(capability, facing);
	}
	
	/**
	 * Override this method instead of readFromNBT for your inventory and custom data.
	 * Called alongside base level things that all shops will have (Owner name, connected manager)
	 * @param nbt
	 */
	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		priceList = nbt.getIntArray("priceList");
		shopName = nbt.getString("shopName");
		
		/* TODO for (int i = 0; i < shopSize; i++) {
			match_meta[i] = nbt.getBoolean("match_meta_" + i);
			match_nbt[i]  = nbt.getBoolean("match_nbt_" + i);
		}*/
		
		super.readNBT(nbt);
	}
	
	/**
	 * Override this method instead of writeToNBT for your inventory and custom data.
	 * Called alongside base level things that all shops will have (Owner name, connected manager)
	 * @param nbt
	 * @return
	 */
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setTag("items", inventory.serializeNBT());
		nbt.setIntArray("priceList", priceList);
		nbt.setString("shopName", shopName);
		
		/* TODO for (int i = 0; i < shopSize; i++) {
			nbt.setBoolean("match_meta_" + i, match_meta[i]);
			nbt.setBoolean("match_nbt_" + i, match_nbt[i]);
		}*/
		
		return super.writeNBT(nbt);
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
