package com.silvaniastudios.econ.api.store.shops;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.silvaniastudios.econ.api.EconUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TradingBlockEntity extends TileEntity {
	
	String player1Uuid;
	String player1Name;
	String player2Uuid;
	String player2Name;
	
	//The money they are offering in the trade
	long player1Money;
	long player2Money;
	
	EconUtils utils = new EconUtils();
	
	public TradingBlockEntity() {}
	
	public ItemStackHandler player1Trade = new ItemStackHandler(25) {
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			markDirty();
		}
	};
	
	public ItemStackHandler player2Trade = new ItemStackHandler(25) {
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			markDirty();
		}
	};
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.NORTH || facing == EnumFacing.EAST || facing == EnumFacing.UP) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(player1Trade);
			} else {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(player2Trade);
			}
		}
		
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("player1Uuid", player1Uuid);
		nbt.setString("player1Name", player1Name);
		nbt.setString("player2Uuid", player2Uuid);
		nbt.setString("player2Name", player2Name);
		
		nbt.setLong("player1Money", player1Money);
		nbt.setLong("player2Money", player2Money);
		
		nbt.setTag("player1Trade", player1Trade.serializeNBT());
		nbt.setTag("player2Trade", player2Trade.serializeNBT());
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		player1Uuid = nbt.getString("player1Uuid");
		player1Name = nbt.getString("player1Name");
		player2Uuid = nbt.getString("player2Uuid");
		player2Name = nbt.getString("player2Name");
		
		player1Money = nbt.getLong("player1Money");
		player2Money = nbt.getLong("player2Money");
		
		if (nbt.hasKey("player1Trade")) {
			player1Trade.deserializeNBT((NBTTagCompound) nbt.getTag("player1Trade"));
		}
		if (nbt.hasKey("player2Trade")) {
			player2Trade.deserializeNBT((NBTTagCompound) nbt.getTag("player2Trade"));
		}
	}
	
	public void performTrade() {
		EntityPlayer player1 = world.getPlayerEntityByUUID(UUID.fromString(player1Uuid));
		EntityPlayer player2 = world.getPlayerEntityByUUID(UUID.fromString(player2Uuid));
		
		if (player1 != null && player2 != null) {
			ItemStackHandler p1 = player1Trade;
			ItemStackHandler p2 = player2Trade;
			
			player1Trade = p2;
			player2Trade = p1;
			
			utils.addMoney(player1, player2Money);
			utils.addMoney(player2, player1Money);
			
			player1.sendMessage(new TextComponentString(I18n.format("econ.shop.trade.money_received", player2Money, player2.getDisplayName())));
			player2.sendMessage(new TextComponentString(I18n.format("econ.shop.trade.money_received", player1Money, player1.getDisplayName())));
			
			player1Money = 0;
			player2Money = 0;
		}
	}
	
	public void clearPlayer1Data() {
		boolean clear = true;
		
		for (int i = 0; i < player1Trade.getSlots(); i++) {
			if (player1Trade.getStackInSlot(i) != ItemStack.EMPTY) {
				clear = false;
				break;
			}
		}
		if (player1Money > 0) {
			clear = false;
		}
		
		if (clear) {
			player1Uuid = "";
			player1Name = "";
		}
	}
	
	public void clearPlayer2Data() {
		boolean clear = true;
		
		for (int i = 0; i < player2Trade.getSlots(); i++) {
			if (player2Trade.getStackInSlot(i) != ItemStack.EMPTY) {
				clear = false;
				break;
			}
		}
		if (player2Money > 0) {
			clear = false;
		}
		
		if (clear) {
			player2Uuid = "";
			player2Name = "";
		}
	}
}
