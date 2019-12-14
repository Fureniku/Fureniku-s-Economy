package com.silvaniastudios.econ.network;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.StoreEntityBase;
import com.silvaniastudios.econ.api.store.management.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.shops.ShopContainerBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class OpenGuiServerSide implements IMessage {

	private int x;
	private int y;
	private int z;
	public static EconUtils econ = new EconUtils();
	
	public OpenGuiServerSide() {}
	
	public OpenGuiServerSide(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		x = nbt.getInteger("x");
		y = nbt.getInteger("y");
		z = nbt.getInteger("z");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		ByteBufUtils.writeTag(buf, nbt);
	}
	
	public static class Handler implements IMessageHandler<OpenGuiServerSide, IMessage> {

		@Override
		public IMessage onMessage(OpenGuiServerSide message, MessageContext ctx) {
			EntityPlayer player = (EntityPlayer) ctx.getServerHandler().player;
			BlockPos pos = new BlockPos(message.x, message.y, message.z);
			TileEntity remoteTE = null;
			
			if (player.openContainer instanceof StoreManagerContainer) {
				StoreManagerContainer ctr = (StoreManagerContainer) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					remoteTE = ctx.getServerHandler().player.world.getTileEntity(pos);
				}
			} else if (player.openContainer instanceof ShopContainerBase) {
				ShopContainerBase ctr = (ShopContainerBase) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					remoteTE = ctx.getServerHandler().player.world.getTileEntity(pos);
				}
			}
			
			if (remoteTE != null && (remoteTE instanceof StoreEntityBase || remoteTE instanceof StoreManagerEntity)) {
				IBlockState state = player.world.getBlockState(pos);
				state.getBlock().onBlockActivated(player.world, pos, state, player, EnumHand.MAIN_HAND, EnumFacing.NORTH, 0.5F, 0.5F, 0.5F);
			}
			
			return null;
		}
	}
}
