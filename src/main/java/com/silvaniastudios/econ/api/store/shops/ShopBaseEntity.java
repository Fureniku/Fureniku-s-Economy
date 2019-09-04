package com.silvaniastudios.econ.api.store.shops;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.StoreEntityBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ShopBaseEntity extends StoreEntityBase {
	
	int shopSize = 0;
	
	public int[] priceList;
	
	EconUtils utils = new EconUtils();
	
	public ShopBaseEntity(int shopSize) {
		this.shopSize = shopSize;
	}
	
	public ItemStackHandler inventory = new ItemStackHandler(shopSize) {
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			markDirty();
		}
	};

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
		return nbt;
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
	public boolean performDirectTransaction(int slotId, EntityPlayer buyer, ItemStackHandler inv) {
		if (slotId <= inv.getSlots()) {
			int price = priceList[slotId];
			
			if (utils.getBalance(buyer) >= price) {
				//TODO add stock check, and remove stock when giving to player
				buyer.addItemStackToInventory(inv.getStackInSlot(slotId));
				utils.chargePlayerAnywhere(buyer, price);
				//TODO place money in the till
				return true;
			}
		}
		return false;
	}
	
	public boolean addItemToCart(int slotId, EntityPlayer buyer, ItemStackHandler inv) {
		return false;
	}
}
