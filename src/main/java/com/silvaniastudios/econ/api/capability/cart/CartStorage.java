package com.silvaniastudios.econ.api.capability.cart;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CartStorage implements IStorage<ICart> {

	@Override
	public NBTBase writeNBT(Capability<ICart> capability, ICart instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<ICart> capability, ICart instance, EnumFacing side, NBTBase nbt) {
	}

}
