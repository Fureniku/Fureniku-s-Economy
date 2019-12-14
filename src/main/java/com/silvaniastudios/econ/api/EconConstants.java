package com.silvaniastudios.econ.api;

public class EconConstants {
	
	public static class Gui {
		public static final int ATM = 0;
		
		public static final int STORE_MANAGER_OWNER = 1;
		public static final int STORE_MANAGER_NOT_OWNER = 2;
		public static final int STOCK_CHEST_OWNER = 3;
		public static final int STOCK_CHEST_NOT_OWNER = 4;
		public static final int TILL_OWNER = 5;
		public static final int TILL_NOT_OWNER = 6;
		public static final int CART_DISPENSER_OWNER = 7;
		public static final int CART_DISPENSER_NOT_OWNER = 8;

		public static final int SHOP_SHELVES_FULL_OWNER = 9;
		public static final int SHOP_SHELVES_FULL_NOT_OWNER = 10;
		public static final int SHOP_SHELVES_HALF_OWNER = 11;
		public static final int SHOP_SHELVES_HALF_NOT_OWNER = 12;
		public static final int SHOP_SHELVES_LARGE_OWNER = 13;
		public static final int SHOP_SHELVES_LARGE_NOT_OWNER = 14;
		public static final int VENDING_MACHINE_OWNER = 15;
		public static final int VENDING_MACHINE_NOT_OWNER = 16;
	}
	
	public static class Inventories {
		public static final int SHELVES_FULL_SIZE = 4;
		public static final int SHELVES_HALF_SIZE = 2;
		public static final int SHELVES_LARGE_SIZE = 2;
		
		public static final int STOCK_CHEST_SIZE = 73;
	}
	
	public static class Other {
		public static final char NEW_LINE_CHARACTER = '¶'; //needed something very unlikely to ever appear in normal text...
	}

}
