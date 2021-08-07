package com.fureniku.econ;

import com.fureniku.econ.blocks.ATMBlock;
import com.fureniku.econ.blocks.ATMEntity;
import com.fureniku.econ.blocks.shop.ShelvesFullBlock;
import com.fureniku.econ.blocks.shop.ShelvesFullEntity;
import com.fureniku.econ.items.EconomyItemBlock;
import com.fureniku.econ.store.management.BackToStockBlock;
import com.fureniku.econ.store.management.BackToStockChestEntity;
import com.fureniku.econ.store.management.CartDispenserBlock;
import com.fureniku.econ.store.management.CartDispenserEntity;
import com.fureniku.econ.store.management.StockChestBlock;
import com.fureniku.econ.store.management.StockChestBlockRotatable;
import com.fureniku.econ.store.management.StockChestEntity;
import com.fureniku.econ.store.management.StoreManagerBlock;
import com.fureniku.econ.store.management.StoreManagerEntity;
import com.fureniku.econ.store.management.TillBlock;
import com.fureniku.econ.store.management.TillEntity;

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
	public static BackToStockBlock back_to_stock = new BackToStockBlock("back_to_stock");
	
	public static StockChestBlockRotatable cardboard_box_closed = new StockChestBlockRotatable("cardboard_box_closed");
	public static StockChestBlockRotatable cardboard_box_open = new StockChestBlockRotatable("cardboard_box_open");
	
	public static StockChestBlock wooden_crate_oak = new StockChestBlock("wooden_crate_oak");
	public static StockChestBlock wooden_crate_spruce = new StockChestBlock("wooden_crate_spruce");
	public static StockChestBlock wooden_crate_birch = new StockChestBlock("wooden_crate_birch");
	public static StockChestBlock wooden_crate_jungle = new StockChestBlock("wooden_crate_jungle");
	public static StockChestBlock wooden_crate_acacia = new StockChestBlock("wooden_crate_acacia");
	public static StockChestBlock wooden_crate_dark_oak = new StockChestBlock("wooden_crate_dark_oak");
	
	public static TillBlock till = new TillBlock("till");
	
	public static ShelvesFullBlock shop_shelves_full  = new ShelvesFullBlock("shop_shelves_full");
	public static ShelvesFullBlock shop_shelves_half  = new ShelvesFullBlock("shop_shelves_half");
	public static ShelvesFullBlock shop_shelves_large = new ShelvesFullBlock("shop_shelves_large");
	
	
	public static void register(IForgeRegistry<Block> registry) {
		//Base stuff
		registry.register(atm_block);
		registry.register(store_manager);
		registry.register(cart_dispenser);
		registry.register(stock_chest);
		registry.register(back_to_stock);
		registry.register(till);
		
		registry.register(cardboard_box_closed);
		registry.register(cardboard_box_open);
		
		registry.register(wooden_crate_oak);
		registry.register(wooden_crate_spruce);
		registry.register(wooden_crate_birch);
		registry.register(wooden_crate_jungle);
		registry.register(wooden_crate_acacia);
		registry.register(wooden_crate_dark_oak);
		
		registry.register(shop_shelves_full);
		registry.register(shop_shelves_half);
		registry.register(shop_shelves_large);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(new EconomyItemBlock(atm_block).setRegistryName(atm_block.getRegistryName()));
		registry.register(new ItemBlock(store_manager).setRegistryName(store_manager.getRegistryName()));
		registry.register(new ItemBlock(cart_dispenser).setRegistryName(cart_dispenser.getRegistryName()));
		registry.register(new ItemBlock(stock_chest).setRegistryName(stock_chest.getRegistryName()));
		registry.register(new ItemBlock(back_to_stock).setRegistryName(back_to_stock.getRegistryName()));
		registry.register(new ItemBlock(till).setRegistryName(till.getRegistryName()));
		
		registry.register(new ItemBlock(cardboard_box_closed).setRegistryName(cardboard_box_closed.getRegistryName()));
		registry.register(new ItemBlock(cardboard_box_open).setRegistryName(cardboard_box_open.getRegistryName()));
		
		registry.register(new ItemBlock(wooden_crate_oak).setRegistryName(wooden_crate_oak.getRegistryName()));
		registry.register(new ItemBlock(wooden_crate_spruce).setRegistryName(wooden_crate_spruce.getRegistryName()));
		registry.register(new ItemBlock(wooden_crate_birch).setRegistryName(wooden_crate_birch.getRegistryName()));
		registry.register(new ItemBlock(wooden_crate_jungle).setRegistryName(wooden_crate_jungle.getRegistryName()));
		registry.register(new ItemBlock(wooden_crate_acacia).setRegistryName(wooden_crate_acacia.getRegistryName()));
		registry.register(new ItemBlock(wooden_crate_dark_oak).setRegistryName(wooden_crate_dark_oak.getRegistryName()));
		
		registry.register(new ItemBlock(shop_shelves_full).setRegistryName(shop_shelves_full.getRegistryName()));
		registry.register(new ItemBlock(shop_shelves_half).setRegistryName(shop_shelves_half.getRegistryName()));
		registry.register(new ItemBlock(shop_shelves_large).setRegistryName(shop_shelves_large.getRegistryName()));
	}
	
	public static void registerModels() {
		atm_block.initModel();
		store_manager.initModel();
		cart_dispenser.initModel();
		stock_chest.initModel();
		back_to_stock.initModel();
		till.initModel();
		
		cardboard_box_closed.initModel();
		cardboard_box_open.initModel();
		
		wooden_crate_oak.initModel();
		wooden_crate_spruce.initModel();
		wooden_crate_birch.initModel();
		wooden_crate_jungle.initModel();
		wooden_crate_acacia.initModel();
		wooden_crate_dark_oak.initModel();
		
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
		GameRegistry.registerTileEntity(BackToStockChestEntity.class, FurenikusEconomy.MODID + ":back_to_stock_chest");
		GameRegistry.registerTileEntity(TillEntity.class, FurenikusEconomy.MODID + ":till");
		
		GameRegistry.registerTileEntity(ShelvesFullEntity.class, FurenikusEconomy.MODID + ":floating_shelves_full");
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
