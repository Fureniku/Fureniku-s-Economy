package com.silvaniastudios.econ.api.store.shops;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.StoreManagerEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class ShopBaseEntity extends TileEntity {
	
	BlockPos managerPos;
	
	String ownerUuid;
	String ownerName;
	
	int[] priceList;
	
	EconUtils utils = new EconUtils();
	
	public ShopBaseEntity() {}
	
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
		nbt.setInteger("manPosY", managerPos.getX());
		nbt.setInteger("manPosZ", managerPos.getX());
		
		nbt.setIntArray("priceList", priceList);
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
		
		priceList = nbt.getIntArray("priceList");
		readNBT(nbt);
	}
	
	/**
	 * Performs a direct transaction (usually a sale).
	 * Checks the buyer has enough money and that the item is in-stock, and then takes their money and gives them the item.
	 * This doesn't use the shopping cart system.
	 * @param int The ID of the slot we're transacting
	 * @param EntityPlayer the player making the purchase
	 * @param ItemStackHandler the available items in this shop interface
	 * @return
	 */
	public boolean performDirectTransaction(int slotId, EntityPlayer buyer, ItemStackHandler inv) {
		if (slotId <= inv.getSlots()) {
			int price = priceList[slotId];
			
			if (utils.getBalance(buyer) >= price) {
				//TODO add stock check, and remove stock when giving to player
				buyer.addItemStackToInventory(inv.getStackInSlot(slotId));
				utils.chargePlayerAnywhere(buyer, price);
				//TODO place money in the till
				return true;
			}
		}
		return false;
	}
	
	public boolean addItemToCart(int slotId, EntityPlayer buyer, ItemStackHandler inv) {
		return false;
	}
	
	/**
	 * Register the shop witth the store manager. Called when using the shop pairing wand.
	 * @param StoreManagerEntity the store manager block's tile entity.
	 */
	protected void register(StoreManagerEntity manager) {
		if (!manager.shopPosArray.contains(getPos())) {
			manager.shopPosArray.add(getPos());
		}
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
