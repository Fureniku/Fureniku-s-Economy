package com.fureniku.econ.network;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.EconUtils;
import com.fureniku.econ.store.StoreEntityBase;
import com.fureniku.econ.store.management.CartDispenserBlock;
import com.fureniku.econ.store.management.CartDispenserContainer;
import com.fureniku.econ.store.management.StoreManagerContainer;
import com.fureniku.econ.store.management.StoreManagerEntity;
import com.fureniku.econ.store.shops.ShopContainerBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
	private int cartId;
	public static EconUtils econ = new EconUtils();
	
	public OpenGuiServerSide() {}
	
	public OpenGuiServerSide(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.cartId = -1;
	}
	
	public OpenGuiServerSide(int x, int y, int z, int cartId) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.cartId = cartId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		x = nbt.getInteger("x");
		y = nbt.getInteger("y");
		z = nbt.getInteger("z");
		cartId = nbt.getInteger("cartId");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		nbt.setInteger("cartId", cartId);
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
				if (ctr.tileEntity.getOwnerUuid().equalsIgnoreCase(player.getUniqueID().toString())) {
					remoteTE = ctx.getServerHandler().player.world.getTileEntity(pos);
				}
			} else if (player.openContainer instanceof ShopContainerBase) {
				ShopContainerBase ctr = (ShopContainerBase) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					remoteTE = ctx.getServerHandler().player.world.getTileEntity(pos);
				}
			} else if (player.openContainer instanceof CartDispenserContainer) {
				CartDispenserContainer ctr = (CartDispenserContainer) player.openContainer;
				if (ctr.tileEntity.ownerUuid.equalsIgnoreCase(player.getUniqueID().toString())) {
					System.out.println("Opening cart ID " + message.cartId);
					IBlockState state = player.world.getBlockState(pos);
					if (state.getBlock() instanceof CartDispenserBlock) {
						CartDispenserBlock block = (CartDispenserBlock) state.getBlock();
						block.openGui(player, EconConstants.Gui.CART_DISPENSER_OWNER_1 + message.cartId, player.world, pos);
					} else {
						System.out.println("no, bad.");
					}
					//player.openGui(FurenikusEconomy.instance, EconConstants.Gui.CART_DISPENSER_OWNER_1 + message.cartId, player.world, pos.getX(), pos.getY(), pos.getZ());
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
