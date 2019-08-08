package com.silvaniastudios.econ.api;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.silvaniastudios.econ.api.capability.currency.CurrencyProvider;
import com.silvaniastudios.econ.api.capability.currency.ICurrency;
import com.silvaniastudios.econ.core.EconConfig;
import com.silvaniastudios.econ.core.EconItems;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.items.ItemMoney;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class EconUtils {
	
	public ArrayList<ItemMoney> itemCoins = new ArrayList<>();
	public ArrayList<ItemMoney> itemNotes = new ArrayList<>();
	public ArrayList<ItemMoney> itemMoney = new ArrayList<>();
	
	public void init() {
		itemCoins.clear();
		itemNotes.clear();
		itemMoney.clear();
		
		itemCoins.add(EconItems.coin1);
		itemCoins.add(EconItems.coin2);
		itemCoins.add(EconItems.coin5);
		itemCoins.add(EconItems.coin10);
		itemCoins.add(EconItems.coin25);
		itemCoins.add(EconItems.coin50);
		itemCoins.add(EconItems.coin100);
		
		itemNotes.add(EconItems.note100);
		itemNotes.add(EconItems.note200);
		itemNotes.add(EconItems.note500);
		itemNotes.add(EconItems.note1000);
		itemNotes.add(EconItems.note2000);
		itemNotes.add(EconItems.note5000);
		itemNotes.add(EconItems.note10000);
		
		itemMoney.addAll(itemCoins);
		itemMoney.addAll(itemNotes);
	}
	
	public void debug(String s) {
		FurenikusEconomy.log(3, "[Utils] " + s);
	}
	
	/**
	 * Converts a string that should be a number to a long.
	 * @param String The string to convert
	 * @return the long, or 0 if it fails.
	 */
	public long parseLong(String s) {
		try {
			return Long.parseLong("" + s);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
	
	/**
	 * Deposits all cash from a players inventory into their bank account.
	 * @param EntityPlayer The player
	 */
	public void depositAllCash(EntityPlayer player) {
		long cash = getInventoryCash(player); //Check how much cash the player has on them
		
		if (addMoney(player, cash)) {
			removeAllPlayerCash(player);

			player.sendMessage(new TextComponentString(
	        		TextFormatting.GOLD + formatBalance(cash) + TextFormatting.GREEN + " Deposited! Your balance is now " + 
	        		TextFormatting.GOLD + formatBalance(getBalance(player))));
		}
	}
	
	/**
	 * Withdraws funds from the players bank account and adds them to their inventory.
	 * @param long The amount to withdraw
	 * @param EntityPlayer The player
	 */
	public void withdrawFunds(long amount, EntityPlayer player) {
		long balance = getBalance(player);
		if ((balance - amount) >= 0) {
			if (chargeBalance(player, amount)) {
				giveCash(amount, player);
			}
		}
	}
	
	/** Give physical money to the player.
	 * @param long The amount to give to the player
	 * @param EntityPlayer the player to give to
	 */
	public void giveCash(long amount, EntityPlayer player) {
		giveChange(amount, 0, player);
	}
	
	/**
	 * Gives change based on the cost of an item and how much they paid.
	 * @param long the amount of money the player has given
	 * @param long the total cost
	 * @param EntityPlayer the player
	 */
	public void giveChange(long paid, long cost, EntityPlayer entityPlayer) {
		long change = paid - cost;
		long toBank = 0;	
		
		while (change >= 10000) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note10000))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note10000));
				debug("$100 to inventory :)");
			} else {
				debug("Sending $100 to bank");
				toBank = toBank + 10000;
			}
			change = change - 10000;
		}
		
		while (change >= 5000) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note5000))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note5000));
				debug("$50 to inventory :)");
			} else {
				debug("$50 to bank.");
				toBank = toBank + 5000;
			}
			change = change - 5000;
		} 
		
		while (change >= 2000) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note2000))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note2000));
				debug("$20 to inventory :)");
			} else {
				debug("$20 to bank.");
				toBank = toBank + 2000;
			}
			change = change - 2000;
		}
		
		while (change >= 1000) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note1000))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note1000));
				debug("$10 to inventory :)");
			} else {
				debug("$10 to bank.");
				toBank = toBank + 1000;
			}
			change = change - 1000;
		}
		
		while (change >= 500) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note500))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note500));
				debug("$5 to inventory :)");
			} else {
				debug("$5 to bank.");
				toBank = toBank + 500;
			}
			change = change - 500;
		}
		
		while (change >= 200) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note200))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note200));
				debug("$2 to inventory :)");
			} else {
				debug("$2 to bank.");
				toBank = toBank + 200;
			}
			change = change - 200;
		}
		
		while (change >= 100) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.note100))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.note100));
				debug("$1 to inventory :)");
			} else {
				debug("$1 to bank.");
				toBank = toBank + 100;
			}
			change = change - 100;
		}
		while (change >= 50) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.coin50))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.coin50));
				debug("$0.50 to inventory :)");
			} else {
				debug("$0.50 to bank.");
				toBank = toBank + 50;
			}
			change = change - 50;
		}
		
		while (change >= 25) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.coin25))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.coin25));
				debug("$0.25 to inventory :)");
			} else {
				debug("$0.25 to bank.");
				toBank = toBank + 25;
			}
			change = change - 25;
		}
		
		while (change >= 10) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.coin10))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.coin10));
				debug("$0.10 to inventory :)");
			} else {
				debug("$0.10 to bank.");
				toBank = toBank + 10;
			}
			change = change - 10;
		}
		
		while (change >= 5) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.coin5))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.coin5));
				debug("$0.05 to inventory :)");
			} else {
				debug("$0.05 to bank.");
				toBank = toBank + 5;
			}
			change = change - 5;
		}
		
		while (change >= 2) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.coin2))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.coin2));
				debug("$0.02 to inventory :)");
			} else {
				debug("$0.02 to bank.");
				toBank = toBank + 2;
			}
			change = change - 2;
		}
		
		while (change > 0) {
			if (inventoryHasSpace(entityPlayer, new ItemStack(EconItems.coin1))) { 
				entityPlayer.inventory.addItemStackToInventory(new ItemStack(EconItems.coin1));
				debug("$0.01 to inventory :)");
			} else {
				debug("$0.01 to bank.");
				toBank = toBank + 1;
			}
			change = change - 1;
		}
		if (toBank >= 1) {
			System.out.println("Depositing " + toBank + " to " + entityPlayer.getDisplayName() + "'s account.");
			depositToAccount(entityPlayer, toBank);
		}
	}
	
	/**
	 * Get the amount of physical cash in a players inventory.
	 * @param EntityPlayer the player to check
	 * @return the amount of money, as a long (100 = $1)
	 */
	public long getInventoryCash(EntityPlayer player) {
		long balance = 0;
		for (int i = player.inventory.getSizeInventory() - 1; i >= 0; -- i) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null) {
				long moneyValue = 0;
				if (stack.getItem() instanceof ItemMoney) {
					ItemMoney money = (ItemMoney) stack.getItem();
					if (money.getMoneyValue() >= 0) {
						moneyValue = money.getMoneyValue();
					}
				}
				int quantity = stack.getCount();
				long totalValue = moneyValue * quantity;
				balance = balance + totalValue;
			}
		}
		return balance;
	}
	
	/**
	 * Removes all the money a player has. Mainly used for ATM mass deposits, might be useful elsewhere.
	 * @param EntityPlayer the player.
	 */
	public void removeAllPlayerCash(EntityPlayer player) {
		boolean b = false;
		for (int i = player.inventory.getSizeInventory() - 1; i >= 0; -- i) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null) {
				if (stack.getItem() instanceof ItemMoney) {
					player.inventory.removeStackFromSlot(i);
					b = true;
				}
			}
		}
		if (b) { ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer); }
	}
	
	/**
	 * Find and consume cash in the players inventory.
	 * Used mostly to pay for things by cash directly (as opposed to players depositing cash into an interface to pay)
	 * @param EntityPlayer the player we are checking
	 * @param long The amount of money we're looking for
	 * @return true if successful, false if it fails. Money is not consumed for false.
	 */
	public boolean findCashInInventory(EntityPlayer player, long value) {
		if (getInventoryCash(player) >= value) {
			for (int i = player.inventory.getSizeInventory() - 1; i >= 0; -- i) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null) {
					if (stack.getItem() instanceof ItemMoney) {
						int qty = stack.getCount();
						ItemMoney money = (ItemMoney) stack.getItem();
						long moneyValue = money.getMoneyValue();
						long currentlyPaid = 0;
						//Second loop, basically checks if the stack's value is high enough one item at a time (as to not overpay)
						for(int x = 1; x <= qty; x++) {
							debug("Nested Loop! Current stack value is: " + (moneyValue * x) + " - The target is " + value);
							if (currentlyPaid + (moneyValue * x) >= value) {
								debug("This is fired if the moneyValue is higher than the value, allegedly");
								if (x == qty) {
									player.inventory.setInventorySlotContents(i, null);
								} else
									player.inventory.decrStackSize(i, x);
								long paidAmount = moneyValue * x;
								debug("Give change: " + (paidAmount - value));
								depositToAccount(player, paidAmount-value);
								((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
								//If second loop pays enough, we return; we don't need to do anything else as the balance has been paid.
								return true;
							}
						}
						//If second loop fails, this part is ran.
						currentlyPaid = currentlyPaid + (moneyValue * qty);
						player.inventory.setInventorySlotContents(i, null);
						((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
					}
				}
			}
		}
		//If they don't have enough cash, that's it - they can't buy it.
		return false;
	}
	
	/**
	 * Take money from both inventory and bank account.
	 * Will attempt to charge cash first. Once all cash runs out, the rest is paid by card.
	 * Will not charge at all and returns false if the player doesn't have enough money total.
	 * @param EntityPlayer the player
	 * @param long the amount we are charging
	 * @return true if successful, false if unsuccessful.
	 */
	public boolean chargePlayerAnywhere(EntityPlayer player, long value) {
		if (findCashInInventory(player, value) == false) {
			
			long invBalance = getInventoryCash(player);
			long cardBalance = getBalance(player);
			
			long totalBalance = invBalance + cardBalance;
			if (player.inventory.hasItemStack(new ItemStack(EconItems.debitCard))) {
				if (invBalance < value) {
					if (totalBalance >= value) {
						long payAmount = value - invBalance;
						if (chargeBalance(player, payAmount)) {
							removeAllPlayerCash(player);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Charge the players card with the defined amount. Used for withdrawl and card payments.
	 * @param EntityPlayer the player
	 * @param long the amount to charge
	 * @return
	 */
	public boolean chargeBalance(EntityPlayer player, long amt) {
		ICurrency currency = player.getCapability(CurrencyProvider.CURRENCY, null);
		if (amt <= currency.getMoney()) {
			debug("AMT is <= cardBalance");
			return currency.subtractMoney(amt);
		}
		debug(player.getDisplayName() + " did not have enough to withdraw.");
		return false;
	}
	
	/**
	 * Formats the balance as a printable string, including the currency symbol. 
	 * For example, passing 100 with default configs will return $1.00
	 * Use this anywhere you are DISPLAYING a value, for example the cost of something.
	 * @param int The balance to convert
	 * @return The balance as a formatted string defined by the mods config file.
	 */
	public String formatBalance(long bal) {
		double printBal = bal / 100.0; 
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		nf.setMinimumFractionDigits(EconConfig.currencyDecimals);
		nf.setMaximumFractionDigits(EconConfig.currencyDecimals);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		String str = nf.format(printBal);
		str = str.replace(",", "");
		
		return EconConfig.currencySign + str;
	}

	public long getBalance(EntityPlayer player) {
		ICurrency currency = player.getCapability(CurrencyProvider.CURRENCY, null);
        return currency.getMoney();
	}
	
	//Checks if the inventory has room for the specified itemstack. Returns false either if all slots are full (with no matching types),
	//Or if there's an available slot but it doesn't have room to accomodate.
	//Returns true if there's an empty slot, or a partially filled slot of the same type which has enough free space to add to it.
	//I am aware that the addItemStackToInventory method already does this check, but you may want to use this anyway.
	//For example, you don't want to charge a player and then not give them their stuff,
	//and likewise, you don't want to give them the stuff and find you can't charge them.
	//Consider this a "safe check" - it finds the answer without altering the inventory in any way.
	public boolean inventoryHasSpace(EntityPlayer player, ItemStack item) {
		for (int x = 35; x >= 0; x--) {
			ItemStack slot = player.inventory.getStackInSlot(x);
			if (slot != null) {
				if (slot.getItem().equals(item.getItem())) {
					int max = slot.getMaxStackSize();
					int slotSize = slot.getCount();
					int itemSize = item.getCount();
					
					if ((itemSize + slotSize) <= max) {
						debug("Unfilled compatable stack found; adding to it.");
						return true;
					}	
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	//Finds out if the player has a debit card that they own in their inventory.
	public boolean hasOwnCard(EntityPlayer player) {
		for (int i = player.inventory.getSizeInventory() - 1; i >= 0; -- i) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null) {
				if (stack.getItem() == EconItems.debitCard) {
					if (player.getUniqueID().toString().equals(stack.getTagCompound().getString("playerUUID"))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void depositToAccount(EntityPlayer player, long deposit) {
		if (addMoney(player, deposit)) {
			player.sendMessage(new TextComponentString(TextFormatting.GOLD + formatBalance(deposit) + TextFormatting.GREEN + " was sent to your bank account. Your current total balance is " + TextFormatting.GOLD  + "$" + formatBalance(getBalance(player))));
		}
	}
	
	public boolean addMoney(EntityPlayer player, long amount) {
		ICurrency currency = player.getCapability(CurrencyProvider.CURRENCY, null);
		
		if (currency == null) {
			FurenikusEconomy.log(0, player.getName() + " has no currency capability set! This is a bug!");
			return false;
		}
		
		currency.addMoney(amount);
		return true;
	}
	
	public int getMoneyTypeCountFromArray(int[] moneyCount, ItemMoney item) {
		if (itemMoney.size() != 14) {
			init();
		}
		
		return moneyCount[itemMoney.indexOf(item)];
	}

	public int[] setMoneyTypeCountForArray(int[] moneyCountIn, ItemMoney item, int amount) {
		if (itemMoney.size() != 14) {
			init();
		}
		
		moneyCountIn[itemMoney.indexOf(item)] = amount;
		return moneyCountIn;
	}
	
	public int[] addMoneyTypeCountForArray(int[] moneyCountIn, ItemMoney item, int amount) {
		if (itemMoney.size() != 14) {
			init();
		}
		
		moneyCountIn[itemMoney.indexOf(item)] += amount;
		return moneyCountIn;
	}
	
	public int[] subtractMoneyTypeCountForArray(int[] moneyCountIn, ItemMoney item, int amount) {
		if (itemMoney.size() != 14) {
			init();
		}
		
		int index = itemMoney.indexOf(item);
		moneyCountIn[index] -= amount;
		if (moneyCountIn[index] < 0) {
			moneyCountIn[index] = 0;
		}
		return moneyCountIn;
	}
}
