package com.silvaniastudios.econ.api.store.management;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.store.StoreEntityBase;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TillEntity extends StoreEntityBase implements ITickable {
	
	String buyerName;
	String buyerUuid;
	int buyerCountdownTime;
	boolean locked;
	
	int[] moneyInventory = new int[FurenikusEconomy.utils.itemNotes.size()];
	
	public TillEntity() {}
	
	public ItemStackHandler inventory = new ItemStackHandler(27) {
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
	
	public void cartPurchase(EntityPlayer player) {
		if (buyerCountdownTime <= 0) {
			
		}
	}
	
	@Override
	public void update() {
		EntityPlayer buyer = world.getPlayerEntityByName(buyerName);
		if (buyerCountdownTime > 0) {
			buyerCountdownTime--;
			//Time out faster if the buyer goes offline.
			if (buyer == null) {
				buyerCountdownTime -= 5;
			}
		}
		
		if (buyerCountdownTime <= 0) {
			if (buyer != null) {
				buyer.sendMessage(new TextComponentString(I18n.format("econ.shop.till_timeout")));
			}
			
			if (buyerUuid.length() > 0) {
				buyerUuid = "";
			}
			if (buyerName.length() > 0) {
				buyerName = "";
			}
		}
	}
	
	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setTag("items", inventory.serializeNBT());
		
		nbt.setString("buyerUuid", buyerUuid);
		nbt.setString("buyerName", buyerName);
		nbt.setInteger("buyerCountdownTime", buyerCountdownTime);
		
		nbt.setIntArray("moneyInventory", moneyInventory);
		
		return nbt;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		
		buyerUuid = nbt.getString("buyerUuid");
		buyerName = nbt.getString("buyerName");
		buyerCountdownTime = nbt.getInteger("buyerCountdownTime");
		
		moneyInventory = nbt.getIntArray("moneyInventory");
	}
}
