package com.fureniku.econ.store.management;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.EconBlockBase;
import com.fureniku.econ.items.ItemMoney;
import com.fureniku.econ.store.StoreEntityBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class StoreManagerBlock extends EconBlockBase {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public StoreManagerBlock(String name) {
		super(name, Material.IRON);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
        		
        		if (entity.getOwnerUuid().isEmpty()) {
        			entity.setOwnerUuid(player.getCachedUniqueIdString());
        			entity.setOwnerName(player.getDisplayName().getFormattedText());
        			player.sendMessage(new TextComponentTranslation("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
        			te.markDirty();
        			return true;
        		}
        		if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemMoney) {
        			ItemMoney money = (ItemMoney) player.getHeldItem(EnumHand.MAIN_HAND).getItem();
        			entity.balance += money.getMoneyValue();
        			player.sendMessage(new TextComponentTranslation("Depositing " + money.getMoneyValue() + " to store"));
        			entity.sendUpdates();
        		}
        		
        		if (player.getUniqueID().toString().equalsIgnoreCase(entity.getOwnerUuid())) {
        			player.openGui(FurenikusEconomy.instance, EconConstants.Gui.STORE_MANAGER_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
        		} else {
        			player.sendMessage(new TextComponentTranslation("econ.shop.not_owner", entity.getOwnerName()));
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
    
    @SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		if (placer.getHorizontalFacing() == EnumFacing.NORTH) { return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH); }
		if (placer.getHorizontalFacing() == EnumFacing.EAST)  { return this.getDefaultState().withProperty(FACING, EnumFacing.EAST); }
		if (placer.getHorizontalFacing() == EnumFacing.SOUTH) { return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH); }
		if (placer.getHorizontalFacing() == EnumFacing.WEST)  { return this.getDefaultState().withProperty(FACING, EnumFacing.WEST); }
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);
	}
	
	public int getMetaFromState(IBlockState state) {
		if (state.getValue(FACING).equals(EnumFacing.NORTH)) {
			return 0;
		} else if (state.getValue(FACING).equals(EnumFacing.EAST)) {
			return 1;
		} else if (state.getValue(FACING).equals(EnumFacing.SOUTH)) {
			return 2;
		} else if (state.getValue(FACING).equals(EnumFacing.WEST)) {
			return 3;
		}
		
		return 0;
	}
	
	public IBlockState getStateFromMeta(int meta) {
		if (meta == 0) { return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH); }
		if (meta == 1) { return this.getDefaultState().withProperty(FACING, EnumFacing.EAST);  }
		if (meta == 2) { return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH); }
		return this.getDefaultState().withProperty(FACING, EnumFacing.WEST); 	
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
}
