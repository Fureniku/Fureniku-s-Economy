package com.silvaniastudios.econ.api.store;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

public class StoreManagerEntity extends TileEntity {
	
	public ArrayList<BlockPos> stockChestPositions = new ArrayList<BlockPos>();	
	
	public int[] shopInterfaceID;
	public ArrayList<BlockPos> shopInterfacePositions = new ArrayList<BlockPos>();
	
	public StoreManagerEntity() {}
	
	public ItemStackHandler inventory = new ItemStackHandler(3) {
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return true;
		}
		
		@Override
		protected void onContentsChanged(int slot) {

		}
	};
	
	public void readNBT(NBTTagCompound nbt) {
		stockChestPositions.clear();
		shopInterfacePositions.clear();
		
		int[] stockX = nbt.getIntArray("stockX");
		int[] stockY = nbt.getIntArray("stockY");
		int[] stockZ = nbt.getIntArray("stockZ");
		
		for (int i = 0; i < stockX.length; i++) {
			BlockPos pos = new BlockPos(stockX[i], stockY[i], stockZ[i]);
			stockChestPositions.add(pos);
		}
		
		int[] shopX = nbt.getIntArray("shopX");
		int[] shopY = nbt.getIntArray("shopY");
		int[] shopZ = nbt.getIntArray("shopZ");
		
		for (int i = 0; i < shopX.length; i++) {
			BlockPos pos = new BlockPos(shopX[i], shopY[i], shopZ[i]);
			shopInterfacePositions.add(pos);
		}
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		NBTTagList stockChestList = new NBTTagList();
		NBTTagList shopList = new NBTTagList();
		
		for (int i = 0; i < stockChestPositions.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("stockX", stockChestPositions.get(i).getX());
			com.setInteger("stockY", stockChestPositions.get(i).getX());
			com.setInteger("stockZ", stockChestPositions.get(i).getX());
			stockChestList.appendTag(com);
		}
		
		for (int i = 0; i < shopInterfacePositions.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("stockX", shopInterfacePositions.get(i).getX());
			com.setInteger("stockY", shopInterfacePositions.get(i).getX());
			com.setInteger("stockZ", shopInterfacePositions.get(i).getX());
			shopList.appendTag(com);
		}

		nbt.setTag("stockChests", stockChestList);
		nbt.setTag("shops", shopList);
		
		
		
		
		return nbt;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return writeNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readNBT(nbt);
	}

}
