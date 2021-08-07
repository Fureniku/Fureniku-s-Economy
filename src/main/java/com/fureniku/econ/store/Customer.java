package com.fureniku.econ.store;

import java.util.UUID;

import com.fureniku.econ.EconConfig;
import com.fureniku.econ.capability.customer.CustomerProvider;
import com.fureniku.econ.store.management.CartDispenserEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class Customer {
	
	private int expireTime;
	private int cartId;
	private BlockPos activeCart;
	private String uuid;
	private String name;
	
	public Customer(EntityPlayer player, BlockPos activeCart, int cartId) {
		expireTime = 0;
		if (player != null) {
			this.uuid = player.getCachedUniqueIdString();
			this.name = player.getDisplayNameString();
			player.getCapability(CustomerProvider.CUSTOMER, null).setCartPos(activeCart);
		} else {
			this.uuid = "NULL";
			this.name = "NULL";
		}
		this.cartId = cartId;
		this.activeCart = activeCart;
	}
	
	public void closeCart(EntityPlayer player) {
		if (player != null) {
			player.getCapability(CustomerProvider.CUSTOMER, null).setCartPos(BlockPos.ORIGIN);
			//FurenikusEconomy.network.sendTo(new UpdateCustomerCapabilityPacket(activeCart), (EntityPlayerMP) player);
		}
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public int getCartId() {
		return cartId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean hasExpired() {
		return expireTime >= EconConfig.cartTimeout*1200;
	}
	
	public int getBalance(World world) {
		return this.getCart(world).getCartBalance(getCartId());
	}
	
	/**
	 * Should be called any time the customer does something. Resets their timer to zero.
	 */
	public void customerAction() {
		expireTime = 0;
	}
	
	public void setExpireTime(int i) {
		expireTime = i;
	}
	
	public EntityPlayer getCustomerPlayer(World world) {
		if (uuid.isEmpty() || uuid.equalsIgnoreCase("NULL")) {
			return null;
		}
		UUID u = UUID.fromString(uuid);
		return world.getPlayerEntityByUUID(u);
	}
	
	public CartDispenserEntity getCart(World world) {
		TileEntity t = world.getTileEntity(activeCart);
		if (t instanceof CartDispenserEntity) {
			return (CartDispenserEntity) t;
		}
		return null;
	}
	
	public BlockPos getCartPos() {
		return activeCart;
	}

	public void tick(World world) {
		expireTime++;
		
		if (expireTime == (EconConfig.cartTimeout*1200) - 600) {
			getCustomerPlayer(world).sendMessage(new TextComponentTranslation("econ.shop.customer.cart_closing_inactive"));
		}
	}
	
	public static NBTTagCompound serializeNbt(Customer customer) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("expireTime", customer.expireTime);
		nbt.setInteger("cartId", customer.cartId);
		nbt.setInteger("posX", customer.activeCart.getX());
		nbt.setInteger("posY", customer.activeCart.getY());
		nbt.setInteger("posZ", customer.activeCart.getZ());
		nbt.setString("uuid", customer.uuid);
		return nbt;
		
	}
	
	public static Customer deserializeNbt(World world, NBTTagCompound nbt) {
		int cartId = nbt.getInteger("cartId");
		BlockPos pos = new BlockPos(nbt.getInteger("posX"), nbt.getInteger("posY"), nbt.getInteger("posZ"));
		UUID uuid = UUID.fromString(nbt.getString("uuid"));
		EntityPlayer playerCustomer = null;
		if (uuid != null && world != null) {
			playerCustomer = world.getPlayerEntityByUUID(uuid);
		}
		//if they're now offline, remove the customer.
		if (playerCustomer == null) {
			return new Customer(null, pos, cartId);
		} 
		Customer customer = new Customer(playerCustomer, pos, cartId);
		customer.setExpireTime(nbt.getInteger("expireTime"));
		
		return customer;
	}
}
