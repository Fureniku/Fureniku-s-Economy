package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.StoreManagerEntity;
import com.silvaniastudios.econ.core.blocks.ATMContainer;
import com.silvaniastudios.econ.core.blocks.ATMEntity;
import com.silvaniastudios.econ.core.client.gui.GuiATM;

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
			if (id == EconConstants.Gui.STORE_MANAGER) {
				if (tileEntity instanceof StoreManagerEntity) {
					return new StoreManagerContainer((StoreManagerEntity) tileEntity);
				}
			}
		} else { 
			FurenikusEconomy.log(3, "TE is null.");
		}
		return null;	
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    	FurenikusEconomy.log(3, "Getting clientr-side GUI element with ID " + id);
    	TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
    	if (tileEntity != null) {
    		if (id == EconConstants.Gui.ATM) {
    			ATMEntity te = (ATMEntity) tileEntity;
    			return new GuiATM(te, new ATMContainer(player.inventory, te));
    		}
    		if (id == EconConstants.Gui.STORE_MANAGER) {
    			//return new StoreManagerGui(player.inventory, (StoreManagerEntity) tileEntity);
    		}
    	}
    	return null;
    }
}