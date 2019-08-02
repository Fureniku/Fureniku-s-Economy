package com.silvaniastudios.econ.api.capability.currency;

public interface ICurrency {
	
	public long getMoney();
	public void setMoney(long m);
	public void addMoney(long m);
	public boolean subtractMoney(long m);

}
