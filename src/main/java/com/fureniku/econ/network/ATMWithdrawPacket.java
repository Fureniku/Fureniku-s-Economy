package com.fureniku.econ.network;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.ATMContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ATMWithdrawPacket implements IMessage {
	
	private long withdrawAmount;
	public static EconUtils econ = new EconUtils();
	
	public ATMWithdrawPacket() {}
	
	public ATMWithdrawPacket(long amt) {
		withdrawAmount = amt;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		withdrawAmount = nbt.getLong("amt");
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("amt", withdrawAmount);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<ATMWithdrawPacket, IMessage> {

		@Override
		public IMessage onMessage(ATMWithdrawPacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof ATMContainer) {
				ATMContainer ctr = (ATMContainer) player.openContainer;
				ctr.tileEntity.withdraw(player, message.withdrawAmount);
			}
			
			
			econ.withdrawFunds(message.withdrawAmount, player);
			FurenikusEconomy.log(3, "[Network] " + String.format("Received %s from %s", message.withdrawAmount, ctx.getServerHandler().player.getDisplayName()));
			return null;
		}
	}
}
