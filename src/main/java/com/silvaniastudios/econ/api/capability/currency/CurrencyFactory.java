package com.silvaniastudios.econ.api.capability.currency;

import java.util.concurrent.Callable;

public class CurrencyFactory implements Callable<ICurrency> {

	@Override
	public ICurrency call() throws Exception {
		return new CurrencyCapability();
	}

}
