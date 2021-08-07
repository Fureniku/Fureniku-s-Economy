package com.fureniku.econ.blocks.shop;

import java.util.List;

import javax.annotation.Nullable;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.items.StoreStockPairer;
import com.fureniku.econ.store.shops.ShopBaseBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShelvesFullBlock extends ShopBaseBlock {
	

	public ShelvesFullBlock(String name) {
		super(name, 2, 2);
	}
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new ShelvesFullEntity();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.MAIN_HAND && !world.isRemote) {
			TileEntity t = world.getTileEntity(pos);
			if (t != null && t instanceof ShelvesFullEntity) {
				ShelvesFullEntity entity = (ShelvesFullEntity) t;
				if (entity != null) {
					if (entity.ownerUuid.isEmpty()) {
		    			entity.ownerUuid = player.getCachedUniqueIdString();
		    			entity.ownerName = player.getDisplayName().getFormattedText();
		    			player.sendMessage(new TextComponentTranslation("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
		    			entity.sendUpdates();
		    			return true;
		    		}
		    		
		    		if (player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid)) {
		    			if (player.getHeldItem(hand).getItem() instanceof StoreStockPairer) {
		    				if (registerToShopManager(player, hand, world, pos)) {
		    					player.sendMessage(new TextComponentTranslation("econ.shop.registered", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
		    				} else {
		    					player.sendMessage(new TextComponentTranslation("econ.shop.register_failed", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
		    				}
		    				return true;
		    			} else {
		    				player.openGui(FurenikusEconomy.instance, EconConstants.Gui.SHOP_SHELVES_FULL_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
		    			}
		    		} else {
		    			player.openGui(FurenikusEconomy.instance, EconConstants.Gui.SHOP_SHELVES_FULL_NOT_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
		    		}
				}
			}
			registerToShopManager(player, hand, world, pos);
		}
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("econ.tooltip.shops.floating_shelves.1"));
		tooltip.add(TextFormatting.ITALIC +""+ TextFormatting.YELLOW + (I18n.format("econ.tooltip.shops.floating_shelves.2")));
	}
	
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return getBoundingBox(state, world, pos);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state.getProperties().get(FACING) == EnumFacing.NORTH) {
			return new AxisAlignedBB(0, 0, 0, 1, 1, 0.5625);
		}
		if (state.getProperties().get(FACING) == EnumFacing.EAST) {
			return new AxisAlignedBB(0.4375, 0, 0, 1, 1, 1);
		}
		if (state.getProperties().get(FACING) == EnumFacing.SOUTH) {
			return new AxisAlignedBB(0, 0, 0.4375, 1, 1, 1);
		}
		if (state.getProperties().get(FACING) == EnumFacing.WEST) {
			return new AxisAlignedBB(0, 0, 0, 0.5625, 1, 1);
		}
		
		return FULL_BLOCK_AABB;
	}
}
