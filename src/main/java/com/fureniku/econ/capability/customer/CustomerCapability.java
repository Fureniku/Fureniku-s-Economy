package com.fureniku.econ.capability.customer;

import com.fureniku.econ.store.Customer;
import com.fureniku.econ.store.management.CartDispenserEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class CustomerCapability implements ICustomer {
	
	private static BlockPos pos;

	@Override
	public BlockPos getCartPos() {
		return pos == null ? BlockPos.ORIGIN : pos;
	}

	@Override
	public void setCartPos(BlockPos p) {
		pos = p;
	}
	
	@Override
	public Customer getCustomer(EntityPlayer player) {
		if (pos == null) {
			System.out.println("pos is null");
		}
		if (pos != null && pos.getY() > 0) {
			//System.out.println("pos is valid");
			TileEntity te = player.world.getTileEntity(pos);
			if (te instanceof CartDispenserEntity) {
				//System.out.println("TE is valid, return customer");
				CartDispenserEntity cart = (CartDispenserEntity) te;
				
				return cart.getManager().getCustomerByUuid(player.getCachedUniqueIdString());
			}
		} else {
			System.out.println("pos is origin");
		}
		return null;
	}
	
	@Override
	public void clearCapability() {
		pos = BlockPos.ORIGIN;
	}
}
