package com.silvaniastudios.econ.api.store;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class StoreManagerEntity extends TileEntity {
	
	public ArrayList<Integer> registeredShopIds = new ArrayList<>();
	public ArrayList<BlockPos> shopPosArray = new ArrayList<>();
	
	public ArrayList<Integer> registeredStockIds = new ArrayList<>();
	public ArrayList<BlockPos> stockPosArray = new ArrayList<>();
	
	public ArrayList<Integer> registeredTillIds = new ArrayList<>();
	public ArrayList<BlockPos> tillPosArray = new ArrayList<>();
	
	public ArrayList<Integer> registeredCartDispenserIds = new ArrayList<>();
	public ArrayList<BlockPos> cartDispenserPosArray = new ArrayList<>();
	
	public String ownerName;
	public String ownerUuid;
	public String shopName;
	
	public long balance;
	
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
		registeredShopIds.clear();
		shopPosArray.clear();
		registeredStockIds.clear();
		stockPosArray.clear();
		registeredTillIds.clear();
		tillPosArray.clear();
		registeredCartDispenserIds.clear();
		cartDispenserPosArray.clear();
		
		ownerName = nbt.getString("ownerName");
		ownerUuid = nbt.getString("ownerUuid");
		shopName = nbt.getString("shopName");
		
		balance = nbt.getLong("balance");
		
		NBTTagList shopPosList = nbt.getTagList("shops", Constants.NBT.TAG_COMPOUND);
		NBTTagList stock = nbt.getTagList("stock", Constants.NBT.TAG_COMPOUND);
		NBTTagList tillPosList = nbt.getTagList("tills", Constants.NBT.TAG_COMPOUND);
		NBTTagList cartDispenserPosList = nbt.getTagList("cartDispenser", Constants.NBT.TAG_COMPOUND);
		
		int[] shopIds = nbt.getIntArray("shopIds");
		int[] stockIds = nbt.getIntArray("stockIds");
		int[] tillIds = nbt.getIntArray("tillIds");
		int[] cartDispenserIds = nbt.getIntArray("cartDispenserIds");
		
		for (int i = 0; i < shopIds.length; i++) {
			NBTTagCompound pos = shopPosList.getCompoundTagAt(i);
			registeredShopIds.add(shopIds[i]);
			shopPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < stockIds.length; i++) {
			NBTTagCompound pos = stock.getCompoundTagAt(i);
			registeredStockIds.add(stockIds[i]);
			stockPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < tillIds.length; i++) {
			NBTTagCompound pos = tillPosList.getCompoundTagAt(i);
			registeredTillIds.add(tillIds[i]);
			tillPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		
		for (int i = 0; i < cartDispenserIds.length; i++) {
			NBTTagCompound pos = cartDispenserPosList.getCompoundTagAt(i);
			registeredCartDispenserIds.add(cartDispenserIds[i]);
			cartDispenserPosArray.add(new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setString("ownerName", ownerName);
		nbt.setString("ownerUuid", ownerUuid);
		nbt.setString("shopName", shopName);
		
		nbt.setLong("balance", balance);
		
		NBTTagList shopList = new NBTTagList();
		NBTTagList stockList = new NBTTagList();
		NBTTagList tillList = new NBTTagList();
		NBTTagList cartDispenserList = new NBTTagList();
		
		for (int i = 0; i < shopPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", shopPosArray.get(i).getX());
			com.setInteger("y", shopPosArray.get(i).getX());
			com.setInteger("z", shopPosArray.get(i).getX());
			shopList.appendTag(com);
		}
		
		for (int i = 0; i < stockPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", shopPosArray.get(i).getX());
			com.setInteger("y", shopPosArray.get(i).getX());
			com.setInteger("z", shopPosArray.get(i).getX());
			stockList.appendTag(com);
		}
		
		for (int i = 0; i < tillPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", shopPosArray.get(i).getX());
			com.setInteger("y", shopPosArray.get(i).getX());
			com.setInteger("z", shopPosArray.get(i).getX());
			tillList.appendTag(com);
		}
		
		for (int i = 0; i < cartDispenserPosArray.size(); i++) {
			NBTTagCompound com = new NBTTagCompound();
			com.setInteger("x", shopPosArray.get(i).getX());
			com.setInteger("y", shopPosArray.get(i).getX());
			com.setInteger("z", shopPosArray.get(i).getX());
			cartDispenserList.appendTag(com);
		}
		

		nbt.setTag("shops", shopList);
		nbt.setTag("stock", stockList);
		nbt.setTag("tills", tillList);
		nbt.setTag("cartDispenser", cartDispenserList);

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
