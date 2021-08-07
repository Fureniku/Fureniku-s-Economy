package com.fureniku.econ.capability.customer;

import com.fureniku.econ.store.Customer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface ICustomer {
	
	public BlockPos getCartPos();
	public void setCartPos(BlockPos p);
	public Customer getCustomer(EntityPlayer player);
	public void clearCapability();

}
