package com.silvaniastudios.econ.api.capability.cart;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CartStorage implements IStorage<ICart> {

	//We actually don't need to save anything for this capability
	//But the game shouts at us if we don't.
	@Override
	public NBTBase writeNBT(Capability<ICart> capability, ICart instance, EnumFacing side) {
		return new NBTTagInt(0);
	}

	@Override
	public void readNBT(Capability<ICart> capability, ICart instance, EnumFacing side, NBTBase nbt) {
	}

}
