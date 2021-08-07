package com.fureniku.econ.store.management;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fureniku.econ.EconConfig;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.items.StoreStockPairer;
import com.fureniku.econ.store.Customer;
import com.fureniku.econ.store.StockEntry;

import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StoreManagerEntity extends TileEntity implements ITickable {
	
	public ArrayList<BlockPos> shopPosArray = new ArrayList<>();
	public ArrayList<BlockPos> stockPosArray = new ArrayList<>();
	public ArrayList<BlockPos> returnStockPosArray = new ArrayList<>();
	public ArrayList<BlockPos> tillPosArray = new ArrayList<>();
	public ArrayList<BlockPos> cartDispenserPosArray = new ArrayList<>();
	
	private String ownerName = "";
	private String ownerUuid = "";
	private String shopName = "";
	
	public boolean show_hidden_shops_ui = false;
	public boolean show_hidden_stock_chests_ui = false;
	public boolean shop_open = true;
	public boolean shop_budget = false;
	
	//public ArrayList<String> activeCustomerUuids = new ArrayList<>();
	//public ArrayList<Integer> activeCustomerTimers = new ArrayList<>();
	public ArrayList<Customer> activeCustomers = new ArrayList<Customer>();
	public ArrayList<Customer> customersPendingClear = new ArrayList<Customer>(); //World is null during loading, so any non-existing customers get put in here. Once world is ready, they're then safely removed from the cart.
	public ArrayList<String> bannedCustomerUuids = new ArrayList<>();
	
	public ArrayList<StockEntry> stockTypes = new ArrayList<StockEntry>();
	
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
		returnStockPosArray.clear();
		tillPosArray.clear();
		cartDispenserPosArray.clear();
		activeCustomers.clear();
		
		stockTypes.clear();
		
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
		NBTTagList returnStockPosList = nbt.getTagList("returns", Constants.NBT.TAG_COMPOUND);
		NBTTagList tillPosList = nbt.getTagList("tills", Constants.NBT.TAG_COMPOUND);
		NBTTagList cartDispenserPosList = nbt.getTagList("cartDispenser", Constants.NBT.TAG_COMPOUND);
		NBTTagList activeCustomersList = nbt.getTagList("activeCustomers", Constants.NBT.TAG_COMPOUND);
		NBTTagList stockTypesList = nbt.getTagList("stockTypes", Constants.NBT.TAG_COMPOUND);
		
		NBTTagList shopInteractionsLogList = nbt.getTagList("shopInteractions", Constants.NBT.TAG_COMPOUND);
		
		for (int i = 0; i < shopPosList.tagCount(); i++) {
			shopPosArray.add(deserializeBlockPos(shopPosList.getCompoundTagAt(i)));
		}
		
		for (int i = 0; i < stockPosList.tagCount(); i++) {
			stockPosArray.add(deserializeBlockPos(stockPosList.getCompoundTagAt(i)));
		}
		
		for (int i = 0; i < returnStockPosList.tagCount(); i++) {
			returnStockPosArray.add(deserializeBlockPos(returnStockPosList.getCompoundTagAt(i)));
		}
		
		for (int i = 0; i < tillPosList.tagCount(); i++) {
			tillPosArray.add(deserializeBlockPos(tillPosList.getCompoundTagAt(i)));
		}
		
		for (int i = 0; i < cartDispenserPosList.tagCount(); i++) {
			cartDispenserPosArray.add(deserializeBlockPos(cartDispenserPosList.getCompoundTagAt(i)));
		}
		
		for (int i = 0; i < activeCustomersList.tagCount(); i++) {
			NBTTagCompound custs = activeCustomersList.getCompoundTagAt(i);
			Customer customer = Customer.deserializeNbt(world, custs);
			if (customer.getUuid().equalsIgnoreCase("NULL")) {
				customersPendingClear.add(customer);
			} else {
				activeCustomers.add(customer);
			}
		}
		
		for (int i = 0; i < stockTypesList.tagCount(); i++) {
			NBTTagCompound stocks = stockTypesList.getCompoundTagAt(i);
			StockEntry stock = StockEntry.deserializeNbt(world, stocks);
			stockTypes.add(stock);
		}
		
		for (int i = 0; i < shopInteractionsLogList.tagCount(); i++) {
			NBTTagCompound entry = shopInteractionsLogList.getCompoundTagAt(i);
			shopInteractions.add(entry.getInteger("id"));
			shopInteractionsLocations.add(new BlockPos(entry.getInteger("x"), entry.getInteger("y"), entry.getInteger("z")));
			shopInteractionsExtra.add(entry.getString("extra_info"));
		}
		
		
		System.out.println("End read NBT");
	}
	
	public BlockPos deserializeBlockPos(NBTTagCompound nbt) {
		return new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}
	
	public NBTTagCompound serializeBlockPos(BlockPos pos) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		return nbt;
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
		NBTTagList returnList = new NBTTagList();
		NBTTagList tillList = new NBTTagList();
		NBTTagList cartDispenserList = new NBTTagList();
		NBTTagList activeCustomerList = new NBTTagList();
		NBTTagList stockTypeList = new NBTTagList();
		
		NBTTagList shopInteractionLogList = new NBTTagList();
		
		System.out.println("Shop pos array size: " + shopPosArray.size());
		
		for (int i = 0; i < shopPosArray.size(); i++) {
			shopList.appendTag(serializeBlockPos(shopPosArray.get(i)));
		}
		
		for (int i = 0; i < stockPosArray.size(); i++) {
			stockList.appendTag(serializeBlockPos(stockPosArray.get(i)));
		}
		
		for (int i = 0; i < returnStockPosArray.size(); i++) {
			returnList.appendTag(serializeBlockPos(returnStockPosArray.get(i)));
		}
		
		for (int i = 0; i < tillPosArray.size(); i++) {
			tillList.appendTag(serializeBlockPos(tillPosArray.get(i)));
		}
		
		for (int i = 0; i < cartDispenserPosArray.size(); i++) {
			cartDispenserList.appendTag(serializeBlockPos(cartDispenserPosArray.get(i)));
		}
		
		for (int i = 0; i < stockTypes.size(); i++) {
			stockTypeList.appendTag(StockEntry.serializeNBT(stockTypes.get(i)));
		}
		
		for (int i = 0; i < activeCustomers.size(); i++) {
			activeCustomerList.appendTag(Customer.serializeNbt(activeCustomers.get(i)));
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
		nbt.setTag("returns", returnList);
		nbt.setTag("tills", tillList);
		nbt.setTag("cartDispenser", cartDispenserList);
		nbt.setTag("activeCustomers", activeCustomerList);
		
		nbt.setTag("shopInteractions", shopInteractionLogList);
		System.out.println("End write NBT");
		return nbt;
	}
	
	public String getOwnerName() { return ownerName; }
	public String getOwnerUuid() { return ownerUuid; }
	public String getShopName() { return shopName; }
	
	public void setOwnerName(String name) { this.ownerName = name; }
	public void setOwnerUuid(String uuid) { this.ownerUuid = uuid; }
	public void setShopName(String name) { this.shopName = name; }
	
	
	
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
	
	public void logEvent(EnumLogType type, String extra_info) {
		logEvent(type, BlockPos.ORIGIN, extra_info);
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
	
	public boolean addReturnToManager(BlockPos shopPos) {
		if (returnStockPosArray.contains(shopPos)) {
			return false;
		}
		logEvent(EnumLogType.RETURN_STOCK_ADDED, shopPos, "");
		returnStockPosArray.add(shopPos);
		sendUpdates();
		return true;
	}
	
	public boolean removeReturnStockFromManager(BlockPos shopPos) {
		if (returnStockPosArray.contains(shopPos)) {
			if (returnStockPosArray.remove(shopPos)) {
				logEvent(EnumLogType.RETURN_STOCK_REMOVED, shopPos, "");
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
		logEvent(EnumLogType.TILL_ADDED, shopPos, "");
		sendUpdates();
		return true;
	}
	
	public boolean removeTillFromManager(BlockPos shopPos) {
		if (tillPosArray.contains(shopPos)) {
			if (tillPosArray.remove(shopPos)) {
				FurenikusEconomy.log(1, "Removing till/cashier/register " + shopPos.getX() + ", " + shopPos.getY() + ", " + shopPos.getZ() + "from a store manager");
				sendUpdates();
				logEvent(EnumLogType.TILL_REMOVED, shopPos, "");
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
		logEvent(EnumLogType.CART_ADDED, shopPos, "");
		sendUpdates();
		return true;
	}
	
	public boolean removeCartDispenserFromManager(BlockPos shopPos) {
		if (cartDispenserPosArray.contains(shopPos)) {
			if (cartDispenserPosArray.remove(shopPos)) {
				FurenikusEconomy.log(1, "Removing cart dispenser " + shopPos.getX() + ", " + shopPos.getY() + ", " + shopPos.getZ() + "from a store manager");
				logEvent(EnumLogType.CART_REMOVED, shopPos, "");
				for (int i = 0; i < activeCustomers.size(); i++) {
					if (activeCustomers.get(i).getCartPos().equals(shopPos)) {
						FurenikusEconomy.log(0, "GHOST CART DETECTED. Removing...");
						activeCustomers.remove(i);
					}
				}
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
			customer.sendMessage(new TextComponentTranslation("econ.shop.customer.shop_ban", shopName));
		}
	}
	
	public boolean unbanCustomer(String uuid) {
		return bannedCustomerUuids.remove(uuid);
	}
	
	public boolean addActiveCustomer(EntityPlayer player, CartDispenserEntity cart) {
		System.out.println("Add active customer, is client : " + world.isRemote);
		if (cart.hasAvailableCart()) {
			activeCustomers.add(new Customer(player, cart.getPos(), cart.firstAvailableCartId()));
			player.sendMessage(new TextComponentTranslation("econ.shop.customer.open_cart_customer", shopName));
			if (getOwnerPlayer() != null) {
				getOwnerPlayer().sendMessage(new TextComponentTranslation("econ.shop.customer.open_cart_owner", player.getDisplayNameString(), shopName));
			}
			this.logEvent(EnumLogType.CUSTOMER_START_CART, player.getDisplayNameString());
			this.sendUpdates();
			cart.sendUpdates();
			return true;
		}
		return false;
	}
	
	public boolean isActiveCustomer(EntityPlayer player) {
		return getCustomerByUuid(player.getCachedUniqueIdString()) != null;
	}
	
	/**
	 * Gets a customer, if one exists, by a players UUID (as a string)
	 * @param String the players uuid
	 * @return the customer if they exist, null if not.
	 */
	public Customer getCustomerByUuid(String uuid) {
		for (int i = 0; i < activeCustomers.size(); i++) {
			if (activeCustomers.get(i).getUuid().equalsIgnoreCase(uuid)) {
				return activeCustomers.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Removes a customer and closes their cart.
	 * @param Customer Customer object (use getCustomerByUuid(String) if you need this)
	 * @param Integer TYPE the cause of this closure:
	 * 0 = timeout
	 * 1 = forced by shop owner
	 * 2 = customer death
	 * 3 = customer distance
	 * 4 = customer logging out
	 * 5 = Shop closure
	 * @return
	 */
	public boolean removeActiveCustomer(Customer customer, int type) {
		if (activeCustomers.contains(customer)) {
			activeCustomers.remove(customer);
			
			this.logEvent(EnumLogType.CUSTOMER_END_CART, customer.getName());

			CartDispenserEntity cart = customer.getCart(world);
			cart.closeCart(customer, customer.getCartId());
			
			EntityPlayer customerPlayer = customer.getCustomerPlayer(world);
			EntityPlayer ownerPlayer = getOwnerPlayer();
			if (customer != null) {
				if (type == 0) {
					if (customerPlayer != null) {
						customerPlayer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closed_inactive", shopName));
					}
					if (ownerPlayer != null) {
						ownerPlayer.sendMessage(new TextComponentTranslation("econ.shop.owner.cart_closed_inactive", customerPlayer.getDisplayNameString(), shopName));
					}
				}
				if (type == 1) {
					if (customerPlayer != null) {
						customerPlayer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closed_forced", shopName));
					}
					if (ownerPlayer != null) {
						ownerPlayer.sendMessage(new TextComponentTranslation("econ.shop.owner.cart_closed_forced", customerPlayer.getDisplayNameString(), shopName));
					}
				}
				if (type == 2) {
					if (customerPlayer != null) {
						customerPlayer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closed_death", shopName));
					}
					if (ownerPlayer != null) {
						ownerPlayer.sendMessage(new TextComponentTranslation("econ.shop.owner.cart_closed_death", customerPlayer.getDisplayNameString(), shopName));
					}
				}
				if (type == 3) {
					if (customerPlayer != null) {
						customerPlayer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closed_distance", shopName));
					}
					if (ownerPlayer != null) {
						ownerPlayer.sendMessage(new TextComponentTranslation("econ.shop.owner.cart_closed_distance", customerPlayer.getDisplayNameString(), shopName));
					}
				}
				if (type == 4) {
					if (customerPlayer != null) {
						customerPlayer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closed_logout", shopName));
					}
					if (ownerPlayer != null) {
						ownerPlayer.sendMessage(new TextComponentTranslation("econ.shop.owner.cart_closed_logout", customer.getName(), shopName));
					}
				}
				if (type == 5) {
					if (customerPlayer != null) {
						customerPlayer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closed_shop_closed", shopName));
					}
					if (ownerPlayer != null) {
						ownerPlayer.sendMessage(new TextComponentTranslation("econ.shop.owner.cart_closed_shop_closed", customerPlayer.getDisplayNameString(), shopName));
					}
				}
			}
			this.sendUpdates();
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the customer at the specified ID in the specified cart. Returns null if one can't be found.
	 * @param CartDispenserEntity The cart to check
	 * @param int the ID slot (0-3)
	 * @return
	 */
	public Customer getCustomerInCart(CartDispenserEntity cart, int id) {
		System.out.println("get customer in cart. Active customer size: " + activeCustomers.size());
		if (!activeCustomers.isEmpty()) {
			for (int i = 0; i < activeCustomers.size(); i++) {
				Customer cust = activeCustomers.get(i);
				System.out.println("getting customer in cart. ID: " + cust.getCartId() + " vs " + id);
				System.out.println("cart pos x/z " + cust.getCartPos().getX() + "," + cust.getCartPos().getZ() + " vs " + cart.getPos().getX() + "," + cart.getPos().getZ());
				if (cust.getCartId() == id) {
					System.out.println("Match ID");
					if (compareBlockPos(cust.getCartPos(), cart.getPos())) {
						System.out.println("Match cart pos");
						return cust;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (customersPendingClear.size() > 0) {
				for (int i = 0; i < customersPendingClear.size(); i++) {
					Customer c = customersPendingClear.get(i);
					c.getCart(world).closeCart(c, c.getCartId());
					customersPendingClear.remove(i);
				}
			}
			if (this.shop_open) {
				for (int i = 0; i < activeCustomers.size(); i++) {
					Customer customer = activeCustomers.get(i);
					EntityPlayer customerPlayer = customer.getCustomerPlayer(world);
					
					if (customerPlayer == null) { //Logged out/disconnected
						removeActiveCustomer(customer, 4);
						break;
					}
					
					customer.tick(world);
					
					if (customer.hasExpired()) { //Timed out
						removeActiveCustomer(customer, 0);
						break;
					}
					
					if (customerPlayer.isDead) { //Died
						removeActiveCustomer(customer, 2);
						break;
					}
					
					if (pos.distanceSq(customerPlayer.posX, customerPlayer.posY, customerPlayer.posZ) > EconConfig.maxDistance) { //Moved too far
						removeActiveCustomer(customer, 3);
						break;
					}
				}
			} else {
				//Remove all active customers when the shop closes
				if (activeCustomers.size() > 0) {
					removeActiveCustomer(activeCustomers.get(0), 5);
				}
			}
		}
	}
	
	public EntityPlayer getOwnerPlayer() {
		return world.getPlayerEntityByUUID(UUID.fromString(ownerUuid));
	}
	
	public void payStoreManager(long amount) {
		balance += amount;
	}
	
	//TODO move to cart entity
	public void moveCartToStockChest(ItemStack stack) {
		for (int i = 0; i < returnStockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(returnStockPosArray.get(i));
			
			if (te != null && te instanceof BackToStockChestEntity) {
				BackToStockChestEntity e = (BackToStockChestEntity) te;
				
				/*if (e.isBackToStock) {
					for (int j = 0; j < e.inventory.getSlots(); j++) {
						stack = e.inventory.insertItem(j, stack, false);
						//TODO test this.
						if (stack == ItemStack.EMPTY) {
							return;
						}
					}
				}*/
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
	
	public boolean compareBlockPos(BlockPos pos1, BlockPos pos2) {
		return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
	}
	
	public ItemStack putItemInBackToStock(ItemStack stack) {
		for (int i = 0; i < returnStockPosArray.size(); i++) {
			if (world.getTileEntity(returnStockPosArray.get(i)) instanceof BackToStockChestEntity) {
				BackToStockChestEntity bts = (BackToStockChestEntity) world.getTileEntity(returnStockPosArray.get(i));
				
				if (!bts.isFull()) {
					for (int j = 0; j < bts.inventory.getSlots(); j++) {
						stack = bts.inventory.insertItem(j, stack, false);
						if (stack.isEmpty()) {
							return stack;
						}
					}
				}
			}
		}
		return stack;
	}
}
