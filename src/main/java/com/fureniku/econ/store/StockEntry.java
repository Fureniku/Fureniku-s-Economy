package com.fureniku.econ.store;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StockEntry {
	
	private ItemStack stack; //The itemset (can be a singular item or a specific stacksize of item. The amount of items IS relevant.)
	private int id; //A unique (per store manager) ID. Once assigned it's forever consumed until the manager is broken.
	private int priceMin;
	private int priceDefault;
	private int priceMax;
	
	private int[] linked; //An array of other stock entries which should share the price fluctuation properties (for example, alternate stack sizes, different colours)
	
	private boolean stockAffected;
	private int minStock; //The point where max price will be charged
	private int maxStock; //The point where min price is charged.
	
	private boolean timeAffected;
	private int cooldownPerTransaction;
	private int cooldownLowerThreshold;
	private int cooldownUpperThreshold;
	
	private boolean levelIsPerPlayer = false;
	private float level = 0.5f; //Value between 0-1 to scale price by. 0 = min, 1 = max, 0.5 = default
	
	//Full constructor
	public StockEntry(ItemStack stack, int id, int priceMin, int priceDefault, int priceMax, int minStock, int maxStock, int cooldownPerTransaction, int[] linked, boolean levelIsPerPlayer) {
		this.stack = stack;
		this.id = id;
		this.priceMin = priceMin;
		this.priceDefault = priceDefault;
		this.priceMax = priceMax;
		this.linked = linked;
		
		stockAffected = (minStock > 0 && maxStock > 0);
		this.minStock = minStock;
		this.maxStock = maxStock;
		
		timeAffected = cooldownPerTransaction > 0;
		this.cooldownPerTransaction = cooldownPerTransaction;
	}
	
	//Constructor for time-based stock pricing
	public StockEntry(ItemStack stack, int id, int priceMin, int priceDefault, int priceMax, int linked[]) {
		this.stack = stack;
		this.id = id;
		this.priceMin = priceMin;
		this.priceDefault = priceDefault;
		this.priceMax = priceMax;
		this.linked = linked;
	}
	
	public StockEntry(ItemStack stack, int id, int priceMin, int priceDefault, int priceMax) {
		this(stack, id, priceMin, priceDefault, priceMax, new int[0]);
	}
	
	//Simple stock with no dynamic pricing
	public StockEntry(ItemStack stack, int id, int price) {
		
	}
	
	
	public ItemStack getStack() {
		return stack;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPrice() {
		return priceDefault;
	}
	
	public static NBTTagCompound serializeNBT(StockEntry stock) {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound item = new NBTTagCompound();
		stock.stack.writeToNBT(item);
		nbt.setTag("item", item);
		nbt.setInteger("id", stock.id);
		nbt.setInteger("priceMin", stock.priceMin);
		nbt.setInteger("priceDefault", stock.priceDefault);
		nbt.setInteger("priceMax", stock.priceMax);
		
		nbt.setIntArray("linked", stock.linked);
		nbt.setFloat("level", stock.level);
		
		return nbt;
	}
	
	public static StockEntry deserializeNbt(World world, NBTTagCompound nbt) {
		return new StockEntry(new ItemStack((NBTTagCompound) nbt.getTag("item")), nbt.getInteger("id"), nbt.getInteger("priceMin"), nbt.getInteger("priceDefault"), nbt.getInteger("priceMax"), nbt.getIntArray("linked"));
		
	}
}
