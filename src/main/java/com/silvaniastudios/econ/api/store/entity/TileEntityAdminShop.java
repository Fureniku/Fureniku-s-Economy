package com.silvaniastudios.econ.api.store.entity;

import com.silvaniastudios.econ.api.EconUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAdminShop extends TileEntity implements IInventory {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	public EconUtils econ = new EconUtils();
	
	public String ownerName;
	public String userName;
	private ItemStack[] items;
	public double buyPrice1;
	public double sellPrice1;
	public double buyPrice2;
	public double sellPrice2;
	public double buyPrice3;
	public double sellPrice3;
	public double buyPrice4;
	public double sellPrice4;
	
	/*@Override
	public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.items[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);
        
		nbt.setString("ownerName", ownerName + "");
		nbt.setDouble("buyPrice1", buyPrice1);
		nbt.setDouble("sellPrice1", sellPrice1);
		nbt.setDouble("buyPrice2", buyPrice2);
		nbt.setDouble("sellPrice2", sellPrice2);
		nbt.setDouble("buyPrice3", buyPrice3);
		nbt.setDouble("sellPrice3", sellPrice3);
		nbt.setDouble("buyPrice4", buyPrice4);
		nbt.setDouble("sellPrice4", sellPrice4);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        this.items = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.items.length) {
                this.items[j] = new ItemStack(nbttagcompound1);
            }
        }
		this.ownerName = nbt.getString("ownerName");
		this.buyPrice1 = nbt.getDouble("buyPrice1");
		this.sellPrice1 = nbt.getDouble("sellPrice1");
		this.buyPrice2 = nbt.getDouble("buyPrice2");
		this.sellPrice2 = nbt.getDouble("sellPrice2");
		this.buyPrice3 = nbt.getDouble("buyPrice3");
		this.sellPrice3 = nbt.getDouble("sellPrice3");
		this.buyPrice4 = nbt.getDouble("buyPrice4");
		this.sellPrice4 = nbt.getDouble("sellPrice4");
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.items.length; i++) {
			ItemStack item = this.items[i];
			if (item != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				item.writeToNBT(tag);
				items.appendTag(tag);
			}
		}
		
		nbt.setTag("ClientAShelfInv", items);
		
		nbt.setString("ownerName", ownerName + "");
		nbt.setDouble("buyPrice1", buyPrice1);
		nbt.setDouble("sellPrice1", sellPrice1);
		nbt.setDouble("buyPrice2", buyPrice2);
		nbt.setDouble("sellPrice2", sellPrice2);
		nbt.setDouble("buyPrice3", buyPrice3);
		nbt.setDouble("sellPrice3", sellPrice3);
		nbt.setDouble("buyPrice4", buyPrice4);
		nbt.setDouble("sellPrice4", sellPrice4);
		
		if (CityConfig.debugMode) {
			System.out.println("Description packet sent. Owner name and BP1: " + ownerName + ", " + buyPrice1);
		}
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
		
		return new SPacketUpdateTileEntity();
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.func_148857_g();
		
		NBTTagList tagList = nbt.getTagList("ClientAShelfInv", 10);
		this.items = new ItemStack[getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if ((slot >= 0) && (slot < this.items.length)) {
				this.items[slot] = new ItemStack(tag);
			}
		}
		this.ownerName = nbt.getString("ownerName");
		this.buyPrice1 = nbt.getDouble("buyPrice1");
		this.sellPrice1 = nbt.getDouble("sellPrice1");
		this.buyPrice2 = nbt.getDouble("buyPrice2");
		this.sellPrice2 = nbt.getDouble("sellPrice2");
		this.buyPrice3 = nbt.getDouble("buyPrice3");
		this.sellPrice3 = nbt.getDouble("sellPrice3");
		this.buyPrice4 = nbt.getDouble("buyPrice4");
		this.sellPrice4 = nbt.getDouble("sellPrice4");
		
		if (CityConfig.debugMode) {
			System.out.println("Description packet RECIEVED!. Owner name and BP1: " + nbt.getString("ownerName") + ", " + nbt.getDouble("buyPrice1"));
		}
		
		this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
	}
	
	//Selling items TO the player
	public void sellItem(int slotId, EntityPlayer entityPlayer) {
		ChatStyle red = new ChatStyle().setColor(EnumChatFormatting.RED);
		ChatStyle green = new ChatStyle().setColor(EnumChatFormatting.GREEN);
		
		double itemCost = 0;
		if (slotId == 1) {
			itemCost = buyPrice1;
		}
		if (slotId == 2) {
			itemCost = buyPrice2;
		}
		if (slotId == 3) {
			itemCost = buyPrice3;
		}
		if (slotId == 4) {
			itemCost = buyPrice4;
		}
		double invCash = econ.getInventoryCash(entityPlayer);
		boolean full = false; //For checking they still have space AFTER getting their change (as change is given before item)
		boolean hasSpace = econ.inventoryHasSpace(entityPlayer, getStackInSlot(slotId - 1));
		ItemStack item = getStackInSlot(slotId - 1);
		
		if (invCash >= itemCost && hasSpace) {
			//Two birds, one stone. Charges the player for us, then tells us how much they paid so we can calculate change.
			double paidAmount = econ.findCashInInventoryWithChange(entityPlayer, itemCost); //Complex code to charge the player's inventory
			if (paidAmount < 0) {
				return;
			} else {
				if (econ.inventoryHasSpace(entityPlayer, getStackInSlot(slotId - 1))) {
					entityPlayer.inventory.addItemStackToInventory(item.copy());
				} else {
					worldObj.spawnEntityInWorld(new EntityItem(worldObj, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, item.copy()));
					full = true;
				}
				System.out.println(entityPlayer.getDisplayName() + " bought " + item.stackSize + " " + item.getDisplayName() + " from the server, for $" + econ.formatBalance(itemCost));
				entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You bought " + EnumChatFormatting.GOLD + item.stackSize + " " + item.getDisplayName() + EnumChatFormatting.GREEN + " from the server for " + EnumChatFormatting.DARK_GREEN + "$" + econ.formatBalance(itemCost) + "!"));
				if (full) {
					entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "You didn't have enough room in your inventory, so the item was dropped."));
				}
			}
		}
		if (invCash < itemCost) {
			//Pay by card
			if (econ.getBalance(entityPlayer) >= itemCost && hasSpace && CityConfig.allowCardPurchases) {
				if (econ.hasOwnCard(entityPlayer)) {
					if (econ.payBalanceByCard(entityPlayer, itemCost)) {
						if (econ.inventoryHasSpace(entityPlayer, getStackInSlot(slotId - 1))) {
							entityPlayer.inventory.addItemStackToInventory(item.copy());
						} else {
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, item.copy()));
							full = true;
						}
						
						entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You bought " + EnumChatFormatting.GOLD + item.stackSize + " " + item.getDisplayName() + EnumChatFormatting.GREEN + " from the server for " + EnumChatFormatting.DARK_GREEN + "$" + econ.formatBalance(itemCost) + "!"));
						entityPlayer.addChatComponentMessage(new ChatComponentText("You didn't have enough money with you, so it was charged to your bank account instead. Your remaining bank balance is \u00A76$" + econ.formatBalance(econ.getBalance(entityPlayer))).setChatStyle(green));
						if (full) {
							entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "You didn't have enough room in your inventory, so the item was dropped."));
						}
					}
				}
			} else {
				if (hasSpace) {
					entityPlayer.addChatComponentMessage(new ChatComponentText("You do not have enough money to do that! Next time, why not pay by card?").setChatStyle(red));
				}
			}
		}
		if (!hasSpace) {
			entityPlayer.addChatComponentMessage(new ChatComponentText("You do not have enough free inventory space to buy that!").setChatStyle(red));
		}
	}
	
	public boolean hasOwnCard(EntityPlayer player) {
		for (int i = player.inventory.getSizeInventory(); i >= 0; --i) {
			ItemStack item = player.inventory.getStackInSlot(i);
			if (item.getItem() == CoreItems.debitCardNew) {
				return (DebitCardItem.checkCardOwner(player, item).equalsIgnoreCase(player.getUniqueID().toString()));
			}
		}
		return false;
	}
	
	public boolean compareStackForOreDict(ItemStack stack, ItemStack item) {
		int[] stackOD = OreDictionary.getOreIDs(stack);
		int[] itemOD = OreDictionary.getOreIDs(item);
		
		if (stackOD != null  && itemOD != null) {
			for(int i = 0; i < stackOD.length; i++) {
				for (int j = 0; j < itemOD.length; j++) {
					if (stackOD[i] == itemOD[j]) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//Buying items FROM the player
	public void buyItem(int slotId, EntityPlayer player) {
		ItemStack item = getStackInSlot(slotId - 1);
		int ss = item.stackSize;
		int invQty = 0;
		double itemCost = 0;
		
		if (slotId == 1) {
			itemCost = sellPrice1;
		}
		if (slotId == 2) {
			itemCost = sellPrice2;
		}
		if (slotId == 3) {
			itemCost = sellPrice3;
		}
		if (slotId == 4) {
			itemCost = sellPrice4;
		}
		
		for (int x = player.inventory.getSizeInventory() - 1; x >= 0; -- x) {
			ItemStack stack = player.inventory.getStackInSlot(x);
			if (stack != null) {
				if ((stack.getItem() == item.getItem() && stack.getItemDamage() == item.getItemDamage()) || compareStackForOreDict(item, stack)) {
					invQty = invQty + stack.stackSize;
					if (invQty >= ss) {
						invQty = ss;
					}
				}
			}
		}
		if (invQty >= ss) {
			int remain = ss;
			for (int x = player.inventory.getSizeInventory() - 1; x >= 0; -- x) {
				ItemStack stack = player.inventory.getStackInSlot(x);
				if (stack != null) {
					if (remain > 0) {
						if (stack.getItem() == item.getItem() && stack.getItemDamage() == item.getItemDamage() || compareStackForOreDict(item, stack)) {
							if (stack.stackSize >= remain) {
								player.inventory.decrStackSize(x, remain);
								econ.giveChange(itemCost, 0, player);
								remain = 0;
								System.out.println(player.getDisplayName() + " sold " + item.stackSize + " " + item.getDisplayName() + " to the server, for $" + econ.formatBalance(itemCost));
								player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You sold " + EnumChatFormatting.GOLD + ss + " " + item.getDisplayName() + EnumChatFormatting.GREEN + " to the server for " + EnumChatFormatting.DARK_GREEN + "$" + econ.formatBalance(itemCost) + "!"));
							} else {
								remain = remain - stack.stackSize;
								player.inventory.setInventorySlotContents(x, null);
							}
						}
					}
				}
			}
		} else {
			player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "You don't have enough of these to sell right now."));
		}
		((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
	}

	
	public TileEntityAdminShop() {
		items = new ItemStack[4]; //Amount of stacks
	}

	@Override
	public int getSizeInventory() {
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return items[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int amount) {
		ItemStack itemStack = getStackInSlot(i);
		
		if (itemStack != null) {
			if (itemStack.stackSize <= amount) {
				setInventorySlotContents(i, null);
			} else {
				itemStack = itemStack.splitStack(amount);
			}
		}
		return itemStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack itemStack = getStackInSlot(i);
		setInventorySlotContents(i, null);
		return itemStack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		if (isItemValidForSlot(i, itemStack)) {
			items[i] = itemStack;
		
			if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
				itemStack.stackSize = getInventoryStackLimit();
			}
		
			//onInventoryChanged();
		}
	}

	//Max size of stacks placed inside.
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	//Checks if the player can use it.
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		return entityPlayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
	}

	//Checks that the item is valid. For shelves, use instanceof to see if it's item or block.
	@Override
	public boolean isItemValidForSlot(int i, ItemStack item) {
		ItemStack slot1 = getStackInSlot(0);
		ItemStack slot2 = getStackInSlot(1);
		ItemStack slot3 = getStackInSlot(2);
		ItemStack slot4 = getStackInSlot(3);
		if (item != null) {
			if (i > 3 && i <= 39) {
				return item.getItem() == slot1.getItem();
			}
			if (i > 39 && i <= 75) {
				return item.getItem() == slot2.getItem();
			}
			if (i > 75 && i <= 111) {
				return item.getItem() == slot3.getItem();
			}
			if (i > 111 && i <= 147) {
				return item.getItem() == slot4.getItem();
			}
			if (i > 147 && i <= 184) {
				if (item.getItem() instanceof ItemCoin || item.getItem() instanceof ItemNote) {
					return true;
				} else {
					return false;
				}
			} if (i > 184) {
				return false;
			}
		}
		return true;
	}*/
}