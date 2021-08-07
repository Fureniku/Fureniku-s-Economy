package com.fureniku.econ.capability.customer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CustomerStorage implements IStorage<ICustomer> {

	@Override
	public NBTBase writeNBT(Capability<ICustomer> capability, ICustomer instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", instance.getCartPos().getX());
		nbt.setInteger("y", instance.getCartPos().getY());
		nbt.setInteger("z", instance.getCartPos().getZ());
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICustomer> capability, ICustomer instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");
		instance.setCartPos(new BlockPos(x,y,z));
	}

}
