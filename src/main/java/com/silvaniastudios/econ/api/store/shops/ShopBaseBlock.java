package com.silvaniastudios.econ.api.store.shops;

import com.google.common.collect.ImmutableList;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShopBaseBlock extends Block {
	
	protected String name;
	
	
	public ShopBaseBlock(String name) {
		super(Material.IRON);
		this.name = name;
		setUnlocalizedName(FurenikusEconomy.MODID + "." + name);
		setRegistryName(name);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setHardness(1.5F);
		this.setHarvestLevel("pickaxe", 0);
	}
	
	public void openGui(World world, BlockPos pos, EntityPlayer player, int guiId) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote) {
			if (te != null && te instanceof ShopBaseEntity) {
				player.openGui(FurenikusEconomy.instance, guiId, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	public void registerItemModel(Item itemBlock) {
		FurenikusEconomy.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	public Item createItemBlock() {
		return new ItemBlock(this).setRegistryName(getRegistryName());
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		StateMapperBase b = new DefaultStateMapper();
		BlockStateContainer bsc = this.getBlockState();
		ImmutableList<IBlockState> values = bsc.getValidStates();
		
		for(IBlockState state : values) {
			ModelResourceLocation mrl = new ModelResourceLocation(state.getBlock().getRegistryName(), b.getPropertyString(state.getProperties()));
			
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(state.getBlock()), this.getMetaFromState(state), mrl);
		}
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
