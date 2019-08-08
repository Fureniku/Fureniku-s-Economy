package com.silvaniastudios.econ.core.blocks;

import com.google.common.collect.ImmutableList;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EconBlockBase extends Block {
	
	protected String name;
	
	public EconBlockBase(String name, Material mat) {
		super(mat);
		this.name = name;
		
		setUnlocalizedName(FurenikusEconomy.MODID + "." + name);
		setRegistryName(name);
		this.setHardness(3.0F);
	}
	
	public void registerItemModel(Item itemBlock) {
		FurenikusEconomy.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	public Item createItemBlock() {
		return new ItemBlock(this).setRegistryName(getRegistryName());
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
}