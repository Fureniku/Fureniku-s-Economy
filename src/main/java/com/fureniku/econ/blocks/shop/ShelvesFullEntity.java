package com.fureniku.econ.blocks.shop;

import javax.annotation.Nonnull;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.store.shops.ShopBaseEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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
			nbt.setBoolean("match_nbt_" + i, match_nbt[i]);
		}*/
		
		return super.writeNBT(nbt);
	}
}
