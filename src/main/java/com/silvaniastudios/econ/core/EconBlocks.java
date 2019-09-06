package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.management.CartDispenserBlock;
import com.silvaniastudios.econ.api.store.management.CartDispenserEntity;
import com.silvaniastudios.econ.api.store.management.StockChestBlock;
import com.silvaniastudios.econ.api.store.management.StockChestEntity;
import com.silvaniastudios.econ.api.store.management.StoreManagerBlock;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.management.TillBlock;
import com.silvaniastudios.econ.api.store.management.TillEntity;
import com.silvaniastudios.econ.api.store.shops.CartShopBase;
import com.silvaniastudios.econ.core.blocks.ATMBlock;
import com.silvaniastudios.econ.core.blocks.ATMEntity;
import com.silvaniastudios.econ.core.blocks.shop.ShopShelvesBlock;
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
	
	public static ShopShelvesBlock shop_shelves_full  = new ShopShelvesBlock("shop_shelves_full",  4, EconConstants.Gui.SHOP_SHELVES_FULL);
	public static ShopShelvesBlock shop_shelves_half  = new ShopShelvesBlock("shop_shelves_half",  2, EconConstants.Gui.SHOP_SHELVES_HALF);
	public static ShopShelvesBlock shop_shelves_large = new ShopShelvesBlock("shop_shelves_large", 2, EconConstants.Gui.SHOP_SHELVES_LARGE);
	
	
	public static void register(IForgeRegistry<Block> registry) {
		//Base stuff
		registry.register(atm_block);
		registry.register(store_manager);
		registry.register(cart_dispenser);
		registry.register(stock_chest);
		registry.register(till);
		
		registry.register(shop_shelves_full);
		registry.register(shop_shelves_half);
		registry.register(shop_shelves_large);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(new CitiesItemBlock(atm_block).setRegistryName(atm_block.getRegistryName()));
		registry.register(new ItemBlock(store_manager).setRegistryName(store_manager.getRegistryName()));
		registry.register(new ItemBlock(cart_dispenser).setRegistryName(cart_dispenser.getRegistryName()));
		registry.register(new ItemBlock(stock_chest).setRegistryName(stock_chest.getRegistryName()));
		registry.register(new ItemBlock(till).setRegistryName(till.getRegistryName()));
		
		registry.register(new ItemBlock(shop_shelves_full).setRegistryName(shop_shelves_full.getRegistryName()));
		registry.register(new ItemBlock(shop_shelves_half).setRegistryName(shop_shelves_half.getRegistryName()));
		registry.register(new ItemBlock(shop_shelves_large).setRegistryName(shop_shelves_large.getRegistryName()));
	}
	
	public static void registerModels() {
		atm_block.initModel();
		store_manager.initModel();
		cart_dispenser.initModel();
		stock_chest.initModel();
		till.initModel();
		
		shop_shelves_full.initModel();
		shop_shelves_half.initModel();
		shop_shelves_large.initModel();
	}
	
	@SuppressWarnings("deprecation")
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ATMEntity.class, FurenikusEconomy.MODID + ":atm");
		GameRegistry.registerTileEntity(StoreManagerEntity.class, FurenikusEconomy.MODID + ":store_manager");
		GameRegistry.registerTileEntity(CartDispenserEntity.class, FurenikusEconomy.MODID + ":cart_dispenser");
		GameRegistry.registerTileEntity(StockChestEntity.class, FurenikusEconomy.MODID + ":stock_chest");
		GameRegistry.registerTileEntity(TillEntity.class, FurenikusEconomy.MODID + ":till");
		
		GameRegistry.registerTileEntity(CartShopBase.class, FurenikusEconomy.MODID + ":floating_shelves");
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
