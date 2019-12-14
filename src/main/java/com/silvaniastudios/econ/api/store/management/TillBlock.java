package com.silvaniastudios.econ.api.store.management;

import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.EconBlockBase;

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

public class TillBlock extends EconBlockBase {

	public TillBlock(String name) {
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
        	if (te != null && te instanceof TillEntity) {
        		TillEntity entity = (TillEntity) te;
        		
        		if (entity.ownerUuid.isEmpty()) {
        			entity.ownerUuid = player.getCachedUniqueIdString();
        			entity.ownerName = player.getDisplayName().getFormattedText();
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName())));
        		}
        		
        		TileEntity te2 = world.getTileEntity(entity.managerPos);
        		if (te2 != null && te2 instanceof StoreManagerEntity) {
        			StoreManagerEntity manager_entity = (StoreManagerEntity) te2;
        			
        			if (!manager_entity.isShopOpen()) {
        				player.sendMessage(new TextComponentString(I18n.format("econ.shop.closed")));
        			}
        		
	        		if (player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid)) {
	        			//Owner code
	        		} else {
	        			//not owner code
	        		}
        		}
        	}
        }
        return true;
	}
	
	@Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
    
    @Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
    	return new TillEntity();
    }
}
