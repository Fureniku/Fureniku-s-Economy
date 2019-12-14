package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.management.StockChestContainer;
import com.silvaniastudios.econ.api.store.management.StockChestEntity;
import com.silvaniastudios.econ.api.store.management.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.core.blocks.ATMContainer;
import com.silvaniastudios.econ.core.blocks.ATMEntity;
import com.silvaniastudios.econ.core.blocks.shop.ShelvesFullEntity;
import com.silvaniastudios.econ.core.blocks.shop.containers.ShelvesFullContainer;
import com.silvaniastudios.econ.core.client.gui.GuiATM;
import com.silvaniastudios.econ.core.client.gui.GuiShelvesFull;
import com.silvaniastudios.econ.core.client.gui.GuiStockChest;
import com.silvaniastudios.econ.core.client.gui.storemanager.GuiStoreManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
    	}
    	return null;
    }
}