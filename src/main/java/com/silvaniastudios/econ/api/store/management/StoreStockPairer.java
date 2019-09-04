package com.silvaniastudios.econ.api.store.management;

import com.silvaniastudios.econ.api.store.shops.ShopBaseBlock;
import com.silvaniastudios.econ.api.store.shops.ShopBaseEntity;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.items.EconItemBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoreStockPairer extends EconItemBase {
	
	public StoreStockPairer(String name) {
		super(name, 1);
		this.setMaxStackSize(1);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		
		ItemStack stack = player.getHeldItem(hand);
		if (!(stack.getItem() instanceof StoreStockPairer)) {
			return EnumActionResult.FAIL;
		}
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		
		if (nbt.getString("owner_uuid").equalsIgnoreCase(player.getCachedUniqueIdString()) || nbt.getString("owner_uuid").length() < 1) {
			if (block instanceof StoreManagerBlock) {
				nbt.setInteger("manager_x", pos.getX());
				nbt.setInteger("manager_y", pos.getY());
				nbt.setInteger("manager_z", pos.getZ());
				nbt.setBoolean("manager_set", true);
				nbt.setString("owner_uuid", player.getCachedUniqueIdString());
				nbt.setString("owner_name", player.getDisplayNameString());
				return EnumActionResult.PASS;
			}

			if (nbt.getBoolean("manager_set")) {
				BlockPos manager_pos = new BlockPos(nbt.getInteger("manager_x"), nbt.getInteger("manager_y"), nbt.getInteger("manager_z"));
				TileEntity te = worldIn.getTileEntity(manager_pos);
				
				if (te != null && te instanceof StoreManagerEntity) {
					StoreManagerEntity manager_entity = (StoreManagerEntity) te;
					
					if (block instanceof ShopBaseBlock) {
						TileEntity t = worldIn.getTileEntity(pos);
						if (t instanceof ShopBaseEntity) {
							ShopBaseEntity e = (ShopBaseEntity) t;
							e.setManager(manager_pos);
							manager_entity.addShopToManager(pos);
							return EnumActionResult.PASS;
						}
					}
					
					if (block instanceof StockChestBlock) {
						TileEntity t = worldIn.getTileEntity(pos);
						if (t instanceof StockChestEntity) {
							StockChestEntity e = (StockChestEntity) t;
							e.setManager(manager_pos);
							manager_entity.addStockToManager(pos);
							return EnumActionResult.PASS;
						}
					}
					
					if (block instanceof CartDispenserBlock) {
						TileEntity t = worldIn.getTileEntity(pos);
						if (t instanceof CartDispenserEntity) {
							CartDispenserEntity e = (CartDispenserEntity) t;
							e.setManager(manager_pos);
							manager_entity.addCartDispenserToManager(pos);
							return EnumActionResult.PASS;
						}
					}
					
					if (block instanceof TillBlock) {
						TileEntity t = worldIn.getTileEntity(pos);
						if (t instanceof TillEntity) {
							TillEntity e = (TillEntity) t;
							e.setManager(manager_pos);
							manager_entity.addTillToManager(pos);
							return EnumActionResult.PASS;
						}
					}
				}
			}
		}
		return EnumActionResult.FAIL;
	}
	
	/*@Override
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean par4) {
		if (item.stackTagCompound != null) {
			if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				list.add(EnumChatFormatting.ITALIC + "Sneak to see stored data");
			} else {
				EnumChatFormatting colour;
				
				if (player.getDisplayName().equalsIgnoreCase(item.stackTagCompound.getString("storeOwner"))) {
					colour = EnumChatFormatting.GREEN;
				} else {
					colour = EnumChatFormatting.RED;
				}
				
				list.add("Stock Chest owner: " + item.stackTagCompound.getString("playerName"));
				list.add("");
				list.add("Stock Chest X Position: " + item.stackTagCompound.getInteger("xPos"));
				list.add("Stock Chest Y Position: " + item.stackTagCompound.getInteger("yPos"));
				list.add("Stock Chest Z Position: " + item.stackTagCompound.getInteger("zPos"));
			}
		} else {
			list.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GOLD + "This Stock Link has not been used yet!");
			list.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.YELLOW + "Right-click a Stock Chest to save it's data,");
			list.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.YELLOW + "Then right-click your store to create a link.");
		}
	}*/
}
