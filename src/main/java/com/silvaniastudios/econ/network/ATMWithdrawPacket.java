package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
			econ.withdrawFunds(message.withdrawAmount, player);
			FurenikusEconomy.network.sendTo(new ServerBalancePacket(""+econ.getBalance(player)), (EntityPlayerMP) player);
			FurenikusEconomy.log(3, "[Network] " + String.format("Received %s from %s", message.withdrawAmount, ctx.getServerHandler().player.getDisplayName()));
			return null;
		}
	}
}
