package com.fureniku.econ.store.management;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.store.Customer;
import com.fureniku.econ.store.StoreEntityBase;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CartDispenserEntity extends StoreEntityBase implements ITickable {
	
	String cart_name = "Cart";
	
	private int tickCount = 0;
	
	int cartCount = 4;
	
	public boolean slotUsed[] = new boolean[cartCount];
	public boolean slotClosed[] = new boolean[] { false, false, false, false };
	
	public int[][] cart_prices = new int[cartCount][27];
	public int[] cart_totals = new int[cartCount];
	
	public ItemStackHandler inventory = new ItemStackHandler(EconConstants.Inventories.CUSTOMER_CART_SIZE * 4) {
		
	};
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (facing != null) {
			return false;
		}
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
	public void readNBT(NBTTagCompound nbt) {
		cart_name = nbt.getString("cart_name");
		if (nbt.hasKey("items")) {
			inventory.deserializeNBT((NBTTagCompound) nbt.getTag("items"));
		}
		for (int i = 0; i < cartCount; i++) {
			slotUsed[i] = nbt.getBoolean("slotUsed" + i);
			slotClosed[i] = nbt.getBoolean("slotClosed" + i);
			
			cart_prices[i] = nbt.getIntArray("prices" + i);
			if (cart_prices[i].length <= 0) {
				cart_prices[i] = new int[27];
				System.out.println("creating new cart price entry on cart " + i);
			}
		}
		cart_totals = nbt.getIntArray("total_prices");
		if (cart_totals.length <= 0) {
			cart_totals = new int[cartCount];
			System.out.println("Creating new cart total entry");
		}
	}
	
	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setString("cart_name", cart_name);
		nbt.setTag("items", inventory.serializeNBT());
		for (int i = 0; i < cartCount; i++) {
			nbt.setBoolean("slotUsed" + i, slotUsed[i]);
			nbt.setBoolean("slotClosed" + i, slotClosed[i]);
			
			nbt.setIntArray("prices" + i, this.cart_prices[i]);
		}
		nbt.setIntArray("total_prices", this.cart_totals);
		return nbt;
	}

	public int getCartBalance(int cartId) {
		return cart_totals[cartId];
	}
	
	public int getSlotPrice(int cartId, int slotId) {
		return cart_prices[cartId][slotId];
	}
	
	public boolean hasAvailableCart() {
		for (int i = 0; i < slotUsed.length; i++) {
			if (slotUsed[i] == false) {
				return true;
			}
		}
		
		return false;
	}
	
	public int firstAvailableCartId() {
		for (int i = 0; i < slotUsed.length; i++) {
			if (slotUsed[i] == false) {
				slotUsed[i] = true;
				this.sendUpdates();
				return i;
			}
		}
		return -1;
	}
	
	public void closeCart(Customer customer, int id) {
		if (customer != null) {
			customer.closeCart(customer.getCustomerPlayer(getWorld()));
		}
		slotClosed[id] = true;
		for (int i = 0; i < 27; i++) {
			this.cart_prices[id][i] = 0;
		}
		this.updateTotalCartBalance(id);
		this.sendUpdates();
	}
	
	public int getUsedSlotsInCart(int cartId) {
		int usedSlots = 0;
		for (int i = (cartId*27); i < (cartId*27) + 27; i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				usedSlots++;
			}
		}
		return usedSlots;
	}

	@Override
	public void update() {
		for (int i = 0; i < cartCount; i++) {
			if (slotClosed[i]) {
				boolean cartEmpty = false;
				
				//TODO this.getManager().putItemInBackToStock(stack)
				
				if (cartEmpty) {
					slotClosed[i] = false;
					slotUsed[i] = false;
					this.sendUpdates();
					//Cart slot is now available again for next customer
				}
			}
		}
		
		if (tickCount < 600) {
			tickCount++;
		} else {
			validateCarts();
			tickCount = 0;
		}
	}
	
	public boolean sendItemToBackToStock() {
		for (int i = 0; i < this.inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				ItemStack stack = inventory.extractItem(i, 1, false);
				stack = this.getManager().putItemInBackToStock(stack);
				if (stack.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void validateCarts() {
		Customer[] customers = new Customer[4];
		System.out.println("Beginning cart validation. Total customers across all carts of this shop: " + this.getManager().activeCustomers.size());
		for (int i = 0; i < this.getManager().activeCustomers.size(); i++) {
			Customer customer = this.getManager().activeCustomers.get(i);
			
			if (this.getManager().compareBlockPos(customer.getCartPos(), this.pos)) {
				customers[customer.getCartId()] = customer;
				System.out.println("Customer at slot " + i + " exists");
			}
		}
		
		for (int i = 0; i < this.cartCount; i++) {
			if (customers[i] != null) {
				//slot is actively in use.
				System.out.println("as we said, they exist at " + i);
				slotUsed[i] = true;
			} else if (customers[i] == null && slotUsed[i]) {
				FurenikusEconomy.log(1, "Ghost cart found on slot " + i + " of a cart belonging to " + this.getManager().getShopName() + ". Removing...");
				//Probably a ghost slot. Close it as if it's any other closed cart.
				closeCart(null, i);
			}
		}
	}
	
	//Only handles moving the item into the cart, and setting the price. Does NOT check stock levels or anything like that!!
	public boolean moveStockToCart(Customer customer, ItemStack stack, int cost) {
		return insertItemStack(customer.getCartId(), stack, cost);
	}
	
	public boolean insertItemStack(int cartId, ItemStack stack, int cost) {
		System.out.println("Begginging insert anywhere on cartID " + cartId + " - time to spam!");
		for (int i = 27*cartId; i < (27*cartId) + 27; i++) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			System.out.println("Slot " + i + " is  " + slotStack.getDisplayName());
			if (slotStack.isEmpty()) {
				System.out.println("Empty slot, we gucci");
				inventory.insertItem(i, stack, false);
				this.cart_prices[cartId][i] = cost;
				updateTotalCartBalance(cartId);
				this.sendUpdates();
				return true;
			}
			//Currently, you can only put things into empty slots.
			//This is to track the cost of the cart, by assigning the cost to each slot.
			//That could get confusing if seller sells 16/32 of the same item for different relative prices, so we just block it for now.
			/*System.out.println("ok now we compare it to " + stack.getDisplayName());
			if (ItemStack.areItemsEqual(stack, slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack)) {
				int space = slotStack.getMaxStackSize() - slotStack.getCount();
				if (space >= stack.getCount()) {
					System.out.println("Enough space for it right away");
					return true;
				} else {
					System.out.println("not enough space, hopefully more room later.");
					stack.setCount(stack.getCount() - space);
				}
			}*/
		}
		return false;
	}
	
	public void updateTotalCartBalance(int cartId) {
		int bal = 0;
		for (int i = 0; i < 27; i++) {
			bal += this.cart_prices[cartId][i];
		}
		this.cart_totals[cartId] = bal;
	}
}
