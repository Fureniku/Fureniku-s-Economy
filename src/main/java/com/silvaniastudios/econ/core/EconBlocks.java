package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.store.CartDispenserBlock;
import com.silvaniastudios.econ.api.store.CartDispenserEntity;
import com.silvaniastudios.econ.api.store.StockChestBlock;
import com.silvaniastudios.econ.api.store.StoreManagerBlock;
import com.silvaniastudios.econ.api.store.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.TillBlock;
import com.silvaniastudios.econ.core.blocks.ATMBlock;
import com.silvaniastudios.econ.core.blocks.ATMEntity;
import com.silvaniastudios.econ.core.items.CitiesItemBlock;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class EconBlocks {
	
	public static ATMBlock atm_block = new ATMBlock("atm_block");
	public static StoreManagerBlock store_manager = new StoreManagerBlock("store_manager");
	public static CartDispenserBlock cart_dispenser = new CartDispenserBlock("cart_dispenser");
	public static StockChestBlock stock_chest = new StockChestBlock("stock_chest");
	public static TillBlock till = new TillBlock("till");
	
	
	public static void register(IForgeRegistry<Block> registry) {
		//Base stuff
		registry.register(atm_block);
		registry.register(store_manager);
		registry.register(cart_dispenser);
		registry.register(stock_chest);
		registry.register(till);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(new CitiesItemBlock(atm_block).setRegistryName(atm_block.getRegistryName()));
		registry.register(new ItemBlock(store_manager).setRegistryName(store_manager.getRegistryName()));
		registry.register(new ItemBlock(cart_dispenser).setRegistryName(cart_dispenser.getRegistryName()));
		registry.register(new ItemBlock(stock_chest).setRegistryName(stock_chest.getRegistryName()));
		registry.register(new ItemBlock(till).setRegistryName(till.getRegistryName()));
	}
	
	public static void registerModels() {
		atm_block.initModel();
		store_manager.initModel();
		cart_dispenser.initModel();
		stock_chest.initModel();
		till.initModel();
	}
	
	@SuppressWarnings("deprecation")
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ATMEntity.class, FurenikusEconomy.MODID + ":atm");
		GameRegistry.registerTileEntity(StoreManagerEntity.class, FurenikusEconomy.MODID + ":store_manager");
		GameRegistry.registerTileEntity(CartDispenserEntity.class, FurenikusEconomy.MODID + ":cart_dispenser");
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
