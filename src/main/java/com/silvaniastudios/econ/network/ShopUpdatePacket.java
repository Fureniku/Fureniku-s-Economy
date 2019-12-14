package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.shop.containers.ShelvesFullContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ShopUpdatePacket implements IMessage {
	
	private int[] prices;
	private String name;
	public static EconUtils econ = new EconUtils();
	
	public ShopUpdatePacket() {}
	
	public ShopUpdatePacket(int[] prices, String name) {
		this.prices = prices;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		prices = nbt.getIntArray("prices");
		name = nbt.getString("name");
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setIntArray("prices", prices);
		nbt.setString("name", name);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<ShopUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(ShopUpdatePacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof ShelvesFullContainer) {
				ShelvesFullContainer ctr = (ShelvesFullContainer) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					BlockPos pos = ctr.tileEntity.getPos();
					if (ctr.tileEntity.priceList.length == message.prices.length) {
						System.out.println("Share those prices!");
						for (int i = 0; i < message.prices.length; i++) {
							System.out.println("Price " + i + " is " + message.prices[i]);
						}
						ctr.tileEntity.priceList = message.prices;
						ctr.tileEntity.shopName = message.name;
						ctr.tileEntity.sendUpdates();
						FurenikusEconomy.log(3, "[Network] Successfully updated shop data at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
					} else {
						FurenikusEconomy.log(1, "[Network] A shop failed to set its data correctly at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
					}
				}
			}
			return null;
		}
	}
}
