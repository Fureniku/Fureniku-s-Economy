package com.silvaniastudios.econ.api.store.shops;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.StoreEntityBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class ShopBaseEntity extends StoreEntityBase {
	
	BlockPos managerPos;
	
	String ownerUuid;
	String ownerName;
	
	int[] priceList;
	
	EconUtils utils = new EconUtils();
	
	public ShopBaseEntity() {}
	
	/**
	 * Override this method instead of readFromNBT for your inventory and custom data.
	 * Called alongside base level things that all shops will have (Owner name, connected manager)
	 * @param nbt
	 */
	public void readNBT(NBTTagCompound nbt) {}
	
	/**
	 * Override this method instead of writeToNBT for your inventory and custom data.
	 * Called alongside base level things that all shops will have (Owner name, connected manager)
	 * @param nbt
	 * @return
	 */
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
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
