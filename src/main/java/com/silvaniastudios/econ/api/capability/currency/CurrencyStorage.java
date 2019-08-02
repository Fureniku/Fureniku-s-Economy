package com.silvaniastudios.econ.api.capability.currency;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CurrencyStorage implements IStorage<CurrencyCapability> {

	@Override
	public NBTBase writeNBT(Capability<CurrencyCapability> capability, CurrencyCapability instance, EnumFacing side) {
		return new NBTTagLong(instance.getMoney());
	}

	@Override
	public void readNBT(Capability<CurrencyCapability> capability, CurrencyCapability instance, EnumFacing side, NBTBase nbt) {
		instance.setMoney(((NBTPrimitive) nbt).getLong());
	}

}
