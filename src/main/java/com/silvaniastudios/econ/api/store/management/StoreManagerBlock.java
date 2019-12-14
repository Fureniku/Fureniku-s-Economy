package com.silvaniastudios.econ.api.store.management;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.StoreEntityBase;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.EconBlockBase;
import com.silvaniastudios.econ.core.items.ItemMoney;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class StoreManagerBlock extends EconBlockBase {
	
	public StoreManagerBlock(String name) {
		super(name, Material.IRON);
		this.setHardness(1.0F);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setLightOpacity(0);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
   	public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
    }
   	
    @Override
   	public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
        	TileEntity te = world.getTileEntity(pos);
        	if (te != null && te instanceof StoreManagerEntity) {
        		StoreManagerEntity entity = (StoreManagerEntity) te;
        		
        		if (entity.ownerUuid.isEmpty()) {
        			entity.ownerUuid = player.getCachedUniqueIdString();
        			entity.ownerName = player.getDisplayName().getFormattedText();
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName())));
        			te.markDirty();
        			return true;
        		}
        		if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemMoney) {
        			ItemMoney money = (ItemMoney) player.getHeldItem(EnumHand.MAIN_HAND).getItem();
        			entity.balance += money.getMoneyValue();
        			player.sendMessage(new TextComponentString("Depositing " + money.getMoneyValue() + " to store"));
        			entity.sendUpdates();
        		}
        		
        		if (player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid)) {
        			player.openGui(FurenikusEconomy.instance, EconConstants.Gui.STORE_MANAGER_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
        		} else {
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.not_owner", entity.ownerName)));
        		}
        	}
        }
        return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null) {
			if (te instanceof StoreManagerEntity) {
				StoreManagerEntity storeEntity = (StoreManagerEntity) te;
				
				for (int i = 0; i < storeEntity.shopPosArray.size(); i++) {
					TileEntity t = world.getTileEntity(storeEntity.shopPosArray.get(i));
					if (t instanceof StoreEntityBase) {
						StoreEntityBase s = (StoreEntityBase) t;
						s.setManager(BlockPos.ORIGIN);
					}
				}
				
				for (int i = 0; i < storeEntity.stockPosArray.size(); i++) {
					TileEntity t = world.getTileEntity(storeEntity.stockPosArray.get(i));
					if (t instanceof StockChestEntity) {
						StockChestEntity s = (StockChestEntity) t;
						s.setManager(BlockPos.ORIGIN);
					}
				}
				
				for (int i = 0; i < storeEntity.tillPosArray.size(); i++) {
					TileEntity t = world.getTileEntity(storeEntity.tillPosArray.get(i));
					if (t instanceof TillEntity) {
						TillEntity till = (TillEntity) t;
						till.setManager(BlockPos.ORIGIN);
					}
				}
				
				for (int i = 0; i < storeEntity.cartDispenserPosArray.size(); i++) {
					TileEntity t = world.getTileEntity(storeEntity.cartDispenserPosArray.get(i));
					if (t instanceof CartDispenserEntity) {
						CartDispenserEntity cd = (CartDispenserEntity) t;
						cd.setManager(BlockPos.ORIGIN);
					}
				}
				FurenikusEconomy.log(2, "Store Manager is being destroyed! Removing store manager from all connected stores.");
			}
		}
		
		super.breakBlock(world, pos, state);
    }
	
	@Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
    
    @Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
    	return new StoreManagerEntity();
    }
}
