package com.fureniku.econ;

import com.fureniku.econ.blocks.ATMContainer;
import com.fureniku.econ.blocks.ATMEntity;
import com.fureniku.econ.blocks.shop.ShelvesFullEntity;
import com.fureniku.econ.blocks.shop.containers.ShelvesFullContainer;
import com.fureniku.econ.blocks.shop.containers.ShelvesFullContainerBuy;
import com.fureniku.econ.client.gui.GuiATM;
import com.fureniku.econ.client.gui.GuiBackToStock;
import com.fureniku.econ.client.gui.GuiStockChest;
import com.fureniku.econ.client.gui.cart.GuiCartDispenser;
import com.fureniku.econ.client.gui.shop.GuiShelvesFull;
import com.fureniku.econ.client.gui.shop.GuiShelvesFullBuy;
import com.fureniku.econ.client.gui.storemanager.GuiStoreManager;
import com.fureniku.econ.store.management.BackToStockChestEntity;
import com.fureniku.econ.store.management.BackToStockContainer;
import com.fureniku.econ.store.management.CartDispenserContainer;
import com.fureniku.econ.store.management.CartDispenserEntity;
import com.fureniku.econ.store.management.StockChestContainer;
import com.fureniku.econ.store.management.StockChestEntity;
import com.fureniku.econ.store.management.StoreManagerContainer;
import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		FurenikusEconomy.log(3, "Getting server-side GUI element with ID " + id);
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity != null) {
			if (id == EconConstants.Gui.ATM) {
				if (tileEntity instanceof ATMEntity) {
					return new ATMContainer(player.inventory, (ATMEntity) tileEntity);
				}
			}
			if (id == EconConstants.Gui.STORE_MANAGER_OWNER) {
				if (tileEntity instanceof StoreManagerEntity) {
					return new StoreManagerContainer(player.inventory, (StoreManagerEntity) tileEntity);
				}
			}
			if (id == EconConstants.Gui.STOCK_CHEST_OWNER) {
				if (tileEntity instanceof StockChestEntity) {
					return new StockChestContainer(player.inventory, (StockChestEntity) tileEntity);
				}
			}
			if (id == EconConstants.Gui.SHOP_SHELVES_FULL_OWNER) {
				if (tileEntity instanceof ShelvesFullEntity) {
					return new ShelvesFullContainer(player.inventory, (ShelvesFullEntity) tileEntity);
				}
			}
			
			if (id == EconConstants.Gui.SHOP_SHELVES_FULL_NOT_OWNER) {
				if (tileEntity instanceof ShelvesFullEntity) {
					ShelvesFullEntity shop = (ShelvesFullEntity) tileEntity;
					if (shop.hasManager()) {
						return new ShelvesFullContainerBuy(player.inventory, shop);
					} else {
						player.sendMessage(new TextComponentTranslation("econ.shop.closed"));
					}
				}
			}
			if (id == EconConstants.Gui.BACK_TO_STOCK_CHEST_OWNER) {
				if (tileEntity instanceof BackToStockChestEntity) {
					return new BackToStockContainer(player.inventory, (BackToStockChestEntity) tileEntity);
				}
			}
			if (tileEntity instanceof CartDispenserEntity) {
				CartDispenserEntity cart = (CartDispenserEntity) tileEntity;
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_1) {
					return new CartDispenserContainer(cart, 0);
				}
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_2) {
					return new CartDispenserContainer(cart, 1);
				}
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_3) {
					return new CartDispenserContainer(cart, 2);
				}
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_4) {
					return new CartDispenserContainer(cart, 3);
				}
				
				if (id >= EconConstants.Gui.CART_DISPENSER_NOT_OWNER_1 && id <= EconConstants.Gui.CART_DISPENSER_NOT_OWNER_4) {
					int i = Math.abs(EconConstants.Gui.CART_DISPENSER_NOT_OWNER_1 - id);
					return new CartDispenserContainer(cart, i);
				}
			}
			
			
		} else { 
			FurenikusEconomy.log(3, "TE is null.");
		}
		return null;	
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	FurenikusEconomy.log(3, "Getting client-side GUI element with ID " + id);
    	TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
    	if (tileEntity != null) {
    		if (id == EconConstants.Gui.ATM) {
    			ATMEntity te = (ATMEntity) tileEntity;
    			return new GuiATM(te, new ATMContainer(player.inventory, te));
    		}
    		if (id == EconConstants.Gui.STORE_MANAGER_OWNER) {
    			StoreManagerEntity te = (StoreManagerEntity) tileEntity;
    			return new GuiStoreManager(te, new StoreManagerContainer(player.inventory, te));
    		}
    		if (id == EconConstants.Gui.STOCK_CHEST_OWNER) {
    			StockChestEntity te = (StockChestEntity) tileEntity;
    			return new GuiStockChest(te, new StockChestContainer(player.inventory, te));
    		}
    		if (id == EconConstants.Gui.SHOP_SHELVES_FULL_OWNER) {
    			ShelvesFullEntity te = (ShelvesFullEntity) tileEntity;
    			return new GuiShelvesFull(te, new ShelvesFullContainer(player.inventory, te));
    		}
    		if (id == EconConstants.Gui.SHOP_SHELVES_FULL_NOT_OWNER) {
    			System.out.println("Client: open shop for non-owner");
    			ShelvesFullEntity te = (ShelvesFullEntity) tileEntity;
    			return new GuiShelvesFullBuy(te, new ShelvesFullContainerBuy(player.inventory, te));
    		}
    		if (id == EconConstants.Gui.BACK_TO_STOCK_CHEST_OWNER) {
    			BackToStockChestEntity te = (BackToStockChestEntity) tileEntity;
    			return new GuiBackToStock(te, new BackToStockContainer(player.inventory, te));
    		}
    		if (tileEntity instanceof CartDispenserEntity) {
    			System.out.println("Client opening cart dispenser");
				CartDispenserEntity cart = (CartDispenserEntity) tileEntity;
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_1) {
					return new GuiCartDispenser(cart, new CartDispenserContainer(cart, 0), 0, true);
				}
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_2) {
					return new GuiCartDispenser(cart, new CartDispenserContainer(cart, 1), 1, true);
				}
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_3) {
					return new GuiCartDispenser(cart, new CartDispenserContainer(cart, 2), 2, true);
				}
				if (id == EconConstants.Gui.CART_DISPENSER_OWNER_4) {
					return new GuiCartDispenser(cart, new CartDispenserContainer(cart, 3), 3, true);
				}
				
				if (id >= EconConstants.Gui.CART_DISPENSER_NOT_OWNER_1 && id <= EconConstants.Gui.CART_DISPENSER_NOT_OWNER_4) {
					int i = Math.abs(EconConstants.Gui.CART_DISPENSER_NOT_OWNER_1 - id);
					return new GuiCartDispenser(cart, new CartDispenserContainer(cart, i), i, false);
				}
			}
    	}
    	return null;
    }
}