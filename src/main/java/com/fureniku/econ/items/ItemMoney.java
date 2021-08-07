package com.fureniku.econ.items;

import com.fureniku.econ.FurenikusEconomy;

public class ItemMoney extends EconItemBase {
	
    public long moneyValue;

    public ItemMoney(long money, String name) {
        super(name, 50);
        this.moneyValue = money;
        this.setCreativeTab(FurenikusEconomy.tabEcon);
    }

    public long getMoneyValue() {
    	return moneyValue;
    }

}