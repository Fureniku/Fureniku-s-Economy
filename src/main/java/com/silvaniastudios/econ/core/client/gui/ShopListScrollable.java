package com.silvaniastudios.econ.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class ShopListScrollable extends GuiScrollingList {

	public ShopListScrollable(int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
	}

	@Override
	protected int getSize() {
		return 0;
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isSelected(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void drawBackground() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		// TODO Auto-generated method stub
		
	}

}
