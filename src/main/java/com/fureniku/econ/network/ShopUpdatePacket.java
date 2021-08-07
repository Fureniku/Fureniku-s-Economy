package com.fureniku.econ.network;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.shop.containers.ShelvesFullContainer;
import com.fureniku.econ.store.shops.ShopBaseEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ShopUpdatePacket implements IMessage {
	
	public static EconUtils econ = new EconUtils();
	private ShopBaseEntity te;
	private int[] prices;
	private String name;
	private int selectedId;
	
	public float[] slot_scale;
	public float[] slot_x_pos;
	public float[] slot_y_pos;
	public float[] slot_z_pos;
	public int[] slot_x_rot;
	public int[] slot_y_rot;
	public int[] slot_z_rot;
	
	public ShopUpdatePacket() {}
	
	public ShopUpdatePacket(int[] prices, String name, ShopBaseEntity te, int selectedId) {
		this.te = te;
		
		this.prices = prices;
		this.name = name;
		
		this.selectedId = selectedId;
		
		slot_scale = new float[te.shopSize];
		slot_x_pos = new float[te.shopSize];
		slot_y_pos = new float[te.shopSize];
		slot_z_pos = new float[te.shopSize];
		slot_x_rot = new int[te.shopSize];
		slot_y_rot = new int[te.shopSize];
		slot_z_rot = new int[te.shopSize];
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		prices = nbt.getIntArray("prices");
		name = nbt.getString("name");
		int size = nbt.getInteger("shopSize");
		
		//Re-initialize these because the server doesn't know about the size
		slot_scale = new float[size];
		slot_x_pos = new float[size];
		slot_y_pos = new float[size];
		slot_z_pos = new float[size];
		
		for (int i = 0; i < size; i++) {
			slot_scale[i] = nbt.getFloat("slot_scale_" + i);
			slot_x_pos[i] = nbt.getFloat("slot_x_pos_" + i);
			slot_y_pos[i] = nbt.getFloat("slot_y_pos_" + i);
			slot_z_pos[i] = nbt.getFloat("slot_z_pos_" + i);
		}
		
		System.out.println("received xrot " + nbt.getIntArray("slot_x_rot")[0]);
		slot_x_rot = nbt.getIntArray("slot_x_rot");
		slot_y_rot = nbt.getIntArray("slot_y_rot");
		slot_z_rot = nbt.getIntArray("slot_z_rot");
		
		System.out.println("receiving selected id " + nbt.getInteger("selected_id"));
		selectedId = nbt.getInteger("selected_id");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setIntArray("prices", prices);
		nbt.setString("name", name);
		nbt.setInteger("shopSize", te.shopSize);
		
		for (int i = 0; i < te.shopSize; i++) {
			nbt.setFloat("slot_scale_" + i, te.slot_scale[i]);
			nbt.setFloat("slot_x_pos_" + i, te.slot_x_pos[i]);
			nbt.setFloat("slot_y_pos_" + i, te.slot_y_pos[i]);
			nbt.setFloat("slot_z_pos_" + i, te.slot_z_pos[i]);
		}
		
		System.out.println("sending xrot " + slot_x_rot[0]);
		nbt.setIntArray("slot_x_rot", te.slot_x_rot);
		nbt.setIntArray("slot_y_rot", te.slot_y_rot);
		nbt.setIntArray("slot_z_rot", te.slot_z_rot);
		
		System.out.println("sending select id " + selectedId);
		nbt.setInteger("selected_id", selectedId);
		
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<ShopUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(ShopUpdatePacket message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			if (player.openContainer instanceof ShelvesFullContainer) {
				ShelvesFullContainer ctr = (ShelvesFullContainer) player.openContainer;
				ShopBaseEntity te = ctr.tileEntity;
				if (te.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					BlockPos pos = te.getPos();
					if (te.priceList.length == message.prices.length) {
						te.priceList = message.prices;
						te.shopName = message.name;
						
						te.slot_scale = message.slot_scale;
						te.slot_x_pos = message.slot_x_pos;
						te.slot_y_pos = message.slot_y_pos;
						te.slot_z_pos = message.slot_z_pos;
						System.out.println("and finally, setting " + message.slot_x_rot[0]);
						te.slot_x_rot = message.slot_x_rot;
						te.slot_y_rot = message.slot_y_rot;
						te.slot_z_rot = message.slot_z_rot;
						
						te.selectedId = message.selectedId;
						
						te.sendUpdates();
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
