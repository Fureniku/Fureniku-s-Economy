package com.fureniku.econ.network;

import com.fureniku.econ.blocks.shop.containers.ShelvesFullContainerBuy;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AddToCartPacket implements IMessage {
	
	private int id;
	
	public AddToCartPacket() {}
	
	public AddToCartPacket(int id) {
		this.id = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		id = nbt.getInteger("id");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("id", id);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<AddToCartPacket, IMessage> {

		@Override
		public IMessage onMessage(AddToCartPacket message, MessageContext ctx) {
			System.out.println("buying packet received on server side for slot " + message.id);
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof ShelvesFullContainerBuy) {
				System.out.println("is container");
				ShelvesFullContainerBuy ctr = (ShelvesFullContainerBuy) player.openContainer;
				
				if (message.id <= ctr.tileEntity.shopSize) {
					System.out.println("valid slot. go to addToCart.");
					ctr.tileEntity.addToCart(message.id, player);
				}
			}
			return null;
		}
	}
}
