package com.silvaniastudios.econ.api.store.shops;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.StoreEntityBase;
import com.silvaniastudios.econ.api.store.management.StockChestEntity;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class ShopBaseEntity extends StoreEntityBase {
	
	public int shopSize;
	public int[] priceList;
	
	public String shopName = "";
	
	public boolean[] match_meta;
	public boolean[] match_nbt;
	
	EconUtils utils = new EconUtils();
	
	public ShopBaseEntity(int shopSize) {
		this.shopSize = shopSize;
		
		priceList = new int[shopSize];
		match_meta = new boolean[shopSize];
		match_nbt = new boolean[shopSize];
	}
	
	public ItemStackHandler inventory = null;
	
	/**
	 * Get the manager of this block, if one has been set.
	 * @return The StoreManagerEntity if set, and null if not.
	 */
	public StoreManagerEntity getManager() {
		if (world.getTileEntity(managerPos) != null && world.getTileEntity(managerPos) instanceof StoreManagerEntity) {
			return (StoreManagerEntity) world.getTileEntity(managerPos);
		}
		return null;
	}
	
	/**
	 * Set the manager of this block
	 * @param BlockPos the position of the manager block
	 */
	public void setManager(BlockPos pos) {
		System.out.println("Shop manager has been set");
		managerPos = pos;
		sendUpdates();
	}
	
	public boolean hasManager() {
		return getManager() != null;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		priceList = nbt.getIntArray("priceList");
		super.readNBT(nbt);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		super.writeNBT(nbt);
		nbt.setIntArray("priceList", priceList);
		return nbt;
	}
	
	public ItemStack findStockInStockChests(StoreManagerEntity e, ItemStack itemToFind, int saleSlot) {
		for (int i = 0; i < e.stockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(e.stockPosArray.get(i));
			if (te != null && te instanceof StockChestEntity) {
				StockChestEntity entity = (StockChestEntity) te;
				
				for (int j = 0; j < entity.inventory.getSlots(); j++) {
					ItemStack found = entity.inventory.getStackInSlot(j);
					
					if (compareItemStacks(itemToFind, found, match_meta[saleSlot], match_nbt[saleSlot])) {
						return found.splitStack(itemToFind.getCount());
					}
				}
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	public boolean checkStockAvailability(StoreManagerEntity e, ItemStack itemToFind, int saleSlot) {
		for (int i = 0; i < e.stockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(e.stockPosArray.get(i));
			if (te != null && te instanceof StockChestEntity) {
				StockChestEntity entity = (StockChestEntity) te;
				for (int j = 0; j < entity.inventory.getSlots(); j++) {
					ItemStack found = entity.inventory.getStackInSlot(j);
					if (compareItemStacks(itemToFind, found, match_meta[saleSlot], match_nbt[saleSlot])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean compareItemStacks(ItemStack original, ItemStack found, boolean meta, boolean nbt) {
		if (found.getItem() == original.getItem()) {
			if ((found.getItemDamage() == original.getItemDamage()) || !meta) {
				if ((found.getTagCompound() == original.getTagCompound()) || !nbt) {
					if (found.getCount() >= original.getCount()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
