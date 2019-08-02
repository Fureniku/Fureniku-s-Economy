package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.core.blocks.ATMBlock;
import com.silvaniastudios.econ.core.items.CitiesItemBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class CoreBlocks {
	
	public static ATMBlock atmBlock = new ATMBlock("atmBlock");
	
	public static void register(IForgeRegistry<Block> registry) {
		//Base stuff
		registry.register(atmBlock);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(new CitiesItemBlock(atmBlock).setRegistryName(atmBlock.getRegistryName()));
	}
	
	public static void registerModels() {
		atmBlock.initModel();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
