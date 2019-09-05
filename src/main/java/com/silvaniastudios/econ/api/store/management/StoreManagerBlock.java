package com.silvaniastudios.econ.api.store.management;

import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.EconBlockBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		System.out.println("Calling onBlockActivated (side is " + world.isRemote + ").");
        if (!world.isRemote) {
        	TileEntity te = world.getTileEntity(pos);
        	if (te != null && te instanceof StoreManagerEntity) {
        		StoreManagerEntity entity = (StoreManagerEntity) te;
        		
        		if (entity.ownerUuid.isEmpty()) {
        			entity.ownerUuid = player.getCachedUniqueIdString();
        			entity.ownerName = player.getDisplayName().getFormattedText();
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName())));
        		}
        		
        		if (player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid)) {
        			//Owner code
        		} else {
        			player.sendMessage(new TextComponentString(I18n.format("econ.shop.not_owner", entity.ownerName)));
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
    	return new StoreManagerEntity();
    }

}
