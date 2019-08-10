package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.core.blocks.ATMBlock;
import com.silvaniastudios.econ.core.blocks.ATMEntity;
import com.silvaniastudios.econ.core.items.CitiesItemBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class EconBlocks {
	
	public static ATMBlock atm_block = new ATMBlock("atm_block");
	
	public static void register(IForgeRegistry<Block> registry) {
		//Base stuff
		registry.register(atm_block);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(new CitiesItemBlock(atm_block).setRegistryName(atm_block.getRegistryName()));
	}
	
	public static void registerModels() {
		atm_block.initModel();
	}
	
	@SuppressWarnings("deprecation")
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ATMEntity.class, FurenikusEconomy.MODID + ":atm");
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
