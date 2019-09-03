package com.silvaniastudios.econ.core.blocks;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.EconItems;
import com.silvaniastudios.econ.core.items.ItemMoney;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ATMEntity extends TileEntity {
	
	EconUtils utils = new EconUtils();
	
	public ATMEntity() {}
	
	public ItemStackHandler inventory = new ItemStackHandler(8) {
			
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			ATMEntity.this.markDirty();
		}
	};
	
	public Container createContainer(EntityPlayer player) {
		return new ATMContainer(player.inventory, this);
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
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		nbt.setTag("items", inventory.serializeNBT());
	}
	
	public boolean withdraw(EntityPlayer player, long amount) {
		long balance = utils.getBalance(player);
		//5,000,000 is the cost of a full stack of 100 notes. Don't withdraw more than that in one go.
		if (balance >= amount && balance <= 5000000) {
		
			//Clear anything that was in there. It's on the floor now, pick it up before someone steals it!
			dropAllSlots();
			utils.chargeBalance(player, amount);
			long amountRemain = amount;
			int note10000 = 0;
			int note5000  = 0;
			int note2000  = 0;
			int note1000  = 0;
			int note500   = 0;
			int note200   = 0;
			int note100   = 0;
			
			while (amountRemain > 10000) {
				note10000++;
				amountRemain -= 10000;
			}
			while (amountRemain > 5000) {
				note5000++;
				amountRemain -= 5000;
			}
			while (amountRemain > 2000) {
				note2000++;
				amountRemain -= 2000;
			}
			while (amountRemain > 1000) {
				note1000++;
				amountRemain -= 1000;
			}
			while (amountRemain > 500) {
				note500++;
				amountRemain -= 500;
			}
			while (amountRemain > 200) {
				note200++;
				amountRemain -= 200;
			}
			while (amountRemain > 100) {
				note100++;
				amountRemain -= 100;
			}
			
			setSlotQty(0, note100);
			setSlotQty(1, note200);
			setSlotQty(2, note500);
			setSlotQty(3, note1000);
			setSlotQty(4, note2000);
			setSlotQty(5, note5000);
			setSlotQty(6, note10000);
			
			if (amountRemain > 0) {
				//Players can't withdraw coins from an ATM.
				//This should never happen thanks to the UI, but if it does, just put it back.
				utils.addMoney(player, amountRemain);
			}
		}
		return false;
	}
	
	public void dropAllSlots() {
		World world = this.world;
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			world.spawnEntity(new EntityItem(world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), item));
			inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}
	
	public boolean setSlotQty(int id, int amount) {
		ItemMoney money = null;
		
		if (id == 0) { money = EconItems.note100; }
		if (id == 1) { money = EconItems.note200; }
		if (id == 2) { money = EconItems.note500; }
		if (id == 3) { money = EconItems.note1000; }
		if (id == 4) { money = EconItems.note2000; }
		if (id == 5) { money = EconItems.note5000; }
		if (id == 6) { money = EconItems.note10000; }
		
		if (amount > money.getItemStackLimit(new ItemStack(money))) {
			return false;
		}
		
		ItemStack stack = inventory.getStackInSlot(id);
		if (stack == null) {
			inventory.setStackInSlot(id, new ItemStack(money, amount));
			return true;
		} else if (stack.getItem() == money) {
			if (stack.getCount() <= stack.getMaxStackSize() - amount) {
				stack.setCount(stack.getCount() + amount);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

	public IBlockState getState() { 
		return world.getBlockState(pos);
	}
	
	public boolean isLoaded() {
		return this.hasWorld() && this.hasPosition() ? this.getWorld().isBlockLoaded(this.getPos()) : false;
	}
	
	public boolean hasPosition() {
		return this.pos != null && this.pos != BlockPos.ORIGIN;
	}
	
	public void sendUpdates() {
		this.markDirty();
		
		if (this.isLoaded()) {
			final IBlockState state = this.getState();
			this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	public void readNBT(NBTTagCompound nbt) {
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		return nbt;
	}
	
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, this.getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readNBT(pkt.getNbtCompound());
		this.getWorld().notifyBlockUpdate(this.pos, this.getState(), this.getState(), 3);
	}
}
