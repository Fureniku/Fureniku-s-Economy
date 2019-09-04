package com.silvaniastudios.econ.api.store.management;

import java.util.ArrayList;
import java.util.UUID;

import com.silvaniastudios.econ.api.capability.cart.CartProvider;
import com.silvaniastudios.econ.api.capability.cart.ICart;
import com.silvaniastudios.econ.core.EconConfig;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class StoreManagerEntity extends TileEntity implements ITickable {
	
	public ArrayList<Integer> registeredShopIds = new ArrayList<>();
	public ArrayList<BlockPos> shopPosArray = new ArrayList<>();
	
	public ArrayList<Integer> registeredStockIds = new ArrayList<>();
	public ArrayList<BlockPos> stockPosArray = new ArrayList<>();
	
	public ArrayList<Integer> registeredTillIds = new ArrayList<>();
	public ArrayList<BlockPos> tillPosArray = new ArrayList<>();
	
	public ArrayList<Integer> registeredCartDispenserIds = new ArrayList<>();
	public ArrayList<BlockPos> cartDispenserPosArray = new ArrayList<>();
	
	public String ownerName = "";
	public String ownerUuid = "";
	public String shopName = "";
	
	public ArrayList<String> activeCustomerUuids = new ArrayList<>();
	public ArrayList<Integer> activeCustomerTimers = new ArrayList<>();
	public ArrayList<String> bannedCustomerUuids = new ArrayList<>();
	
	public long balance = 0;
	
	public boolean closed;
	
	public StoreManagerEntity() {}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		registeredShopIds.clear();
		shopPosArray.clear();
		registeredStockIds.clear();
		stockPosArray.clear();
		registeredTillIds.clear();
		tillPosArray.clear();
		registeredCartDispenserIds.clear();
		cartDispenserPosArray.clear();
		
		ownerName = nbt.getString("ownerName");
		ownerUuid = nbt.getString("ownerUuid");
		shopName = nbt.getString("shopName");
		
		balance = nbt.getLong("balance");
		
		closed = nbt.getBoolean("closed");
		
		NBTTagList shopPosList = nbt.getTagList("shops", Constants.NBT.TAG_COMPOUND);
		NBTTagList stock = nbt.getTagList("stock", Constants.NBT.TAG_COMPOUND);
		NBTTagList tillPosList = nbt.getTagList("tills", Constants.NBT.TAG_COMPOUND);
		NBTTagList cartDispenserPosList = nbt.getTagList("cartDispenser", Constants.NBT.TAG_COMPOUND);
		
		int[] shopIds = nbt.getIntArray("shopIds");
		int[] stockIds = nbt.getIntArray("stockIds");
		int[] tillIds = nbt.getIntArray("tillIds");
		int[] cartDispenserIds = nbt.getIntArray("cartDispenserIds");
		
		for (int i = 0; i < shopIds.length; i++) {
			NBTTagCompound pos = shopPosList.getCompoundTagAt(i);
			registeredShopIds.add(shopIds[i]);
			shopPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < stockIds.length; i++) {
			NBTTagCompound pos = stock.getCompoundTagAt(i);
			registeredStockIds.add(stockIds[i]);
			stockPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < tillIds.length; i++) {
			NBTTagCompound pos = tillPosList.getCompoundTagAt(i);
			registeredTillIds.add(tillIds[i]);
			tillPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < cartDispenserIds.length; i++) {
			NBTTagCompound pos = cartDispenserPosList.getCompoundTagAt(i);
			registeredCartDispenserIds.add(cartDispenserIds[i]);
			cartDispenserPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		System.out.println("Start write NBT");
		nbt.setString("ownerName", ownerName);
		nbt.setString("ownerUuid", ownerUuid);
		nbt.setString("shopName", shopName);
		
		nbt.setLong("balance", balance);
		
		nbt.setBoolean("closed", closed);
		
		NBTTagList shopList = new NBTTagList();
		NBTTagList stockList = new NBTTagList();
		NBTTagList tillList = new NBTTagList();
		NBTTagList cartDispenserList = new NBTTagList();
		
		for (int i = 0; i < shopPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", shopPosArray.get(i).getX());
			com.setInteger("y", shopPosArray.get(i).getY());
			com.setInteger("z", shopPosArray.get(i).getZ());
			shopList.appendTag(com);
		}
		
		for (int i = 0; i < stockPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", stockPosArray.get(i).getX());
			com.setInteger("y", stockPosArray.get(i).getY());
			com.setInteger("z", stockPosArray.get(i).getZ());
			stockList.appendTag(com);
		}
		
		for (int i = 0; i < tillPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", tillPosArray.get(i).getX());
			com.setInteger("y", tillPosArray.get(i).getY());
			com.setInteger("z", tillPosArray.get(i).getZ());
			tillList.appendTag(com);
		}
		
		for (int i = 0; i < cartDispenserPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", cartDispenserPosArray.get(i).getX());
			com.setInteger("y", cartDispenserPosArray.get(i).getY());
			com.setInteger("z", cartDispenserPosArray.get(i).getZ());
			cartDispenserList.appendTag(com);
		}
		
		nbt.setTag("shops", shopList);
		nbt.setTag("stock", stockList);
		nbt.setTag("tills", tillList);
		nbt.setTag("cartDispenser", cartDispenserList);
		System.out.println("End write NBT");
		return nbt;
	}
	
	/**
	 * Returns the name of the owner of this block. If the owner is online, and has changed their Minecraft name since the block was placed,
	 * this will also update their name in the block.
	 * @param world
	 * @return The owner name
	 */
	public String getOwnerName(World world) {
		EntityPlayer ownerPlayer = world.getPlayerEntityByUUID(UUID.fromString(ownerUuid));
		if (ownerPlayer != null) {
			String name = ownerPlayer.getDisplayName().getFormattedText();
			if (name != ownerName) {
				FurenikusEconomy.log(0, ownerName + " has changed their Minecraft name to " + name + ". Updating...");
				ownerName = name;
			}
		}
		return ownerName;
	}
	
	public void addShopToManager(BlockPos pos) {
		shopPosArray.add(pos);
	}
	
	public void addStockToManager(BlockPos pos) {
		stockPosArray.add(pos);
	}
	
	public void addTillToManager(BlockPos pos) {
		tillPosArray.add(pos);
	}
	
	public void addCartDispenserToManager(BlockPos pos) {
		cartDispenserPosArray.add(pos);
	}
	
	public void closeShop() {
		closed = true;
	}
	
	public void openShop() {
		closed = false;
	}
	
	public boolean isShopOpen() {
		return closed;
	}
	
	public void banCustomer(String uuid) {
		bannedCustomerUuids.add(uuid);
		
		UUID u = UUID.fromString(uuid);
		EntityPlayer customer = world.getPlayerEntityByUUID(u);
		if (customer != null) {
			customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.shop_ban", shopName)));
		}
	}
	
	public boolean unbanCustomer(String uuid) {
		return bannedCustomerUuids.remove(uuid);
	}
	
	public void addActiveCustomer(String uuid) {
		activeCustomerUuids.add(uuid);
		activeCustomerTimers.add(0);
	}
	
	/**
	 * Removes a customer and closes their cart.
	 * @param String UUID the players UUID
	 * @param Integer TYPE the cause of this closure:
	 * 0 = timeout
	 * 1 = forced by shop owner
	 * 2 = customer death
	 * 3 = customer distance
	 * 4 = customer logging out
	 * @return
	 */
	public boolean removeActiveCustomer(String uuid, int type) {
		int id = activeCustomerUuids.indexOf(uuid);
		if (id >= 0) {
			activeCustomerUuids.remove(id);
			activeCustomerTimers.remove(id);
			
			closeCart(uuid);
			
			UUID u = UUID.fromString(uuid);
			EntityPlayer customer = world.getPlayerEntityByUUID(u);
			if (customer != null) {
				if (type == 1) {
					customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closed_forced", shopName)));
				}
				if (type == 2) {
					customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closed_death", shopName)));
				}
				if (type == 3) {
					customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closed_distance", shopName)));
				}
				if (type == 4) {
					customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closed_logout", shopName)));
				}
				if (type == 5) {
					customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closed_another_store", shopName)));
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void update() {
		if (activeCustomerUuids.size() > 0) {
			if (activeCustomerUuids.size() == activeCustomerTimers.size()) {
				for (int i = 0; i < activeCustomerUuids.size(); i++) {
					int t = activeCustomerTimers.get(i);
					activeCustomerTimers.set(i, t+1);
					String uuid = activeCustomerUuids.get(i);
					UUID u = UUID.fromString(uuid);
					EntityPlayer customer = world.getPlayerEntityByUUID(u);
					
					if (customer == null) {
						closeCart(uuid);
						removeActiveCustomer(uuid, 0);
					}
					
					if (t+1 >= (1200*EconConfig.cartTimeout)-600) {
						customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closing_inactive")));
					}
					
					if (t+1 >= (1200*EconConfig.cartTimeout)) {
						closeCart(uuid);
						removeActiveCustomer(uuid, 0);
						customer.sendMessage(new TextComponentString(I18n.format("econ.shop.customer.cart_closed_inactive")));
					}
					
					if (customer.isDead) {
						closeCart(uuid);
						removeActiveCustomer(uuid, 2);
					}
					
					if (pos.distanceSq(customer.posX, customer.posY, customer.posZ) > EconConfig.maxDistance) {
						closeCart(uuid);
						removeActiveCustomer(uuid, 3);
					}
				}
			} else {
				//Something went horribly wrong!
				for (int i = 0; i < activeCustomerUuids.size(); i++) {
					closeCart(activeCustomerUuids.get(i));
				}
				activeCustomerUuids.clear();
				activeCustomerTimers.clear();
			}
		}
	}
	
	public void createCart(EntityPlayer player) {
		ICart cart = player.getCapability(CartProvider.CART, null);
		if (cart.getShopManager() != null) {
			//The player has a cart for another shop already. Lets close it.
			TileEntity te = world.getTileEntity(cart.getShopManager());
			if (te != null && te instanceof StoreManagerEntity) {
				StoreManagerEntity sm = (StoreManagerEntity) te;
				sm.removeActiveCustomer(player.getCachedUniqueIdString(), 5);
			}
		}
		addActiveCustomer(player.getCachedUniqueIdString());
		cart.setShopName(shopName);
		cart.setShopManager(pos);
	}
	
	public void closeCart(String uuid) {
		UUID u = UUID.fromString(uuid);
		EntityPlayer customer = world.getPlayerEntityByUUID(u);
		ICart cart = customer.getCapability(CartProvider.CART, null);
		
		for (int i = 0; i < cart.getSlots(); i++) {
			ItemStack stack = cart.getStackInSlot(i);
			if (stack != ItemStack.EMPTY) {
				moveCartToStockChest(stack);
				cart.setStackInSlot(i, ItemStack.EMPTY);
			}
		}
		
		cart.setShopName(null);
		cart.setShopManager(null);
	}
	
	public void moveCartToStockChest(ItemStack stack) {
		for (int i = 0; i < stockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(stockPosArray.get(i));
			
			if (te != null && te instanceof StockChestEntity) {
				StockChestEntity e = (StockChestEntity) te;
				
				if (e.isBackToStock) {
					for (int j = 0; j < e.inventory.getSlots(); j++) {
						stack = e.inventory.insertItem(j, stack, false);
						//TODO test this.
						if (stack == ItemStack.EMPTY) {
							return;
						}
					}
				}
			}
		}
		
		//No results by this point? Go through again, but check all chests instead of just BTS.
		for (int i = 0; i < stockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(stockPosArray.get(i));
			
			if (te != null && te instanceof StockChestEntity) {
				StockChestEntity e = (StockChestEntity) te;
				for (int j = 0; j < e.inventory.getSlots(); j++) {
					stack = e.inventory.insertItem(j, stack, false);
					//TODO test this.
					if (stack == ItemStack.EMPTY) {
						return;
					}
				}
			}
		}
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (EntityPlayer.getUUID(playerIn.getGameProfile()).toString().equals(ownerUuid)) {
			return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
		}
		return false;
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
