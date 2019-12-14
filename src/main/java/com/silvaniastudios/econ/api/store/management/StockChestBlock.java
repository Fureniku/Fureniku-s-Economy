package com.silvaniastudios.econ.api.store.management;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.EconBlockBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class StockChestBlock extends EconBlockBase {

	public StockChestBlock(String name) {
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
        if (!world.isRemote) {
        	TileEntity te = world.getTileEntity(pos);
        	if (te != null && te instanceof StockChestEntity) {
        		StockChestEntity entity = (StockChestEntity) te;
        		
        		if (entity.ownerUuid.isEmpty()) {
        			entity.ownerUuid = player.getCachedUniqueIdString();
        			entity.ownerName = player.getDisplayName().getFormattedText();
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName())));
        		}
        		
        		if (player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid)) {
        			if (player.getHeldItem(hand).getItem() instanceof StoreStockPairer) {
	    				if (registerToShopManager(player, hand, world, pos)) {
	    					player.sendMessage(new TextComponentString(I18n.format("econ.shop.registered", this.getPickBlock(state, null, world, pos, player).getDisplayName())));
	    				} else {
	    					player.sendMessage(new TextComponentString(I18n.format("econ.shop.register_failed", this.getPickBlock(state, null, world, pos, player).getDisplayName())));
	    				}
	    				return true;
	    			} else {
	    				player.openGui(FurenikusEconomy.instance, EconConstants.Gui.STOCK_CHEST_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
	    			}
	    		} else {
	    			player.openGui(FurenikusEconomy.instance, EconConstants.Gui.STOCK_CHEST_NOT_OWNER, world, pos.getX(), pos.getY(), pos.getZ());
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.not_owner")));
        		}
        	}
        }
        return true;
	}
	
	public boolean registerToShopManager(EntityPlayer player, EnumHand hand, World world, BlockPos pos) {
		System.out.println("Begin registering to shop manager");
		ItemStack stack = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		if (stack.getItem() instanceof StoreStockPairer && te != null && te instanceof StockChestEntity) {
			StockChestEntity sce = (StockChestEntity) te;
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null) {
				System.out.println("Valid stock pairer");
				BlockPos managerPos = new BlockPos(nbt.getInteger("manager_x"), nbt.getInteger("manager_y"), nbt.getInteger("manager_z"));
				TileEntity t = world.getTileEntity(managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					System.out.println("Found manager");
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					if (sm.ownerUuid.equalsIgnoreCase(player.getCachedUniqueIdString())) {
						System.out.println("Correct player. everything should work?");
						if (sm.addStockToManager(pos)) {
							sce.setManager(managerPos);
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
		if (te != null && te instanceof StockChestEntity) {
			System.out.println("TE found");
			StockChestEntity sce = (StockChestEntity) te;
			if (sce.managerPos != null && sce.managerPos.getY() > 0) {
				TileEntity t = world.getTileEntity(sce.managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					System.out.println("Found manager");
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					return sm.removeStockFromManager(pos);
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
    	return new StockChestEntity(EconConstants.Inventories.STOCK_CHEST_SIZE);
    }
}
