package com.fureniku.econ.network;

import com.fureniku.econ.store.management.StoreManagerContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StoreManagerClearLogPacket implements IMessage {
	
	public StoreManagerClearLogPacket() {}
	@Override public void fromBytes(ByteBuf buf) {}
	@Override public void toBytes(ByteBuf buf) {}
	
	public static class Handler implements IMessageHandler<StoreManagerClearLogPacket, IMessage> {

		@Override
		public IMessage onMessage(StoreManagerClearLogPacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof StoreManagerContainer) {
				StoreManagerContainer ctr = (StoreManagerContainer) player.openContainer;
				if (ctr.tileEntity.getOwnerUuid().equalsIgnoreCase(player.getUniqueID().toString())) {
					ctr.tileEntity.shopInteractionsLocations.clear();
					ctr.tileEntity.shopInteractions.clear();
					
					ctr.tileEntity.sendUpdates();
				}
			}
			return null;
		}
	}
}
