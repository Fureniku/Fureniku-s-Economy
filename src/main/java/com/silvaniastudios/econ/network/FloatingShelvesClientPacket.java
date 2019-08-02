package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.container.old.ContainerFloatingShelves;
import com.silvaniastudios.econ.api.store.entity.TileEntityFloatingShelves;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FloatingShelvesClientPacket implements IMessage {
	
	public static String buyPrice1;
	public static String sellPrice1;
	public static String buyPrice2;
	public static String sellPrice2;
	public static String buyPrice3;
	public static String sellPrice3;
	public static String buyPrice4;
	public static String sellPrice4;
	public static int x;
	public static int y;
	public static int z;
	
	public static EconUtils econ = new EconUtils();
	
	public FloatingShelvesClientPacket() {}
	
	public FloatingShelvesClientPacket(String b1, String s1, String b2, String s2, String b3, String s3, String b4, String s4, int xPos, int yPos, int zPos) {
		buyPrice1 = b1;
		sellPrice1 = s1;
		buyPrice2 = b2;
		sellPrice2 = s2;
		buyPrice3 = b3;
		sellPrice3 = s3;
		buyPrice4 = b4;
		sellPrice4 = s4;
		x = xPos;
		y = yPos;
		z = zPos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		buyPrice1 = ByteBufUtils.readUTF8String(buf);
		sellPrice1 = ByteBufUtils.readUTF8String(buf);
		buyPrice2 = ByteBufUtils.readUTF8String(buf);
		sellPrice2 = ByteBufUtils.readUTF8String(buf);
		buyPrice3 = ByteBufUtils.readUTF8String(buf);
		sellPrice3 = ByteBufUtils.readUTF8String(buf);
		buyPrice4 = ByteBufUtils.readUTF8String(buf);
		sellPrice4 = ByteBufUtils.readUTF8String(buf);
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, "" + buyPrice1);
		ByteBufUtils.writeUTF8String(buf, "" + sellPrice1);
		ByteBufUtils.writeUTF8String(buf, "" + buyPrice2);
		ByteBufUtils.writeUTF8String(buf, "" + sellPrice2);
		ByteBufUtils.writeUTF8String(buf, "" + buyPrice3);
		ByteBufUtils.writeUTF8String(buf, "" + sellPrice3);
		ByteBufUtils.writeUTF8String(buf, "" + buyPrice4);
		ByteBufUtils.writeUTF8String(buf, "" + sellPrice4);
	}
	
	public static class Handler implements IMessageHandler<FloatingShelvesClientPacket, IMessage> {

		@Override
		public IMessage onMessage(FloatingShelvesClientPacket message, MessageContext ctx) {
			FurenikusEconomy.log(3, "[Network] Packet recieved from client regarding prices in shops!");

			EntityPlayer player = ctx.getServerHandler().player;
			
			String str1 = "" + FloatingShelvesClientPacket.buyPrice1;
			String str2 = "" + FloatingShelvesClientPacket.sellPrice1;
			String str3 = "" + FloatingShelvesClientPacket.buyPrice2;
			String str4 = "" + FloatingShelvesClientPacket.sellPrice2;
			String str5 = "" + FloatingShelvesClientPacket.buyPrice3;
			String str6 = "" + FloatingShelvesClientPacket.sellPrice3;
			String str7 = "" + FloatingShelvesClientPacket.buyPrice4;
			String str8 = "" + FloatingShelvesClientPacket.sellPrice4;

			if (player.openContainer instanceof ContainerFloatingShelves) {
				ContainerFloatingShelves container = (ContainerFloatingShelves) player.openContainer;
				TileEntityFloatingShelves tileShop = container.te;
				
				if (tileShop != null) {
					tileShop.buyPrice1 = econ.parseDouble(str1);
					tileShop.sellPrice1 = econ.parseDouble(str2);
					tileShop.buyPrice2 = econ.parseDouble(str3);
					tileShop.sellPrice2 = econ.parseDouble(str4);
					tileShop.buyPrice3 = econ.parseDouble(str5);
					tileShop.sellPrice3 = econ.parseDouble(str6);
					tileShop.buyPrice4 = econ.parseDouble(str7);
					tileShop.sellPrice4 = econ.parseDouble(str8);
					
					//TODO tileShop.getDescriptionPacket();
					FurenikusEconomy.log(3, "[Network] Floating Shelves prices have been set, and description packet triggered.");
				} else {
					System.out.println("TE is null. Sadface :(");
				}
			}
			return null;
		}
	}
}