package com.fureniku.econ.store.management;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.store.StoreEntityBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BackToStockChestEntity extends StoreEntityBase {
	
	public String stockChestName = "Back To Stock";
	int chestSize = EconConstants.Inventories.STOCK_CHEST_SIZE-1;
	private boolean full = false;
	
	public BackToStockChestEntity() {}
	
	
	public ItemStackHandler inventory = new ItemStackHandler(chestSize) {
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
	
	public void readNBT(NBTTagCompound nbt) {
		stockChestName = nbt.getString("stockChestName");
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		getInUseSlots(); //Mark this as full if it's full. No reason to save/load the data.
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setString("stockChestName", stockChestName);
		nbt.setTag("items", inventory.serializeNBT());
		return nbt;
	}
	
	public int getInUseSlots() {
		int usedSlots = 0;
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				usedSlots++;
			}
		}
		
		full = usedSlots == inventory.getSlots();
		
		return usedSlots;
	}
	
	public boolean isFull() {
		return full;
	}
}
