package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.management.StockChestContainer;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StockUpdatePacket implements IMessage {
	
	private String name;
	private boolean bts;
	public static EconUtils econ = new EconUtils();
	
	public StockUpdatePacket() {}
	
	public StockUpdatePacket(String name, boolean bts) {
		this.bts = bts;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		bts = nbt.getBoolean("isBTS");
		name = nbt.getString("name");
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", name);
		nbt.setBoolean("isBTS", bts);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<StockUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(StockUpdatePacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof StockChestContainer) {
				StockChestContainer ctr = (StockChestContainer) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					BlockPos pos = ctr.tileEntity.getPos();
					ctr.tileEntity.isBackToStock = message.bts;
					ctr.tileEntity.stockChestName = message.name;
					ctr.tileEntity.sendUpdates();
					FurenikusEconomy.log(3, "[Network] Successfully updated stock data at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
					
				}
			}
			return null;
		}
	}
}
