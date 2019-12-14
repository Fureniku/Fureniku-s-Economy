package com.silvaniastudios.econ.api.store.management;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.silvaniastudios.econ.api.store.shops.ShopBaseBlock;
import com.silvaniastudios.econ.api.store.shops.ShopBaseEntity;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.items.EconItemBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class StoreStockPairer extends EconItemBase {
	
	public StoreStockPairer(String name) {
		super(name, 1);
		this.setMaxStackSize(1);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		
		ItemStack stack = player.getHeldItem(hand);
		if (!(stack.getItem() instanceof StoreStockPairer)) {
			return EnumActionResult.FAIL;
		}
		
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null) {
			System.out.println("Creating new NBT because it didn't exist yet");
			nbt = new NBTTagCompound();
		}
		
		System.out.println("Block hit is a " + block.getLocalizedName());
		
		if (nbt.getString("owner_uuid").equalsIgnoreCase(player.getCachedUniqueIdString()) || nbt.getString("owner_uuid").length() < 1) {
			System.out.println("Start doing NBT stuff");
			if (block instanceof StoreManagerBlock) {
				System.out.println("Manager data should now be set.");
				nbt.setInteger("manager_x", pos.getX());
				nbt.setInteger("manager_y", pos.getY());
				nbt.setInteger("manager_z", pos.getZ());
				nbt.setBoolean("manager_set", true);
				nbt.setString("owner_uuid", player.getCachedUniqueIdString());
				nbt.setString("owner_name", player.getDisplayNameString());
				stack.setTagCompound(nbt);
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
				
			} else {
				System.out.println("Mismatch");
			}
		}
		return EnumActionResult.FAIL;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				tooltip.add(TextFormatting.ITALIC + "Sneak to see stored data");
			} else {
				TextFormatting colour;
				NBTTagCompound nbt = stack.getTagCompound();
				EntityPlayer player = Minecraft.getMinecraft().player;
				
				if (player.getCachedUniqueIdString().equalsIgnoreCase(nbt.getString("owner_uuid"))) {
					colour = TextFormatting.GREEN;
				} else {
					colour = TextFormatting.RED;
				}
				
				tooltip.add(colour + "Store Manager owner: " + nbt.getString("owner_name"));
				tooltip.add("");
				tooltip.add("Store Manager X Position: " + nbt.getInteger("manager_x"));
				tooltip.add("Store Manager Y Position: " + nbt.getInteger("manager_y"));
				tooltip.add("Store Manager Z Position: " + nbt.getInteger("manager_z"));
			}
		} else {
			tooltip.add(TextFormatting.ITALIC + "" + TextFormatting.GOLD + "This Stock Link has not been used yet!");
			tooltip.add(TextFormatting.ITALIC + "" + TextFormatting.YELLOW + "Right-click a Stock Chest to save it's data,");
			tooltip.add(TextFormatting.ITALIC + "" + TextFormatting.YELLOW + "Then right-click your store to create a link.");
		}
	}
}
