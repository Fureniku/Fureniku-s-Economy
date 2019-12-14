package com.silvaniastudios.econ.api.store.shops;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.EconUtils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TradePostEntity extends ShopBaseEntity {
	
	//Sell and buy are from the trade post's point of view, sell is what it gives to the 3rd party player.
	
	public long sellMoney = 0;
	public long buyMoney = 0;
	public long budgetCurrent = 0;
	public long budgetMax = 0;
	public boolean salesRefillBudget = false;
	EconUtils utils = new EconUtils();
	
	public TradePostEntity() {
		super(6);
		inventory = new ItemStackHandler(shopSize) {
			
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return true;
			}
			
			@Override
			protected void onContentsChanged(int slot) {
				markDirty();
			}
		};
	}
	
	public boolean performTrade(EntityPlayer buyer) {
		if (getManager() != null) {
			ItemStack sellItem1 = inventory.getStackInSlot(0);
			ItemStack sellItem2 = inventory.getStackInSlot(1);
			ItemStack sellItem3 = inventory.getStackInSlot(2);
			
			ItemStack buyItem1 = inventory.getStackInSlot(3);
			ItemStack buyItem2 = inventory.getStackInSlot(4);
			ItemStack buyItem3 = inventory.getStackInSlot(5);
			
			if (shopCanTrade(sellItem1, sellItem2, sellItem3) && playerCanTrade(buyer, buyItem1, buyItem2, buyItem3)) {
				if (buyItem1 != ItemStack.EMPTY) {
					takeItemFromPlayer(buyer, buyItem1, 3);
				}
				if (buyItem2 != ItemStack.EMPTY) {
					takeItemFromPlayer(buyer, buyItem2, 4);
				}
				if (buyItem3 != ItemStack.EMPTY) {
					takeItemFromPlayer(buyer, buyItem3, 5);
				}
				
				if (!buyer.addItemStackToInventory(sellItem1)) {
					world.spawnEntity(new EntityItem(world, buyer.posX, buyer.posY, buyer.posZ, sellItem1));
				}
				if (!buyer.addItemStackToInventory(sellItem2)) {
					world.spawnEntity(new EntityItem(world, buyer.posX, buyer.posY, buyer.posZ, sellItem2));
				}
				if (!buyer.addItemStackToInventory(sellItem3)) {
					world.spawnEntity(new EntityItem(world, buyer.posX, buyer.posY, buyer.posZ, sellItem3));
				}
				
				if (salesRefillBudget) {
					long difference = budgetMax - budgetCurrent;
					if (difference > buyMoney) {
						budgetCurrent = budgetMax;
						getManager().payStoreManager(buyMoney-difference);
					} else {
						budgetCurrent += buyMoney;
					}
				} else {
					getManager().payStoreManager(buyMoney);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean shopCanTrade(ItemStack s1, ItemStack s2, ItemStack s3) {
		boolean b1 = checkStockAvailability(getManager(), s1, 0);
		boolean b2 = checkStockAvailability(getManager(), s1, 1);
		boolean b3 = checkStockAvailability(getManager(), s1, 2);
		boolean b4 = budgetCurrent >= sellMoney;
		
		return b1 && b2 && b3 && b4;
	}
	
	public boolean playerCanTrade(EntityPlayer buyer, ItemStack i1, ItemStack i2, ItemStack i3) {
		if (utils.getBalance(buyer) >= buyMoney) {
			boolean b1 = false;
			boolean b2 = false;
			boolean b3 = false;
			for (int i = 0; i < buyer.inventory.getSizeInventory(); i++) {
				ItemStack found = buyer.inventory.getStackInSlot(i);
				
				if (i1 == ItemStack.EMPTY) { b1 = true; }
				if (i2 == ItemStack.EMPTY) { b2 = true; }
				if (i3 == ItemStack.EMPTY) { b3 = true; }
				
				if (!b1) {
					if (compareItemStacks(i1, found, match_meta[3], match_nbt[3])) {
						b1 = true;
					}
				}
				
				if (!b2) {
					if (compareItemStacks(i2, found, match_meta[4], match_nbt[4])) {
						b2 = true;
					}
				}
			
				if (!b3) {
					if (compareItemStacks(i3, found, match_meta[5], match_nbt[5])) {
						b3 = true;
					}
				}
				
				if (b1 && b2 && b3) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ItemStack takeItemFromPlayer(EntityPlayer player, ItemStack item, int sellSlot) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack found = player.inventory.getStackInSlot(i);
			if (compareItemStacks(item, found, match_meta[sellSlot], match_nbt[sellSlot])) {
				return found.splitStack(item.getCount());
			}
		}
		return ItemStack.EMPTY;
	}
}
