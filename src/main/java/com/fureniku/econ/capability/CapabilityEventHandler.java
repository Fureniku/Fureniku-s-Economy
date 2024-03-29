package com.fureniku.econ.capability;

import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.capability.currency.CurrencyProvider;
import com.fureniku.econ.capability.currency.ICurrency;
import com.fureniku.econ.capability.customer.CustomerProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityEventHandler {
	
	public static final ResourceLocation CURRENCY = new ResourceLocation(FurenikusEconomy.MODID, "currency");
	public static final ResourceLocation CUSTOMER = new ResourceLocation(FurenikusEconomy.MODID, "customer");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer) {
			event.addCapability(CURRENCY, new CurrencyProvider());
			event.addCapability(CUSTOMER, new CustomerProvider());
		}
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		ICurrency currency = player.getCapability(CurrencyProvider.CURRENCY, null);
		ICurrency currencyOld = event.getOriginal().getCapability(CurrencyProvider.CURRENCY, null);
		
		currency.setMoney(currencyOld.getMoney());
	}
}
