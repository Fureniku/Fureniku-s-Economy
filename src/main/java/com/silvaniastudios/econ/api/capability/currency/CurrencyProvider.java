package com.silvaniastudios.econ.api.capability.currency;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CurrencyProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ICurrency.class)
	public static final Capability<ICurrency> CURRENCY = null;

	private ICurrency instance = CURRENCY.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CURRENCY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CURRENCY ? CURRENCY.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CURRENCY.getStorage().writeNBT(CURRENCY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CURRENCY.getStorage().readNBT(CURRENCY, this.instance, null, nbt);
	}

}
