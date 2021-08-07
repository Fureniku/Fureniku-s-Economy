package com.fureniku.econ.network;

import com.fureniku.econ.store.management.StoreManagerContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StoreManagerUpdatePacket implements IMessage {
	
	private String name;
	private boolean show_hidden_shops;
	private boolean show_hidden_stock_chests;
	private boolean shop_open;
	private boolean shop_budget;
	
	public StoreManagerUpdatePacket() {}
	
	public StoreManagerUpdatePacket(String name, boolean show_hidden_shops, boolean show_hidden_stock_chests, boolean shop_open, boolean shop_budget) {
		this.name = name;
		this.show_hidden_shops = show_hidden_shops;
		this.show_hidden_stock_chests = show_hidden_stock_chests;
		this.shop_open = shop_open;
		this.shop_budget = shop_budget;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		name = nbt.getString("name");
		show_hidden_shops = nbt.getBoolean("show_hidden_shops");
		show_hidden_stock_chests = nbt.getBoolean("show_hidden_stock_chests");
		shop_open = nbt.getBoolean("shop_open");
		shop_budget = nbt.getBoolean("shop_budget");
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", name);
		nbt.setBoolean("show_hidden_shops", show_hidden_shops);
		nbt.setBoolean("show_hidden_stock_chests", show_hidden_stock_chests);
		nbt.setBoolean("shop_open", shop_open);
		nbt.setBoolean("shop_budget", shop_budget);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<StoreManagerUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(StoreManagerUpdatePacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof StoreManagerContainer) {
				StoreManagerContainer ctr = (StoreManagerContainer) player.openContainer;
				if (ctr.tileEntity.getOwnerUuid().equalsIgnoreCase(player.getUniqueID().toString())) {
					ctr.tileEntity.shop_open = message.shop_open;
					ctr.tileEntity.show_hidden_shops_ui = message.show_hidden_shops;
					ctr.tileEntity.show_hidden_stock_chests_ui = message.show_hidden_stock_chests;
					ctr.tileEntity.shop_budget = message.shop_budget;
					
					ctr.tileEntity.setShopName(message.name);
					
					ctr.tileEntity.sendUpdates();
				}
			}
			return null;
		}
	}
}
