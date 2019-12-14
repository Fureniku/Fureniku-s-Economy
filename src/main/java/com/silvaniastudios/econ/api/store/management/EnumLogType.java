package com.silvaniastudios.econ.api.store.management;

import java.util.ArrayList;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public enum EnumLogType {
	SHOP_ADDED(0, "shop_added", TextFormatting.GREEN),
	SHOP_REMOVED(1, "shop_removed", TextFormatting.RED),
	STOCK_ADDED(2, "stock_added", TextFormatting.GREEN),
	STOCK_REMOVED(3, "stock_removed", TextFormatting.RED),
	TILL_ADDED(4, "till_added", TextFormatting.GREEN),
	TILL_REMOVED(5, "till_removed", TextFormatting.RED),
	CART_ADDED(6, "cart_added", TextFormatting.GREEN),
	CART_REMOVED(7, "cart_removed", TextFormatting.RED),
	WITHDRAW_BALANCE(8, "withdraw_balance", TextFormatting.BLUE),
	OPEN_SHOP(9, "open_shop", TextFormatting.DARK_GREEN),
	CLOSE_SHOP(10, "close_shop", TextFormatting.DARK_RED);
	
	private static final EnumLogType[] LOOKUP_ARRAY = new EnumLogType[values().length];
	private final int id;
	private final String type;
	private final TextFormatting format;
	
	private EnumLogType(int id, String type, TextFormatting format) {
		this.id = id;
		this.type = type;
		this.format = format;
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getId() {
        return this.id;
    }
	
	public TextFormatting getFormat() {
		return format;
	}
	
	public boolean includeTextInString() {
		if (getId() == EnumLogType.WITHDRAW_BALANCE.getId()) {
			return true;
		}
		return false;
	}
	
	public String getShortMessage() {
		return getFormat() + colourByNumbers(I18n.format("econ.gui.store_manager.log." + type + "_short"));
	}
	
	public String getFullMessage(String str, int x, int y, int z) {
		if (x != 0 && y != 0 && z != 0) {
			return getFormat() + colourByNumbers(I18n.format("econ.gui.store_manager.log." + type + "_full", x, y, z));
		}
		if (includeTextInString()) {
			return getFormat() + colourByNumbers(I18n.format("econ.gui.store_manager.log." + type + "_full", str));
		}
		return getFormat() + colourByNumbers(I18n.format("econ.gui.store_manager.log." + type + "_full"));
	}
	
	@SuppressWarnings("deprecation")
	public String getFullMessageForServer(String str, int x, int y, int z) {
		if (x != 0 && y != 0 && z != 0) {
			return net.minecraft.util.text.translation.I18n.translateToLocalFormatted("econ.gui.store_manager.log." + type + "_full", x, y, z);
		}
		if (includeTextInString()) {
			return net.minecraft.util.text.translation.I18n.translateToLocalFormatted("econ.gui.store_manager.log." + type + "_full", str);
		}
		return net.minecraft.util.text.translation.I18n.translateToLocal("econ.gui.store_manager.log." + type + "_full");
	}
	
	public static EnumLogType byId(int id) {
        if (id < 0 || id >= LOOKUP_ARRAY.length) {
        	id = 0;
        }
        
        return LOOKUP_ARRAY[id];
    }
	
	//who doesn't love colouring by numbers!
	//(Colours any numbers (coords, prices etc) as yellow, while leaving the rest of the string as whatever it should be)
	public String colourByNumbers(String str) {
		ArrayList<String> splitString = new ArrayList<>();
		boolean n = false; //switch to true when numbers start, and back to false when they end.
		int stringStart = 0; //each time we start a new string, set to the current index
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((Character.isDigit(c) || c == '$' || c == '-') && n == false) {
				n = true;
				splitString.add(getFormat().toString() + str.substring(stringStart, i));
				stringStart = i;
				
				
			} else if (Character.isAlphabetic(str.charAt(i)) && n == true) {
				n = false;
				splitString.add(TextFormatting.YELLOW.toString() + str.substring(stringStart, i));
				stringStart = i;
			}
		}
		//Add the last element which wouldn't trigger in the loops
		splitString.add(getFormat().toString() + str.substring(stringStart, str.length()));

		String newString = "";
		for (int i = 0; i < splitString.size(); i++) {
			newString += splitString.get(i);
		}
		
		return newString;
	}
	
	static {
        for (EnumLogType type: values()) {
        	LOOKUP_ARRAY[type.getId()] = type;
        }
    }
}
