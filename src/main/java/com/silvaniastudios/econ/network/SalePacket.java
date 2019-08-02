package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.store.container.old.ContainerAdminShop;
import com.silvaniastudios.econ.api.store.entity.TileEntityAdminShop;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SalePacket implements IMessage {
	
	private static String packetId;
	private static int slotId;
	private static int x;
	private static int y;
	private static int z;
	
	public SalePacket() {}
	
	public SalePacket(String pktId, int slot, int xPos, int yPos, int zPos) {
		packetId = pktId;
		slotId = slot;
		x = xPos;
		y = yPos;
		z = zPos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		packetId = ByteBufUtils.readUTF8String(buf);
		slotId = ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, packetId);	
		ByteBufUtils.writeVarShort(buf, slotId);
	}
	
	public static class Handler implements IMessageHandler<SalePacket, IMessage> {

		@Override
		public IMessage onMessage(SalePacket message, MessageContext ctx) {
			String pktId = SalePacket.packetId;
			int slotId = SalePacket.slotId;
			int x = SalePacket.x;
			int y = SalePacket.y;
			int z = SalePacket.z;
			EntityPlayer player = ctx.getServerHandler().player;

			FurenikusEconomy.log(3, "[Network] Pkt ID: " + pktId);
			FurenikusEconomy.log(3, "[Network] Data: Slot ID:" + slotId + ", X: " + x + ", Y: " + y + ", Z: " + z);

			if (player.openContainer instanceof ContainerAdminShop) {
				ContainerAdminShop container = (ContainerAdminShop) player.openContainer;
				//container.te.xCoord

				TileEntityAdminShop tileAdmin = container.te;//(TileEntityAdminShop) world.getTileEntity(x, y, z);
				if (tileAdmin != null) {
					/* TODO if (pktId.equalsIgnoreCase("salePacket")) {
						tileAdmin.sellItem(slotId, player);
					} else if (pktId.equalsIgnoreCase("buyPacket")) {
						tileAdmin.buyItem(slotId, player);
					}*/
				}
			}
			return null;
		}
	}
}
