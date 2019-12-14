package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.management.EnumLogType;
import com.silvaniastudios.econ.api.store.management.StoreManagerContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StoreManagerWithdrawPacket implements IMessage {
	
	static EconUtils econ = new EconUtils();
	
	public StoreManagerWithdrawPacket() {}
	@Override public void fromBytes(ByteBuf buf) {}
	@Override public void toBytes(ByteBuf buf) {}
	
	public static class Handler implements IMessageHandler<StoreManagerWithdrawPacket, IMessage> {

		@Override
		public IMessage onMessage(StoreManagerWithdrawPacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof StoreManagerContainer) {
				StoreManagerContainer ctr = (StoreManagerContainer) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					if (econ.addMoney(player, ctr.tileEntity.balance)) {
						ctr.tileEntity.logEvent(EnumLogType.WITHDRAW_BALANCE, BlockPos.ORIGIN, econ.formatBalance(ctr.tileEntity.balance));
						ctr.tileEntity.balance = 0;
						ctr.tileEntity.sendUpdates();
					}
				}
			}
			return null;
		}
	}
}
