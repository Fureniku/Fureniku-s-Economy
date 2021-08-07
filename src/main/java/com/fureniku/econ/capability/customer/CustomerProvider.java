package com.fureniku.econ.capability.customer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CustomerProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ICustomer.class)
	public static final Capability<ICustomer> CUSTOMER = null;

	private ICustomer instance = CUSTOMER.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CUSTOMER;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CUSTOMER ? CUSTOMER.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CUSTOMER.getStorage().writeNBT(CUSTOMER, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CUSTOMER.getStorage().readNBT(CUSTOMER, this.instance, null, nbt);
	}

}
