package com.fureniku.econ.store.management;

import java.util.List;

import javax.annotation.Nullable;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.EconBlockBase;
import com.fureniku.econ.items.StoreStockPairer;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BackToStockBlock extends EconBlockBase {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BackToStockBlock(String name) {
		super(name, Material.IRON);
		this.setHardness(1.0F);
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
        if (!world.isRemote) {
        	TileEntity te = world.getTileEntity(pos);
        	if (te != null && te instanceof BackToStockChestEntity) {
        		BackToStockChestEntity entity = (BackToStockChestEntity) te;
        		
        		if (entity.ownerUuid.isEmpty()) {
        			entity.ownerUuid = player.getCachedUniqueIdString();
        			entity.ownerName = player.getDisplayName().getFormattedText();
        			player.sendMessage(new TextComponentTranslation("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
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
	    				player.openGui(FurenikusEconomy.instance, EconConstants.Gui.BACK_TO_STOCK_CHEST_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
	    			}
	    		} else {
	    			player.openGui(FurenikusEconomy.instance, EconConstants.Gui.BACK_TO_STOCK_CHEST_NOT_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
        			player.sendMessage(new TextComponentTranslation("econ.shop.not_owner"));
        		}
        	}
        }
        return true;
	}
	
	public boolean registerToShopManager(EntityPlayer player, EnumHand hand, World world, BlockPos pos) {
		System.out.println("Begin registering to shop manager");
		ItemStack stack = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		if (stack.getItem() instanceof StoreStockPairer && te != null && te instanceof BackToStockChestEntity) {
			BackToStockChestEntity bts = (BackToStockChestEntity) te;
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null) {
				System.out.println("Valid stock pairer");
				BlockPos managerPos = new BlockPos(nbt.getInteger("manager_x"), nbt.getInteger("manager_y"), nbt.getInteger("manager_z"));
				TileEntity t = world.getTileEntity(managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					System.out.println("Found manager");
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					if (sm.getOwnerUuid().equalsIgnoreCase(player.getCachedUniqueIdString())) {
						System.out.println("Correct player. everything should work?");
						if (sm.addReturnToManager(pos)) {
							bts.setManager(managerPos);
							return true;
						}
						return false;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!unregisterFromManager(worldIn, pos)) {
			System.out.println("[Fureniku's Economy] A destroyed shop interface didn't correctly unregister from its manager at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
		}
		
		super.breakBlock(worldIn, pos, state);
    }
	
	public boolean unregisterFromManager(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof BackToStockChestEntity) {
			BackToStockChestEntity bts = (BackToStockChestEntity) te;
			if (bts.managerPos != null && bts.managerPos.getY() > 0) {
				TileEntity t = world.getTileEntity(bts.managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					return sm.removeReturnStockFromManager(pos);
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
    
    @Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
    	return new BackToStockChestEntity();
    }
    
    @SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("econ.tooltip.shops.back_to_stock_chest.1"));
		tooltip.add(I18n.format("econ.tooltip.shops.back_to_stock_chest.2"));
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
