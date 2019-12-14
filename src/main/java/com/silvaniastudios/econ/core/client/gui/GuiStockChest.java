package com.silvaniastudios.econ.core.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.management.StockChestContainer;
import com.silvaniastudios.econ.api.store.management.StockChestEntity;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.network.StockUpdatePacket;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiStockChest extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/stock_chest_large.png");
	
	StockChestEntity te;
	
	private GuiTextField chest_name;
	
	 public GuiStockChest(StockChestEntity entity, StockChestContainer inventorySlotsIn) {
		super(inventorySlotsIn);
		this.te = entity;
	}
	 
	public EconUtils econ = new EconUtils();

	protected int xSize = 256;
	protected int ySize = 256;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
        this.mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		StoreManagerEntity sm = te.getManager();
		fontRenderer.drawString("Shop Owner: " + te.ownerName, -33, -38, 0x404040);
		
		if (sm != null) {
			fontRenderer.drawString("Manager: " + te.managerPos.getX() + ", " + te.managerPos.getY() + ", " + te.managerPos.getZ(), -33, -28, 0x404040);
			fontRenderer.drawString("Store Name: " + sm.shopName, -33, -18, 0x404040);
		} else {
			fontRenderer.drawString("Manager not set", -33, -18, 0x404040);
		}
		
		
		chest_name.drawTextBox();
	}
	
	@Override
	public void keyTyped(char c, int i) throws IOException {
		if (i == Keyboard.KEY_ESCAPE) {
			FurenikusEconomy.network.sendToServer(new StockUpdatePacket(chest_name.getText(), false));
			super.keyTyped(c, i);
		}
		chest_name.textboxKeyTyped(c, i);
	}
	
	@Override
	public void initGui() {
		super.initGui();
        
        chest_name = new GuiTextField(4, this.fontRenderer, 7, -7, 162, 18);
        chest_name.setText(te.stockChestName);
        
        if (chest_name.getText().length() == 0) {
        	chest_name.setText("Stock Chest");
        }
        
        chest_name.setFocused(true);
	}

	public int parseInt(String s) {
		try {
			return Integer.parseInt("" + s);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
}
