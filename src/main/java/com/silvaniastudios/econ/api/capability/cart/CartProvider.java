package com.silvaniastudios.econ.api.capability.cart;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CartProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ICart.class)
	public static final Capability<ICart> CART = null;

	private ICart instance = CART.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CART;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CART ? CART.<T> cast(this.instance) : null;
	}

	//We don't need to save NBT, as this inventory is effectively saved to memory only.
	//The inventory is never saved, as if a player is unloaded, the stock is returned to the shop.
	@Override
	public NBTBase serializeNBT() {
		return null;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {}

}
