package com.silvaniastudios.econ.core;

import com.silvaniastudios.econ.core.items.EconItemBase;
import com.silvaniastudios.econ.core.items.DebitCardItem;
import com.silvaniastudios.econ.core.items.ItemMoney;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class CoreItems {		
	public static ItemMoney coin1 = new ItemMoney(1, "coin1");
	public static ItemMoney coin2 = new ItemMoney(2, "coin2");
	public static ItemMoney coin5 = new ItemMoney(5, "coin5");
	public static ItemMoney coin10 = new ItemMoney(10, "coin10");
	public static ItemMoney coin25 = new ItemMoney(25, "coin25");
	public static ItemMoney coin50 = new ItemMoney(50, "coin50");
	public static ItemMoney coin100 = new ItemMoney(100, "coin100");
	public static ItemMoney note100 = new ItemMoney(100, "note100");
	public static ItemMoney note200 = new ItemMoney(200, "note200");
	public static ItemMoney note500 = new ItemMoney(500, "note500");
	public static ItemMoney note1000 = new ItemMoney(1000, "note1000");
	public static ItemMoney note2000 = new ItemMoney(2000, "note2000");
	public static ItemMoney note5000 = new ItemMoney(5000, "note5000");
	public static ItemMoney note10000 = new ItemMoney(10000, "note10000");

	public static DebitCardItem debitCard = new DebitCardItem("debitCard");
	//public static Item storeStockPairer = new StoreStockPairer().setUnlocalizedName("storeStockPairer");
	
	//public static Item bankerSpawner = new BankerSpawnerItem().setUnlocalizedName("bankerSpawnerItem");
	//public static Item pliers = new PliersItem().setUnlocalizedName("pliers");
	
	public static EconItemBase plastic = new EconItemBase("plastic", 64);
	public static EconItemBase raw_plastic = new EconItemBase("raw_plastic", 64);
	public static EconItemBase small_pcb = new EconItemBase("small_pcb", 64);
	public static EconItemBase large_pcb = new EconItemBase("large_pcb", 64);
	public static EconItemBase atm_internals = new EconItemBase("atm_internals", 64);
	public static EconItemBase atm_screen = new EconItemBase("atm_screen", 64);
	public static EconItemBase atm_buttons = new EconItemBase("atm_buttons", 64);
	public static EconItemBase cpu = new EconItemBase("cpu", 64);
	
	
	
	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				coin1,
				coin2,
				coin5,
				coin10,
				coin25,
				coin50,
				coin100,
				note100,
				note200,
				note500,
				note1000,
				note2000,
				note5000,
				note10000,
				debitCard,
				plastic,
				raw_plastic,
				small_pcb,
				large_pcb,
				atm_internals,
				atm_screen,
				atm_buttons,
				cpu
		);
	}
	
	public static void registerModels() {
		coin1.registerItemModel();
		coin2.registerItemModel();
		coin5.registerItemModel();
		coin10.registerItemModel();
		coin25.registerItemModel();
		coin50.registerItemModel();
		coin100.registerItemModel();
		note100.registerItemModel();
		note200.registerItemModel();
		note500.registerItemModel();
		note1000.registerItemModel();
		note2000.registerItemModel();
		note5000.registerItemModel();
		note10000.registerItemModel();
		debitCard.registerItemModel();
		plastic.registerItemModel();
		raw_plastic.registerItemModel();
		small_pcb.registerItemModel();
		large_pcb.registerItemModel();
		atm_internals.registerItemModel();
		atm_screen.registerItemModel();
		atm_buttons.registerItemModel();
		cpu.registerItemModel();
	}	
}
