package com.fureniku.econ.network;

import com.fureniku.econ.store.Customer;
import com.fureniku.econ.store.management.CartDispenserContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CloseCartPacket implements IMessage {
	
	private int id;
	
	public CloseCartPacket() {}
	
	public CloseCartPacket(int id) {
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
	
	public static class Handler implements IMessageHandler<CloseCartPacket, IMessage> {

		@Override
		public IMessage onMessage(CloseCartPacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof CartDispenserContainer) {
				CartDispenserContainer ctr = (CartDispenserContainer) player.openContainer;
				Customer customer = ctr.tileEntity.getManager().getCustomerInCart(ctr.tileEntity, message.id);
				
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					ctr.tileEntity.getManager().removeActiveCustomer(customer, 1);	
				} else if (customer.getUuid().equals(player.getCachedUniqueIdString())) {
					ctr.tileEntity.getManager().removeActiveCustomer(customer, 1);
				}
				
				ctr.tileEntity.sendUpdates();
			}
			return null;
		}
	}
}
