package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.old.ContainerFloatingShelves;
import com.silvaniastudios.econ.api.store.old.ContainerStockChest;
import com.silvaniastudios.econ.api.store.old.GuiFloatingShelves;
import com.silvaniastudios.econ.api.store.old.GuiStockChest;
import com.silvaniastudios.econ.api.store.old.TileEntityFloatingShelves;
import com.silvaniastudios.econ.api.store.old.TileEntityStockChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if (tileEntity != null) {
			if (id == EconConstants.GUI.STORE_MANAGER) {
				if (tileEntity instanceof StoreManagerEntity) {
					return new StoreManagerContainer((StoreManagerEntity) tileEntity);
				}
			}
		}
		return null;	
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
    	if (tileEntity != null) {
    		if (id == EconConstants.GUI.STORE_MANAGER) {
    			
    		}
    	}
    	switch(id) {
    		case 1: {
    			if (tileEntity instanceof TileEntityFloatingShelves) {
    				return new GuiFloatingShelves(player.inventory, (TileEntityFloatingShelves) tileEntity);
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