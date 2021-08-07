package com.fureniku.econ.store.management;

import javax.annotation.Nonnull;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.store.StoreEntityBase;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class StockChestEntity extends StoreEntityBase {
	
	public String stockChestName = "Stock Chest";
	int chestSize = EconConstants.Inventories.STOCK_CHEST_SIZE;
	
	public StockChestEntity(int size) {
		this.chestSize = size;
	}
	
	public StockChestEntity() {}
	
	public ItemStackHandler inventory = new ItemStackHandler(chestSize) {
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			
			Item type = getInventoryType();
			System.out.println("Type trying to go in: " + stack.getDisplayName());
			System.out.println("Comparing it to: " + new ItemStack(type).getDisplayName());
			if (type == ItemStack.EMPTY.getItem()) { return true; }
			return type == stack.getItem();
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			markDirty();
		}
		
		@Override
	    @Nonnull
	    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	    {
			Item type = getInventoryType();
			if (type != ItemStack.EMPTY.getItem()) {
				if (type != stack.getItem()) {
					return stack;
				}
			}
	        if (stack.isEmpty())
	            return ItemStack.EMPTY;

	        validateSlotIndex(slot);

	        ItemStack existing = this.stacks.get(slot);

	        int limit = getStackLimit(slot, stack);

	        if (!existing.isEmpty())
	        {
	            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
	                return stack;

	            limit -= existing.getCount();
	        }

	        if (limit <= 0)
	            return stack;

	        boolean reachedLimit = stack.getCount() > limit;

	        if (!simulate)
	        {
	            if (existing.isEmpty())
	            {
	                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
	            }
	            else
	            {
	                existing.grow(reachedLimit ? limit : stack.getCount());
	            }
	            onContentsChanged(slot);
	        }

	        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
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
	
	public Item getInventoryType() {
		for (int i = 0; i < chestSize; i++) {
			if (inventory.getStackInSlot(i) != ItemStack.EMPTY) {
				System.out.println("Setting inventory type to " + inventory.getStackInSlot(i).getDisplayName());
				return inventory.getStackInSlot(i).getItem();
			}
		}
		return ItemStack.EMPTY.getItem();
	}
	
	public ItemStack getInventoryTypeItemStack() {
		for (int i = 0; i < chestSize; i++) {
			if (inventory.getStackInSlot(i) != ItemStack.EMPTY) {
				return inventory.getStackInSlot(i);
			}
		}
		return ItemStack.EMPTY;
	}
	
	public void validateInventory() {
		ItemStack base = ItemStack.EMPTY;
		for (int i = 0; i < chestSize; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != ItemStack.EMPTY) {
				if (base == ItemStack.EMPTY) {
					base = stack;
				} else if (base.getItem() != stack.getItem()) {
					FurenikusEconomy.log(3, "Ejecting invalid item " + stack.getDisplayName() + "from stock chest at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack));
					inventory.setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}
	
	public int getTotalItemNumber() {
		int count = 0;
		//Start at 1 to ignore the Display slot
		for (int i = 1; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i) != ItemStack.EMPTY) {
				count += inventory.getStackInSlot(i).getCount();
			}
		}
		return count;
	}
	
	public void readNBT(NBTTagCompound nbt) {
		stockChestName = nbt.getString("stockChestName");
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setString("stockChestName", stockChestName);
		nbt.setTag("items", inventory.serializeNBT());
		return nbt;
	}

}
