package com.silvaniastudios.econ.api.store.old;

import com.silvaniastudios.econ.api.EconUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFloatingShelves extends TileEntity implements IInventory {

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
	public String ownerUuid;
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
	public boolean matchNBT;
	
	public int stockXPos;
	public int stockYPos;
	public int stockZPos;
	
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
		nbt.setString("ownerUuid", ownerUuid + "");
		nbt.setDouble("buyPrice1", buyPrice1);
		nbt.setDouble("sellPrice1", sellPrice1);
		nbt.setDouble("buyPrice2", buyPrice2);
		nbt.setDouble("sellPrice2", sellPrice2);
		nbt.setDouble("buyPrice3", buyPrice3);
		nbt.setDouble("sellPrice3", sellPrice3);
		nbt.setDouble("buyPrice4", buyPrice4);
		nbt.setDouble("sellPrice4", sellPrice4);
		nbt.setBoolean("matchNBT", matchNBT);
		
		nbt.setInteger("stockXPos", stockXPos);
		nbt.setInteger("stockYPos", stockYPos);
		nbt.setInteger("stockZPos", stockZPos);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        this.items = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.items.length)
            {
                this.items[j] = new ItemStack(nbttagcompound1);
            }
        }
		this.ownerName = nbt.getString("ownerName");
		this.ownerUuid = nbt.getString("ownerUuid");
		this.buyPrice1 = nbt.getDouble("buyPrice1");
		this.sellPrice1 = nbt.getDouble("sellPrice1");
		this.buyPrice2 = nbt.getDouble("buyPrice2");
		this.sellPrice2 = nbt.getDouble("sellPrice2");
		this.buyPrice3 = nbt.getDouble("buyPrice3");
		this.sellPrice3 = nbt.getDouble("sellPrice3");
		this.buyPrice4 = nbt.getDouble("buyPrice4");
		this.sellPrice4 = nbt.getDouble("sellPrice4");
		this.matchNBT = nbt.getBoolean("matchNBT");
		
		this.stockXPos = nbt.getInteger("stockXPos");
		this.stockYPos = nbt.getInteger("stockYPos");
		this.stockZPos = nbt.getInteger("stockZPos");
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
		nbt.setBoolean("matchNBT", matchNBT);
		
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
		
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
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
		this.matchNBT = nbt.getBoolean("matchNBT");

		this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
	}
	
	public ItemStack findStockItem(TileEntityStockChest stockChest, ItemStack item, String owner) {
		if (stockChest.ownerName.equalsIgnoreCase(ownerName)) {
			econ.debug("Checking stock chest for stock. Chest found.");
			for (int i = 0; i < stockChest.invSize - 1; i++) {
				ItemStack stock = stockChest.getStackInSlot(i);
				if (stock != null && item != null) {
					ItemStack itemCopy = item.copy();
					ItemStack stockCopy = stock.copy();
					if (compareItemStacks(itemCopy, stockCopy)) {
						econ.debug("Stock match found");
						if (stock.stackSize >= item.stackSize) {
							econ.debug("And there is enough in the stack!");
							int amt = item.stackSize;
							ItemStack retrievedItems = stockChest.decrStackSize(i, amt);
							//stock = stock.splitStack(amt);
							if (stock.stackSize == 0) {
								setInventorySlotContents(i, null);
							}
							stockChest.markDirty();
							stockChest.closeInventory();
							return retrievedItems;
						}
					}
				}
			}
		}
		return null;
	}
	
	public boolean compareItemStacks(ItemStack item1, ItemStack item2) {
		item1.stackSize = 1;
		item2.stackSize = 1;
		
		econ.debug("Comparing stacks.");
		econ.debug("Stack 1 is " + item1.getItem());
		econ.debug("Stack 2 is " + item2.getItem());
			
		if (item1.getItem().equals(item2.getItem()) || compareStackForOreDict(item1, item2)) {
			econ.debug("Stacks match and are of the same oredict entry");
			if (item1.getItemDamage() == item2.getItemDamage()) {
				econ.debug("Damage is correct");
				if (matchNBT) {
					econ.debug("NBT check enabled. Checking NBT...");
					NBTTagCompound nbt1 = item1.getTagCompound();
					NBTTagCompound nbt2 = item2.getTagCompound();
					if (nbt1.equals(nbt2)) {
						return true;
					} else {
						return false;
					}
				}
				econ.debug("Items match on ID, metadata and NBT");
				return true;
			}
		}
		return false;
	}
	
	public boolean compareStackForOreDict(ItemStack stack, ItemStack item) {
		econ.debug("Checking oredict for a match");
		int[] stackOD = OreDictionary.getOreIDs(stack);
		int[] itemOD = OreDictionary.getOreIDs(item);
		
		if (stackOD != null  && itemOD != null) {
			for(int i = 0; i < stackOD.length; i++) {
				for (int j = 0; j < itemOD.length; j++) {
					if (stackOD[i] == itemOD[j]) {
						econ.debug("Oredict match found!");
						return true;
					}
				}
			}
		}
		econ.debug("Oredict mismatch!");
		econ.debug("Oredict 1: " + stackOD);
		econ.debug("Oredict 2: " + itemOD);
		return false;
	}
	
	//Selling items TO the player
	public void sellItem(int slotId, EntityPlayer entityPlayer) {
		World world = entityPlayer.worldObj;
		TileEntity te = world.getTileEntity(stockXPos, stockYPos, stockZPos);
		TileEntityStockChest stockChest = null;
		EntityPlayer storeOwner = null;
		EnumChatFormatting gold = EnumChatFormatting.GOLD;
		EnumChatFormatting green = EnumChatFormatting.GREEN;
		EnumChatFormatting red = EnumChatFormatting.RED;
		EnumChatFormatting darkGreen = EnumChatFormatting.DARK_GREEN;
		boolean full = false;
		
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getDisplayName().equalsIgnoreCase(ownerName)) {
				storeOwner = players.get(i);
			}
		}
		
		if (te != null && te instanceof TileEntityStockChest) {
			if (CityConfig.debugMode) {
				System.out.println("Stock chest found");
			}
			stockChest = (TileEntityStockChest) te;
		}
				
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
		boolean hasSpace = econ.inventoryHasSpace(entityPlayer, getStackInSlot(slotId - 1));
		ItemStack item = getStackInSlot(slotId - 1);
		if (item == null) {
			return;
		}
		
		if (stockChest != null)
			if (stockChest.selling) {
				if (invCash >= itemCost && hasSpace) {
					ItemStack stockItem = findStockItem(stockChest, item.copy(), ownerName);
					if (stockItem != null) {
					//Two birds, one stone. Charges the player for us, then tells us how much they paid so we can calculate change.
						if (CityConfig.debugMode) {
							System.out.println("Attempting to charge player");
						}
						double paidAmount = econ.findCashInInventoryWithChange(entityPlayer, itemCost); //Complex code to charge the player's inventory
						if (paidAmount < 0) {
							if (CityConfig.debugMode) {
								System.out.println("No monies.");
							}
							return;
						} else {
							if (econ.inventoryHasSpace(entityPlayer, getStackInSlot(slotId - 1))) {
								if (CityConfig.debugMode) {
									System.out.println("Player still has space");
								}
								entityPlayer.inventory.addItemStackToInventory(stockItem);
							} else {
								if (CityConfig.debugMode) {
									System.out.println("Player DOESN'T have space, dropping item.");
								}
								full = true;
								worldObj.spawnEntityInWorld(new EntityItem(worldObj, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, stockItem));
							}
							System.out.println(entityPlayer.getDisplayName() + " bought " + item.stackSize + " " + item.getDisplayName() + " from " + ownerName + " for $" + econ.formatBalance(itemCost));
				
							if (storeOwner != null) {
								storeOwner.addChatComponentMessage(new ChatComponentText(gold + entityPlayer.getDisplayName() + green + " bought " + gold + item.stackSize + " " + item.getDisplayName() + green + " from you for " + darkGreen + "$" + econ.formatBalance(itemCost) + "!"));
							}
							
							econ.depositToAccountViaUUID(ownerUuid, itemCost);
							entityPlayer.addChatComponentMessage(new ChatComponentText(green + "You bought " + gold + item.stackSize + " " + item.getDisplayName() + green + " from " + gold + ownerName + green + " for " + darkGreen + "$" + econ.formatBalance(itemCost) + "!"));
							if (full) {
								entityPlayer.addChatComponentMessage(new ChatComponentText(red + "You didn't have enough room in your inventory, so the item was dropped."));
							}
						}
					} else {
						entityPlayer.addChatComponentMessage(new ChatComponentText(red + item.getDisplayName() + " is currently out of stock."));
		
						if (storeOwner != null) {
							storeOwner.addChatComponentMessage(new ChatComponentText(red + "Your " + item.getDisplayName() + " at " + gold + xCoord + ", " + yCoord + ", " + zCoord + red + " is currently out of stock."));
						}
					}
				} else if (invCash < itemCost) {
					if (econ.getBalance(entityPlayer) >= itemCost && hasSpace && CityConfig.allowCardPurchases) {
						if (econ.hasOwnCard(entityPlayer)) {
							ItemStack stockItem = findStockItem(stockChest, item.copy(), ownerName);
							if (stockItem != null) {
								if (econ.payBalanceByCard(entityPlayer, itemCost)) {
									if (econ.inventoryHasSpace(entityPlayer, getStackInSlot(slotId - 1))) {
										entityPlayer.inventory.addItemStackToInventory(stockItem);
									} else {
										worldObj.spawnEntityInWorld(new EntityItem(worldObj, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, stockItem));
										full = true;
									}
									if (storeOwner != null) {
										storeOwner.addChatComponentMessage(new ChatComponentText(gold + entityPlayer.getDisplayName() + green + " bought " + gold + item.stackSize + " " + item.getDisplayName() + green + " from you for " + darkGreen + "$" + econ.formatBalance(itemCost) + "!"));
									}
									
									econ.depositToAccountViaUUID(ownerUuid, itemCost);
									
									entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You bought " + EnumChatFormatting.GOLD + item.stackSize + " " + item.getDisplayName() + EnumChatFormatting.GREEN + " from the server for " + EnumChatFormatting.DARK_GREEN + "$" + econ.formatBalance(itemCost) + "!"));
									entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "You have paid by card. Your remaining bank balance is $" + EnumChatFormatting.GOLD + econ.formatBalance(econ.getBalance(entityPlayer))));
									if (full) {
										entityPlayer.addChatComponentMessage(new ChatComponentText(red + "You didn't have enough room in your inventory, so the item was dropped."));
									}
								}
							} else {
								entityPlayer.addChatComponentMessage(new ChatComponentText(red + item.getDisplayName() + " is currently out of stock."));
								
								if (storeOwner != null) {
									storeOwner.addChatComponentMessage(new ChatComponentText(red + "Your " + item.getDisplayName() + " at " + gold + xCoord + ", " + yCoord + ", " + zCoord + red + " is currently out of stock."));
								}
							}
						}
					} else {
						if (hasSpace) {
							entityPlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "You do not have enough money to do that! Next time, why not pay by card?"));
						}
					}
				}
			} else {
				entityPlayer.addChatComponentMessage(new ChatComponentText(red + "This shop is not currently selling stock."));
			}
		if (!hasSpace) {
			entityPlayer.addChatComponentMessage(new ChatComponentText(red + "You do not have enough free inventory space to buy that!"));
		}
		if (stockChest == null) {
			entityPlayer.addChatComponentMessage(new ChatComponentText(red + "This shop is currently closed."));
			if (storeOwner != null) {
				storeOwner.addChatComponentMessage(new ChatComponentText(red + "Your shop at " + gold + xCoord + ", " + yCoord + ", " + zCoord + red + " is not assigned to a stock chest."));
			}
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
	
	//Buying items FROM the player
	public void buyItem(int slotId, EntityPlayer entityPlayer) {
		World world = entityPlayer.worldObj;
		TileEntity te = world.getTileEntity(stockXPos, stockYPos, stockZPos);
		TileEntityStockChest stockChest = null;
		EntityPlayer storeOwner = null;
		EnumChatFormatting gold = EnumChatFormatting.GOLD;
		EnumChatFormatting green = EnumChatFormatting.GREEN;
		EnumChatFormatting red = EnumChatFormatting.RED;
		EnumChatFormatting darkGreen = EnumChatFormatting.DARK_GREEN;
		
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getDisplayName().equalsIgnoreCase(ownerName)) {
				storeOwner = players.get(i);
			}
		}
		
		if (te != null && te instanceof TileEntityStockChest) {
			econ.debug("Stock chest found");
			stockChest = (TileEntityStockChest) te;
		}
		
		ItemStack shopItem = getStackInSlot(slotId - 1);
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
		if (stockChest != null) {
			if (stockChest.buying) {
				if (stockChest.buyFundLimit >= itemCost || stockChest.buyFundLimit <= -1) {
					if (econ.getBalanceViaUUID(ownerUuid) >= itemCost) {
						for (int x = entityPlayer.inventory.getSizeInventory() - 1; x >= 0; x--) {
							ItemStack playerInvStack = entityPlayer.inventory.getStackInSlot(x);
							if (playerInvStack != null) {
								if ((playerInvStack.getItem() == shopItem.getItem() && playerInvStack.getItemDamage() == shopItem.getItemDamage()) || compareStackForOreDict(playerInvStack, shopItem)) {
									invQty = invQty + playerInvStack.stackSize;
									if (invQty >= shopItem.stackSize) {
										invQty = shopItem.stackSize;
									}
								}
							}
						}
						//Player has enough/more than the shop wants.
						if (invQty >= shopItem.stackSize) {
							int insertSlot = findEmptySlot(shopItem, stockChest); //Find a slot to put it in.
							if (insertSlot >= 0) { //Only true if there's a slot, no slots free = -1
								int remain = shopItem.stackSize; //How many we want in total (in case taking from multiple stacks of player inv)
								for (int x = entityPlayer.inventory.getSizeInventory() - 1; x >= 0; x--) {
									ItemStack playerInvStack = entityPlayer.inventory.getStackInSlot(x);
									if (playerInvStack != null) { //Player has items
										if (remain > 0) { //We still need to take more items.
											if ((playerInvStack.getItem() == shopItem.getItem() && playerInvStack.getItemDamage() == shopItem.getItemDamage()) || compareStackForOreDict(playerInvStack, shopItem)) { //The items are the same.
												econ.debug("[Selling Item] Compare check has passed");
												if (playerInvStack.stackSize >= remain) { //A single stack is greater than or equal to total amount needed.
													econ.debug("[Selling Item] player stack >= remain. Stack: " + playerInvStack.stackSize + ", remain: " + remain);
													if (stockChest.getStackInSlot(insertSlot) != null) { //There's an identical item in the slot.
														econ.debug("[Selling Item] Merging with a stack");
														int size = stockChest.getStackInSlot(insertSlot).stackSize; //Total stacksize.
														ItemStack insertItem = playerInvStack.copy(); //Copy the item from the player inventory.
														//playerInvStack.stackSize -= remain;
														//if (playerInvStack.stackSize <= 0) {
															//entityPlayer.inventory.setInventorySlotContents(x, null);
														//}
														
														insertItem.stackSize = remain + size; //We already checked they have enough. Combine with size.
														if (insertItem.stackSize > insertItem.getMaxStackSize()) {
															ItemStack secondStack = insertItem.copy(); //If new stack and existing one are too big together,
															secondStack.stackSize = insertItem.stackSize - insertItem.getMaxStackSize(); //Make a new second stack
															insertItem.stackSize = insertItem.getMaxStackSize(); //And put that in the next available slot.
															stockChest.setInventorySlotContents(insertSlot, insertItem);
															stockChest.setInventorySlotContents(findEmptySlot(shopItem, stockChest), secondStack);
														} else {
															stockChest.setInventorySlotContents(insertSlot, insertItem);
														}
													} else {
														stockChest.setInventorySlotContents(insertSlot, shopItem);
														econ.debug("[Selling Item] Creating a new stack");
													}
													econ.debug("[Selling Item] Decreasing player's stack by " + remain);
													entityPlayer.inventory.decrStackSize(x, remain);
													econ.debug("[Selling Item] Stack size is now " + playerInvStack.stackSize);
													if (playerInvStack.stackSize <= 0) {
														econ.debug("[Selling Item] I just said, it's zero. NULLIFY!");
														entityPlayer.inventory.setInventorySlotContents(x, null);
													}
													econ.debug("[Selling Item] Charging owner, giving money to seller");
													econ.chargeAccountViaUUID(ownerUuid, itemCost);
													econ.giveChange(itemCost, 0, entityPlayer);
													stockChest.buyFundLimit = stockChest.buyFundLimit - itemCost;
													stockChest.markDirty();
													stockChest.getDescriptionPacket();
													remain = 0;
													System.out.println(entityPlayer.getDisplayName() + " sold " + shopItem.stackSize + " " + shopItem.getDisplayName() + " to " + ownerName + ", for $" + econ.formatBalance(itemCost));
													entityPlayer.addChatComponentMessage(new ChatComponentText(green + "You sold " + gold + shopItem.stackSize + " " + shopItem.getDisplayName() + green + " to " + ownerName + " for " + darkGreen + "$" + econ.formatBalance(itemCost) + "!"));
													if (storeOwner != null) {
														storeOwner.addChatComponentMessage(new ChatComponentText(green + entityPlayer.getDisplayName() + " has sold " + gold + shopItem.stackSize + " " + shopItem.getDisplayName() + green + " to you for " + darkGreen + "$" + econ.formatBalance(itemCost) + "!"));
													}
													setInventorySlotContents(slotId - 1, shopItem);
												} else {
													econ.debug("[Selling Item] NEED MORE! Removing this stack and looping back around.");
													remain = remain - playerInvStack.stackSize;
													entityPlayer.inventory.setInventorySlotContents(x, null);
												}
											}
										}
									}
								}
							} else {
								entityPlayer.addChatComponentMessage(new ChatComponentText(red + "This shop's stock is full! It can't take any more items."));
								if (storeOwner != null) {
									storeOwner.addChatComponentMessage(new ChatComponentText(red + "Your Stock Chest at " + gold + stockXPos + ", " + stockYPos + ", " + stockZPos + red + " has run out space for buying items."));
								}
							}
						} else {
							entityPlayer.addChatComponentMessage(new ChatComponentText(red + "You don't have enough of these to sell right now."));
						}
					} else {
						entityPlayer.addChatComponentMessage(new ChatComponentText(red + "This shop is out of funds. Please try later."));
						if (storeOwner != null) {
							storeOwner.addChatComponentMessage(new ChatComponentText(red + entityPlayer.getDisplayName() + " is trying to sell to you, but you don't have enough money in your bank."));
						}
					}
				} else {
					entityPlayer.addChatComponentMessage(new ChatComponentText(red + "This shop is out of funds. Please try later."));
					if (storeOwner != null) {
						storeOwner.addChatComponentMessage(new ChatComponentText(red + "Your Stock Chest at " + gold + stockXPos + ", " + stockYPos + ", " + stockZPos + red + " has run out of funds."));
					}
				}
			} else {
				entityPlayer.addChatComponentMessage(new ChatComponentText(red + "This shop is not currently buying stock."));
			}
		}
		((EntityPlayerMP) entityPlayer).sendContainerToPlayer(entityPlayer.inventoryContainer);
	}
	
	public int findEmptySlot(ItemStack item, TileEntityStockChest stockChest) {
		for (int i = 0; i < stockChest.getSizeInventory(); i++) {
			if (stockChest.getStackInSlot(i) != null) {
				ItemStack stockItem = stockChest.getStackInSlot(i);
				if (item.getItem().equals(stockItem.getItem())) {
					econ.debug("Finding empty slot in stock chest: Item matches");
					if (item.getItemDamage() == stockItem.getItemDamage()) {
						econ.debug("Finding empty slot in stock chest: Damage matches");
						int space = stockItem.getMaxStackSize() - stockItem.stackSize;
						if (item.stackTagCompound != null && stockItem.stackTagCompound != null && item.stackTagCompound.equals(stockItem.stackTagCompound)) {
							econ.debug("Finding empty slot in stock chest: NBT matches");
							if (space >= item.stackSize) {
								return i;
							}
						} else if (item.stackTagCompound == null && stockItem.stackTagCompound == null) {
							econ.debug("Finding empty slot in stock chest: NBT doesn't exist");
							if (space >= item.stackSize) {
								return i;
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < stockChest.getSizeInventory(); i++) {
			if (stockChest.getStackInSlot(i) == null) {
				return i;
			}
		}
		return -1;
	}

	
	public TileEntityFloatingShelves() {
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
	
	public void addMoneyToShopInventory(int i, ItemStack itemStack, double amount) {
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
	//Wouldn't work for owner only- if I did that, no one else would be able to interact (Maybe? Need to test that.)
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
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}*/
}