package com.silvaniastudios.econ.api.store.shops;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.StoreEntityBase;
import com.silvaniastudios.econ.api.store.management.StockChestEntity;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ShopBaseEntity extends StoreEntityBase {
	
	int shopSize = 0;
	public int[] priceList;
	public BlockPos managerPos;
	public boolean[] match_meta;
	public boolean[] match_nbt;
	
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
		
		nbt.setInteger("manPosX", managerPos.getX());
		nbt.setInteger("manPosY", managerPos.getX());
		nbt.setInteger("manPosZ", managerPos.getX());
		
		for (int i = 0; i < shopSize; i++) {
			nbt.setBoolean("match_meta_" + i, match_meta[i]);
			nbt.setBoolean("match_nbt_" + i, match_nbt[i]);
		}
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
		
		int x = nbt.getInteger("manPosX");
		int y = nbt.getInteger("manPosY");
		int z = nbt.getInteger("manPosZ");
		managerPos = new BlockPos(x, y, z);
		
		for (int i = 0; i < shopSize; i++) {
			match_meta[i] = nbt.getBoolean("match_meta_" + i);
			match_nbt[i]  = nbt.getBoolean("match_nbt_" + i);
		}
		
		return nbt;
	}
	
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
		managerPos = pos;
	}
	
	public boolean hasManager() {
		return getManager() != null;
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
