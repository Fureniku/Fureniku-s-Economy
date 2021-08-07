package com.fureniku.econ.client;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.capability.customer.CustomerProvider;
import com.fureniku.econ.capability.customer.ICustomer;
import com.fureniku.econ.store.Customer;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {
	
	@SubscribeEvent
	public void gameOverlayEvent(RenderGameOverlayEvent.Text event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		ICustomer cstmr = player.getCapability(CustomerProvider.CUSTOMER, null);
		if (cstmr.getCartPos() != null && cstmr.getCartPos().getY() > 0) {
			Customer customer = cstmr.getCustomer(player);
			if (customer != null) {
				String val = I18n.format("econ.overlay.cart_value", EconUtils.formatBalance(cstmr.getCustomer(player).getBalance(player.getEntityWorld())));
				
				int cartSlots = customer.getCart(player.getEntityWorld()).getUsedSlotsInCart(customer.getCartId());
				int playerSpace = playerSpace(player);
				ChatFormatting col = cartSlots > playerSpace ? ChatFormatting.RED : ChatFormatting.WHITE;
				
				String size = col + I18n.format("econ.overlay.cart_size", cartSlots);
				mc.fontRenderer.drawString(val, 10, 10, 0xffffff, false);
				mc.fontRenderer.drawString(size, 10, 20, 0xffffff, false);
				if (cartSlots > playerSpace) {
					mc.fontRenderer.drawString(ChatFormatting.RED + I18n.format("econ.overlay.cart_oversize"), 10, 30, 0xffffff, false);
				}
			}
		}
	}
	
	
	private int playerSpace(EntityPlayer player) {
		int space = 0;
		for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
			if (player.inventory.mainInventory.get(i).isEmpty()) {
				space++;
			}
		}
		return space;
	}
}
