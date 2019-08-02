package com.silvaniastudios.econ.api.capability.currency;

public class CurrencyCapability implements ICurrency {
	
	private static long money;
	
	public long getMoney() {
		return money;
	}
	
	public void setMoney(long m) {
		money = m;
	}

	@Override
	public void addMoney(long m) {
		money = money + m;
	}

	@Override
	public boolean subtractMoney(long m) {
		if (money - m >= 0) {
			money = money - m;
			return true;
		}
		return false;
	}

}
