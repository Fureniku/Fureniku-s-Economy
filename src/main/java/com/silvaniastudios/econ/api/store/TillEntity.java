package com.silvaniastudios.econ.api.store;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TillEntity extends TileEntity {
	
	BlockPos managerPos;
	
	String ownerUuid;
	String ownerName;
	
	public TillEntity() {}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("ownerUuid", ownerUuid);
		nbt.setString("ownerName", ownerName);
		
		nbt.setInteger("manPosX", managerPos.getX());
		nbt.setInteger("manPosY", managerPos.getX());
		nbt.setInteger("manPosZ", managerPos.getX());
		
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
