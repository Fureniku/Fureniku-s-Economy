package com.fureniku.econ.store.shops;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.store.Customer;
import com.fureniku.econ.store.StoreEntityBase;
import com.fureniku.econ.store.management.CartDispenserEntity;
import com.fureniku.econ.store.management.StockChestEntity;
import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.ItemStackHandler;

public class ShopBaseEntity extends StoreEntityBase {
	
	public int shopSize;
	public int[] priceList;
	
	public float[] slot_scale;
	public float[] slot_x_pos;
	public float[] slot_y_pos;
	public float[] slot_z_pos;
	public int[] slot_x_rot;
	public int[] slot_y_rot;
	public int[] slot_z_rot;
	
	public String shopName = "";
	public int selectedId = 0;
	
	public boolean[] match_nbt;
	
	EconUtils utils = new EconUtils();
	
	public ShopBaseEntity(int shopSize) {
		this.shopSize = shopSize;
		
		priceList = new int[shopSize];
		match_nbt = new boolean[shopSize];
		
		slot_scale = new float[shopSize];
		slot_x_pos = new float[shopSize];
		slot_y_pos = new float[shopSize];
		slot_z_pos = new float[shopSize];
		
		slot_x_rot = new int[shopSize];
		slot_y_rot = new int[shopSize];
		slot_z_rot = new int[shopSize];
		
		for (int i = 0; i < shopSize; i++) {
			slot_scale[i] = 1;
		}
	}
	
	public ItemStackHandler inventory = null;
	
	@Override
	public void readNBT(NBTTagCompound nbt) {
		priceList = nbt.getIntArray("priceList");
		
		for (int i = 0; i < shopSize; i++) {
			slot_scale[i] = nbt.getFloat("slot_scale_" + i);
			slot_x_pos[i] = nbt.getFloat("slot_x_pos_" + i);
			slot_y_pos[i] = nbt.getFloat("slot_y_pos_" + i);
			slot_z_pos[i] = nbt.getFloat("slot_z_pos_" + i);
		}
		
		slot_x_rot = nbt.getIntArray("slot_x_rot");
		slot_y_rot = nbt.getIntArray("slot_y_rot");
		slot_z_rot = nbt.getIntArray("slot_z_rot");
		
		selectedId = nbt.getInteger("selected_id");
		super.readNBT(nbt);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		super.writeNBT(nbt);
		nbt.setIntArray("priceList", priceList);
		
		for (int i = 0; i < shopSize; i++) {
			nbt.setFloat("slot_scale_" + i, slot_scale[i]);
			nbt.setFloat("slot_x_pos_" + i, slot_x_pos[i]);
			nbt.setFloat("slot_y_pos_" + i, slot_y_pos[i]);
			nbt.setFloat("slot_z_pos_" + i, slot_z_pos[i]);
		}
		
		nbt.setIntArray("slot_x_rot", slot_x_rot);
		nbt.setIntArray("slot_y_rot", slot_y_rot);
		nbt.setIntArray("slot_z_rot", slot_z_rot);
		
		nbt.setInteger("selected_id", selectedId);
		return nbt;
	}
	
	public ItemStack findStockInStockChests(StoreManagerEntity e, ItemStack itemToFind, int saleSlot) {
		for (int i = 0; i < e.stockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(e.stockPosArray.get(i));
			if (te != null && te instanceof StockChestEntity) {
				StockChestEntity entity = (StockChestEntity) te;
				//Ignore slot 0 as that's the display slot.
				for (int j = 1; j < entity.inventory.getSlots(); j++) {
					ItemStack found = entity.inventory.getStackInSlot(j);
					
					if (compareItemStacks(itemToFind, found, match_nbt[saleSlot])) {
						return found.splitStack(itemToFind.getCount());
					}
				}
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	public boolean checkStockAvailability(StoreManagerEntity e, ItemStack itemToFind, int saleSlot) {
		for (int i = 0; i < e.stockPosArray.size(); i++) {
			TileEntity te = world.getTileEntity(e.stockPosArray.get(i));
			if (te != null && te instanceof StockChestEntity) {
				StockChestEntity entity = (StockChestEntity) te;
				for (int j = 0; j < entity.inventory.getSlots(); j++) {
					ItemStack found = entity.inventory.getStackInSlot(j);
					if (compareItemStacks(itemToFind, found, match_nbt[saleSlot])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean compareItemStacks(ItemStack original, ItemStack found, boolean nbt) {
		if (found.getItem() == original.getItem()) {
			if ((found.getItemDamage() == original.getItemDamage())) {
				if ((found.getTagCompound() == original.getTagCompound()) || !nbt) {
					if (found.getCount() >= original.getCount()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds an item to the players cart if it exists.
	 * @param int The ID of the slot we're transacting
	 * @param EntityPlayer the player making the purchase
	 * @param ItemStackHandler the available items in this shop interface
	 * @return
	 */
	public boolean addToCart(int slotId, EntityPlayer buyer) {
		if (slotId <= shopSize) {
			if (getManager() != null) {
				StoreManagerEntity e = getManager();
				if (e.isActiveCustomer(buyer)) {
					Customer customer = e.getCustomerByUuid(buyer.getCachedUniqueIdString());
					
					customer.customerAction();
					ItemStack item = inventory.getStackInSlot(slotId);
					ItemStack found = findStockInStockChests(e, item, slotId);
					if (found != ItemStack.EMPTY) {
						CartDispenserEntity cart = customer.getCart(world);
						if (cart.moveStockToCart(customer, found, this.priceList[slotId])) {
							buyer.sendMessage(new TextComponentTranslation("econ.shop.customer.added_to_cart", found.getDisplayName()));
							return true;
						} else {
							buyer.sendMessage(new TextComponentTranslation("econ.shop.customer.cart_full"));
							sendBackToStock(found);
							return false;
						}
					} else {
						buyer.sendMessage(new TextComponentTranslation("econ.shop.customer.out_of_stock"));
						return false;
					}
				} else {
					buyer.sendMessage(new TextComponentTranslation("econ.shop.customer.no_cart"));
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean sendBackToStock(ItemStack stack) {
		return false;
	}
}
