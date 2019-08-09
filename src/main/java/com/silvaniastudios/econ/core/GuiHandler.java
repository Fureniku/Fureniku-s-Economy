package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.StoreManagerEntity;
import com.silvaniastudios.econ.core.client.gui.GuiATM;

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
			if (id == EconConstants.Gui.STORE_MANAGER) {
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
    		if (id == EconConstants.Gui.STORE_MANAGER) {
    			return new StoreManagerGui(player.inventory, (StoreManagerEntity) tileEntity);
    		}
    	}
    	
    	if (id == EconConstants.Gui.ATM) {
    		return new GuiATM();
    	}
    }
}