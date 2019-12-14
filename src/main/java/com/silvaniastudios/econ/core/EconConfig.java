package com.silvaniastudios.econ.core;

import net.minecraftforge.common.config.Config;

@Config(modid = FurenikusEconomy.MODID, name = "FurenikusEconomy")
@Config.LangKey("econ.config.title")
public class EconConfig {
	
	@Config.Comment("Can players create their own shops, or are only server-ran shops (made in creative mode) available?")
	public static boolean playerOwnedShops = true;
	
	@Config.Comment("Will mobs drop money when they die?")
	public static boolean mobsDropMoney = true;
	
	@Config.Comment("Can players use cards in a shop, or are they cash-only?")
	public static boolean allowCardPurchases = true;
	
	@Config.Comment("Is the player required to have a debit card to do bank-related transactions? \n"
			+ "If false, players can withdraw from ATMs and make card purchases at stores without ever needing to get a card. \n"
			+ "Slightly easier for the player, but also less realistic.")
	public static boolean isCardRequired = true;
	
	@Config.Comment("The cost of getting a new card. Defaults to $10.00 (on default config settings). \n"
			+ "The first card is always free, this is for replacements if the first is lost. \n"
			+ "The cost can be taken from their bank account.")
	public static int replacementCardCost = 1000;
	
	@Config.Comment("Used for console output. For logging simplicity, currencySign is ignored and always uses $ for the log."
			+ "\n0: Only major errors/problems are logged. "
			+ "\n1: All shop and ATM transactions are logged."
			+ "\n2: All interactions are logged (e.g. a player checking their balance at an ATM"
			+ "\n3: Everything is logged. Very spammy.")
	
	public static int debugLevel = 0;
	@Config.Comment("The symbol used for money in GUIs etc. \n"
			+ "For convenience, popular choices are $, €, £, ¥")
	public static String currencySign = "$";
	
	@Config.Comment("The number of decimal places money should be formatted with. \n"
			+ "Defaults to two (eg, 1.00)."
			+ "This is purely cosmetic and doesn't change how money works internally."
			+ "If you for example set this to three, then the lowest coin will be worth 0.010, not 0.001.")
	public static int currencyDecimals = 2;
	
	@Config.Comment("How long (in minutes) before a players shopping cart should time out and close")
	public static int cartTimeout = 15;
	
	@Config.Comment("How long (in seconds) before a till's secure storage should time out. \n"
			+ "When a player makes a cart purchase, their items are moved to the till for them to withdraw to their inventory. \n"
			+ "After this time period elapses, any items still in the till are ejected onto the floor, freeing up the till for the next customer.")
	public static int tillTimeout = 60;
	
	@Config.Comment("The maximum distance a player can be from the store manager before their cart is closed")
	public static int maxDistance = 64;
	
	@Config.Comment("Set to false to turn off the \"Next time, try shift-right clicking...\" message when depositing individual money items to an ATM.")
	public static boolean fastDepositNotification = true;
}
