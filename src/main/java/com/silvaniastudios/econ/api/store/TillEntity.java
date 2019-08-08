package com.silvaniastudios.econ.api.store;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TillEntity extends TileEntity implements ITickable {
	
	BlockPos managerPos;
	
	String ownerUuid;
	String ownerName;
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
	
	@Override
	public void update() {
		if (buyerCountdownTime > 0) {
			buyerCountdownTime--;
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("ownerUuid", ownerUuid);
		nbt.setString("ownerName", ownerName);
		
		nbt.setInteger("manPosX", managerPos.getX());
		nbt.setInteger("manPosY", managerPos.getX());
		nbt.setInteger("manPosZ", managerPos.getX());
		
		nbt.setTag("items", inventory.serializeNBT());
		
		nbt.setString("buyerUuid", buyerUuid);
		nbt.setString("buyerName", buyerName);
		nbt.setInteger("buyerCountdownTime", buyerCountdownTime);
		
		nbt.setIntArray("moneyInventory", moneyInventory);
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		ownerUuid = nbt.getString("ownerUuid");
		ownerName = nbt.getString("ownerName");
		
		int x = nbt.getInteger("manPosX");
		int y = nbt.getInteger("manPosY");
		int z = nbt.getInteger("manPosZ");
		managerPos = new BlockPos(x, y, z);
		
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		
		buyerUuid = nbt.getString("buyerUuid");
		buyerName = nbt.getString("buyerName");
		buyerCountdownTime = nbt.getInteger("buyerCountdownTime");
		
		moneyInventory = nbt.getIntArray("moneyInventory");
	}
	
	protected void register(StoreManagerEntity manager) {
		if (!manager.tillPosArray.contains(getPos())) {
			manager.tillPosArray.add(getPos());
		}
	}
	
	/**
	 * Get the ID of this shop, as set by the manager. Used for players to identify specific shop interfaces.
	 * @param StoreManagerEntity the store manager
	 * @return the shop's ID
	 */
	protected int getId(StoreManagerEntity manager) {
		return manager.tillPosArray.indexOf(getPos());
	}
	
	/**
	 * Get the manager of this block, if one has been set.
	 * @param BlockPos the position of this block
	 * @return The StoreManagerEntity if set, and null if not.
	 */
	public StoreManagerEntity getManager(BlockPos pos) {
		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof StoreManagerEntity) {
			return (StoreManagerEntity) world.getTileEntity(pos);
		}
		return null;
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
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
			final IBlockState state = world.getBlockState(pos);
			this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, this.getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
		this.getWorld().notifyBlockUpdate(this.pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
}
