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
			+ "If false, players can withdraw from ATMs and make card purchases at stores without ever needing to get a card.")
	public static boolean isCardRequired = true;
	@Config.Comment("The cost of getting a new card. Defaults to $10.00 (on default config settings). \n"
			+ "The first card is always free, this is for replacements if the first is lost.")
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
			+ "This is purely cosmetic and doesn't change how money works internally.")
	public static int currencyDecimals = 2;
}
