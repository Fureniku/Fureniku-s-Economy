package com.fureniku.econ.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiCurrencyField extends GuiTextField {

	public GuiCurrencyField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
			int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void writeText(String textToWrite) {
		super.writeText(textToWrite);
		//Remove dollar, leading zeros and decimal points from previous formatting
		String newText = this.getText().replace("$0", "").replace("$", "").replace(".", "");
		if (newText.length() > 2) {
			this.setText("$" + newText.substring(0, newText.length()-2) + "." + newText.substring(newText.length()-2, newText.length()));
		} else {
			this.setText("$0." + newText);
		}
	}
	
	public int getValue() {
		return parseInt(this.getText().replace("$", "").replace(".", ""));
	}
	
	public int parseInt(String s) {
		try {
			return Integer.parseInt("" + s);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
}
