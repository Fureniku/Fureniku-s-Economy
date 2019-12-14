package com.silvaniastudios.econ.core.client.gui.storemanager;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.management.EnumLogType;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class LogListScrollable extends GuiScrollingList_Mod {
	
	private FontRenderer fontRenderer;
	private Minecraft client;
	private GuiStoreManager gui;
	private ArrayList<Integer> logId;
	private ArrayList<BlockPos> logLocation;
	private ArrayList<String> logExtra;
	
	private static final ResourceLocation TEXTURE_WIDGET = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_widget.png");

	public LogListScrollable(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight, FontRenderer font, Minecraft mc, GuiStoreManager gui,
			ArrayList<Integer> logId, ArrayList<BlockPos> logLocation, ArrayList<String> logExtra) {
		super(mc, width, height, top, height+top, left, entryHeight, screenWidth, screenHeight);
		this.fontRenderer = font;
		this.client = mc;
		this.gui = gui;
		this.logId = logId;
		this.logLocation = logLocation;
		this.logExtra = logExtra;
	}

	@Override
	protected int getSize() {
		return logId.size();
	}

	@Override protected void elementClicked(int index, boolean doubleClick) {}
	@Override protected boolean isSelected(int index) { return false; }
	@Override protected void drawBackground() {}
	
	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glColor4f(1,1,1,1);
        
		int left = (screenWidth - gui.xSize) / 2 + 277;
		int top = (screenHeight - gui.ySize) / 2;
		
		int slottWidth = 221;
		int slotHeight = 17;
		
		//Mouse hovering over this slot
		boolean hovering = (mouseX >= left && mouseX <= left + slottWidth && mouseY >= slotTop && mouseY >= top + 18 && mouseY <= slotTop + slotHeight && mouseY <= top + 18 + listHeight);

		int offset = hovering ? 96 : 0;
		this.client.getTextureManager().bindTexture(TEXTURE_WIDGET);
		gui.drawTexturedModalRect(left, slotTop, 0, 64+offset, slottWidth, slotHeight);
		
		int elementId = logId.size() - slotIdx - 1;
		
		//Invert the list, starting with the last element
		EnumLogType logType = EnumLogType.byId(logId.get(elementId));
		if (logType != null) {
			fontRenderer.drawString(I18n.format(logType.getShortMessage()), left + 7, slotTop+5, 0x404040);
		}
		
		//Create the tooltip with detailed info on the log entry
		//This is drawn later so it doesn't get overwritten by other list things.
		if (hovering) {
			createTooltip(elementId);
		}
	}
	
	protected void createTooltip(int index) {
		gui.renderTooltip = true;
		gui.tooltipList.clear();
		BlockPos pos = logLocation.get(index);
		EnumLogType logType = EnumLogType.byId(logId.get(index));
		
		if (logType.includeTextInString()) {
			gui.tooltipList.add(I18n.format(logType.getFullMessage(logExtra.get(index), pos.getX(), pos.getY(), pos.getZ())));
		} else {
			ArrayList<String> extra = new ArrayList<String>();
			System.out.println("log extra: " + logExtra.get(index));
			if (logExtra.get(index).length() > 0) {
				extra = splitString(logExtra.get(index));
			} else {
				extra.add("");
			}
			
			gui.tooltipList.add(I18n.format(logType.getFullMessage(extra.get(0), pos.getX(), pos.getY(), pos.getZ())));
			
			if (extra.get(0).length() > 0) {
				gui.tooltipList.addAll(extra);
			}
	}
	}
	
	private ArrayList<String> splitString(String str) {
		ArrayList<String> splitString = new ArrayList<>();
		int stringStart = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == EconConstants.Other.NEW_LINE_CHARACTER) {
				splitString.add(str.substring(stringStart, i));
				stringStart = i;
			}
		}
		//Add the last element which wouldn't trigger in the loops
		splitString.add(str.substring(stringStart, str.length()));
		return splitString;
	}
}
