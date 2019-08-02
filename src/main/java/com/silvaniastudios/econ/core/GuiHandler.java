package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.store.container.old.ContainerAdminShop;
import com.silvaniastudios.econ.api.store.container.old.ContainerFloatingShelves;
import com.silvaniastudios.econ.api.store.container.old.ContainerStockChest;
import com.silvaniastudios.econ.api.store.container.old.GuiAdminShop;
import com.silvaniastudios.econ.api.store.container.old.GuiFloatingShelves;
import com.silvaniastudios.econ.api.store.container.old.GuiStockChest;
import com.silvaniastudios.econ.api.store.entity.TileEntityAdminShop;
import com.silvaniastudios.econ.api.store.entity.TileEntityFloatingShelves;
import com.silvaniastudios.econ.api.store.entity.TileEntityStockChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		switch(id) {
			case 1: {
				if(tileEntity instanceof TileEntityFloatingShelves) {
					return new ContainerFloatingShelves(player.inventory, (TileEntityFloatingShelves) tileEntity);
				}	
			}
			case 3: {
				if(tileEntity instanceof TileEntityAdminShop) {
					return new ContainerAdminShop(player.inventory, (TileEntityAdminShop) tileEntity);
				}	
			}
			case 4: {
				if(tileEntity instanceof TileEntityStockChest) {
					return new ContainerStockChest(player.inventory, (TileEntityStockChest) tileEntity);
				}
			}
		}
		return null;	
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
    	switch(id) {
    		case 1: {
    			if (tileEntity instanceof TileEntityFloatingShelves) {
    				return new GuiFloatingShelves(player.inventory, (TileEntityFloatingShelves) tileEntity);
    			}	
    		}
    		case 3: {
    			if (tileEntity instanceof TileEntityAdminShop) {
    				return new GuiAdminShop(player.inventory, (TileEntityAdminShop) tileEntity);
    			}	
    		}
    		case 4: {
    			if (tileEntity instanceof TileEntityStockChest) {
    				return new GuiStockChest(player.inventory, (TileEntityStockChest) tileEntity);
    			}
    		}
    	}
    	return null;
    }
}