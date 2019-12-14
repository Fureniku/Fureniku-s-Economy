package com.silvaniastudios.econ.api.store.management;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.silvaniastudios.econ.api.capability.cart.CartProvider;
import com.silvaniastudios.econ.api.capability.cart.ICart;
import com.silvaniastudios.econ.core.EconConfig;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StoreManagerEntity extends TileEntity implements ITickable {
	
	public ArrayList<BlockPos> shopPosArray = new ArrayList<>();
	public ArrayList<BlockPos> stockPosArray = new ArrayList<>();
	public ArrayList<BlockPos> tillPosArray = new ArrayList<>();
	public ArrayList<BlockPos> cartDispenserPosArray = new ArrayList<>();
	
	public String ownerName = "";
	public String ownerUuid = "";
	public String shopName = "";
	
	public boolean show_hidden_shops_ui = false;
	public boolean show_hidden_stock_chests_ui = false;
	public boolean shop_open = true;
	public boolean shop_budget = false;
	
	public ArrayList<String> activeCustomerUuids = new ArrayList<>();
	public ArrayList<Integer> activeCustomerTimers = new ArrayList<>();
	public ArrayList<String> bannedCustomerUuids = new ArrayList<>();
	
	public ArrayList<Integer> shopInteractions = new ArrayList<>();
	public ArrayList<BlockPos> shopInteractionsLocations = new ArrayList<>();
	public ArrayList<String> shopInteractionsExtra = new ArrayList<>();
	
	public long balance = 0;
	public long budget = 0;
	
	public StoreManagerEntity() {}
	
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			if (stack.getItem() instanceof StoreStockPairer) {
				if (stack.hasTagCompound()) {
					NBTTagCompound nbt = stack.getTagCompound();
					int x = nbt.getInteger("manager_x");
					int y = nbt.getInteger("manager_y");
					int z = nbt.getInteger("manager_z");
					
					if (x == pos.getX() || y == pos.getY() || z == pos.getZ()) {
						return true;
					}
				}
			}
			
			return false;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			markDirty();
		}
	};
	
	public Container createContainer(EntityPlayer player) {
		return new StoreManagerContainer(player.inventory, this);
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
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		System.out.println("Start read NBT");
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		
		shopPosArray.clear();
		stockPosArray.clear();
		tillPosArray.clear();
		cartDispenserPosArray.clear();
		
		shopInteractions.clear();
		shopInteractionsLocations.clear();
		shopInteractionsExtra.clear();
		
		ownerName = nbt.getString("ownerName");
		ownerUuid = nbt.getString("ownerUuid");
		shopName = nbt.getString("shopName");
		
		if (shopName.length() == 0 && ownerName.length() > 0) {
			shopName = ownerName + "'s Shop";
		}
		
		balance = nbt.getLong("balance");
		budget = nbt.getLong("budget");
		
		shop_open = nbt.getBoolean("shop_open");
		show_hidden_shops_ui = nbt.getBoolean("show_hidden_shops_ui");
		show_hidden_stock_chests_ui = nbt.getBoolean("show_hidden_stock_chests_ui");
		shop_budget = nbt.getBoolean("shop_budget");
		
		NBTTagList shopPosList = nbt.getTagList("shops", Constants.NBT.TAG_COMPOUND);
		NBTTagList stockPosList = nbt.getTagList("stock", Constants.NBT.TAG_COMPOUND);
		NBTTagList tillPosList = nbt.getTagList("tills", Constants.NBT.TAG_COMPOUND);
		NBTTagList cartDispenserPosList = nbt.getTagList("cartDispenser", Constants.NBT.TAG_COMPOUND);
		
		NBTTagList shopInteractionsLogList = nbt.getTagList("shopInteractions", Constants.NBT.TAG_COMPOUND);
		
		for (int i = 0; i < shopPosList.tagCount(); i++) {
			NBTTagCompound pos = shopPosList.getCompoundTagAt(i);
			shopPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
			System.out.println("Reading store manager saved shop: " + i + ". Coordinates: " + pos.getInteger("x") + ", " + pos.getInteger("y") + ", " + pos.getInteger("z"));
		}
		
		for (int i = 0; i < stockPosList.tagCount(); i++) {
			NBTTagCompound pos = stockPosList.getCompoundTagAt(i);
			stockPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < tillPosList.tagCount(); i++) {
			NBTTagCompound pos = tillPosList.getCompoundTagAt(i);
			tillPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < cartDispenserPosList.tagCount(); i++) {
			NBTTagCompound pos = cartDispenserPosList.getCompoundTagAt(i);
			cartDispenserPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < shopInteractionsLogList.tagCount(); i++) {
			NBTTagCompound entry = shopInteractionsLogList.getCompoundTagAt(i);
			shopInteractions.add(entry.getInteger("id"));
			shopInteractionsLocations.add(new BlockPos(entry.getInteger("x"), entry.getInteger("y"), entry.getInteger("z")));
			shopInteractionsExtra.add(entry.getString("extra_info"));
		}
		
		
		System.out.println("End read NBT");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		System.out.println("Start write NBT");
		nbt.setTag("items", inventory.serializeNBT());
		
		
		nbt.setString("ownerName", ownerName);
		nbt.setString("ownerUuid", ownerUuid);
		nbt.setString("shopName", shopName);
		
		System.out.println("Owner name set as " + ownerName);
		
		nbt.setLong("balance", balance);
		nbt.setLong("budget", budget);
		
		nbt.setBoolean("shop_open", shop_open);
		nbt.setBoolean("show_hidden_shops_ui", show_hidden_shops_ui);
		nbt.setBoolean("show_hidden_stock_chests_ui", show_hidden_stock_chests_ui);
		nbt.setBoolean("shop_budget", shop_budget);
		
		NBTTagList shopList = new NBTTagList();
		NBTTagList stockList = new NBTTagList();
		NBTTagList tillList = new NBTTagList();
		NBTTagList cartDispenserList = new NBTTagList();
		
		/*NBTTagList activeCustomerUuidList = new NBTTagList();
		NBTTagList activeCustomerTimersList = new NBTTagList();
		NBTTagList bannedCustomerUuidList = new NBTTagList();*/
		NBTTagList shopInteractionLogList = new NBTTagList();
		
		System.out.println("Shop pos array size: " + shopPosArray.size());
		
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
		
		for (int i = 0; i < activeCustomerUuids.size(); i++) {
			//TODO
		}
		
		for (int i = 0; i < activeCustomerTimers.size(); i++) {
			
		}
		
		for (int i = 0; i < bannedCustomerUuids.size(); i++) {
			
		}
		
		for (int i = 0; i < shopInteractions.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("id", shopInteractions.get(i));
			com.setInteger("x", shopInteractionsLocations.get(i).getX());
			com.setInteger("y", shopInteractionsLocations.get(i).getY());
			com.setInteger("z", shopInteractionsLocations.get(i).getZ());
			com.setString("extra_info", shopInteractionsExtra.get(i));
			shopInteractionLogList.appendTag(com);
		}
		
		nbt.setTag("shops", shopList);
		nbt.setTag("stock", stockList);
		nbt.setTag("tills", tillList);
		nbt.setTag("cartDispenser", cartDispenserList);
		
		nbt.setTag("shopInteractions", shopInteractionLogList);
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
	
	public void logEvent(EnumLogType type, BlockPos shopPos, String extra_info) {
		System.out.println("Logging with extra info: " + extra_info);
		if (shopInteractions.size() > 100) {
			shopInteractions.remove(0);
		}
		FurenikusEconomy.log(1, "[Store Log Event]: " + type.getFullMessageForServer(extra_info, shopPos.getX(), shopPos.getY(), shopPos.getZ()));
		shopInteractions.add(type.getId());
		shopInteractionsLocations.add(shopPos);
		shopInteractionsExtra.add(extra_info);
	}
	
	public boolean addShopToManager(BlockPos shopPos) {
		if (shopPosArray.contains(shopPos)) {
			return false;
		}
		shopPosArray.add(shopPos);
		logEvent(EnumLogType.SHOP_ADDED, shopPos, "");
		sendUpdates();
		return true;
	}
	
	public boolean removeShopFromManager(BlockPos shopPos) {
		if (shopPosArray.contains(shopPos)) {
			if (shopPosArray.remove(shopPos)) {
				logEvent(EnumLogType.SHOP_REMOVED, shopPos, "");
				sendUpdates();
				return true;
			}
		}
		return false;
	}
	
	public boolean addStockToManager(BlockPos shopPos) {
		if (stockPosArray.contains(shopPos)) {
			return false;
		}
		logEvent(EnumLogType.STOCK_ADDED, shopPos, "");
		stockPosArray.add(shopPos);
		sendUpdates();
		return true;
	}
	
	public boolean removeStockFromManager(BlockPos shopPos) {
		if (stockPosArray.contains(shopPos)) {
			if (stockPosArray.remove(shopPos)) {
				logEvent(EnumLogType.STOCK_REMOVED, shopPos, "");
				sendUpdates();
				return true;
			}
		}
		return false;
	}
	
	public boolean addTillToManager(BlockPos shopPos) {
		if (tillPosArray.contains(shopPos)) {
			return false;
		}
		FurenikusEconomy.log(1, "Adding till/cashier/register " + shopPos.getX() + ", " + shopPos.getY() + ", " + shopPos.getZ() + "to a store manager");
		tillPosArray.add(shopPos);
		sendUpdates();
		return true;
	}
	
	public boolean removeTillFromManager(BlockPos shopPos) {
		if (tillPosArray.contains(shopPos)) {
			if (tillPosArray.remove(shopPos)) {
				FurenikusEconomy.log(1, "Removing till/cashier/register " + shopPos.getX() + ", " + shopPos.getY() + ", " + shopPos.getZ() + "from a store manager");
				sendUpdates();
				return true;
			}
		}
		return false;
	}
	
	public boolean addCartDispenserToManager(BlockPos shopPos) {
		if (cartDispenserPosArray.contains(shopPos)) {
			return false;
		}
		FurenikusEconomy.log(1, "Adding cart dispenser " + shopPos.getX() + ", " + shopPos.getY() + ", " + shopPos.getZ() + "to a store manager");
		cartDispenserPosArray.add(shopPos);
		sendUpdates();
		return true;
	}
	
	public boolean removeCartDispenserFromManager(BlockPos shopPos) {
		if (cartDispenserPosArray.contains(shopPos)) {
			if (cartDispenserPosArray.remove(shopPos)) {
				FurenikusEconomy.log(1, "Removing cart dispenser " + shopPos.getX() + ", " + shopPos.getY() + ", " + shopPos.getZ() + "from a store manager");
				sendUpdates();
				return true;
			}
		}
		return false;
	}
	
	public void closeShop() {
		shop_open = false;
	}
	
	public void openShop() {
		shop_open = true;
	}
	
	public boolean isShopOpen() {
		return shop_open;
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
	
	public void payStoreManager(long amount) {
		balance += amount;
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
