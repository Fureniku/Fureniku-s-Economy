package com.fureniku.econ.store;

import javax.annotation.Nullable;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class StoreEntityBase extends TileEntity {
	
	public BlockPos managerPos = new BlockPos(0,0,0);
	
	public String ownerUuid = "";
	public String ownerName = "";
	
	EconUtils utils = new EconUtils();
	
	public StoreEntityBase() {}
	
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
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("ownerUuid", ownerUuid);
		nbt.setString("ownerName", ownerName);
		
		nbt.setInteger("manPosX", managerPos.getX());
		nbt.setInteger("manPosY", managerPos.getY());
		nbt.setInteger("manPosZ", managerPos.getZ());
		
		
		return writeNBT(nbt);
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
		
		
		readNBT(nbt);
	}

	
	/**
	 * Get the ID of this shop, as set by the manager. Used for players to identify specific shop interfaces.
	 * @param StoreManagerEntity the store manager
	 * @return the shop's ID
	 */
	protected int getId(StoreManagerEntity manager) {
		return manager.shopPosArray.indexOf(getPos());
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
	
	public boolean hasManager() {
		return getManager() != null;
	}
	
	/**
	 * Set the manager of this block
	 * @param BlockPos the position of the manager block
	 */
	public void setManager(BlockPos pos) {
		managerPos = pos;
		sendUpdates();
	}
	
	public boolean isPlayerOwner(EntityPlayer player) {
		return ownerUuid.equalsIgnoreCase(player.getCachedUniqueIdString());
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid();
    }
	
	public boolean isLoaded() {
		return this.hasWorld() && this.hasPosition() ? this.getWorld().isBlockLoaded(this.getPos()) : false;
	}
	
	public boolean hasPosition() {
		return this.pos != null && this.pos != BlockPos.ORIGIN;
	}
	
	public void sendUpdates() {
		System.out.println("Sending data to client..");
		this.markDirty();
		
		if (this.isLoaded()) {
			final IBlockState state = world.getBlockState(pos);
			this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
			this.getWorld().scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		}
	}
	
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		System.out.println("Client data retrieved!");
		System.out.println("To prove it, the owner is " + ownerName);
		handleUpdateTag(pkt.getNbtCompound());
	}

}