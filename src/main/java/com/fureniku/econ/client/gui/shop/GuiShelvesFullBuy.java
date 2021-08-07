package com.fureniku.econ.client.gui.shop;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.shop.ShelvesFullEntity;
import com.fureniku.econ.blocks.shop.containers.ShelvesFullContainerBuy;
import com.fureniku.econ.network.AddToCartPacket;
import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiShelvesFullBuy extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/floating_shelves_full_buy.png");
	
	ShelvesFullEntity te;
	
	private GuiButton buy_1;
	private GuiButton buy_2;
	private GuiButton buy_3;
	private GuiButton buy_4;
	
	public GuiShelvesFullBuy(ShelvesFullEntity entity, ShelvesFullContainerBuy inventorySlotsIn) {
		super(inventorySlotsIn);
		this.te = entity;
	}

	protected int xSize = 176;
	protected int ySize = 208;
	
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
		fontRenderer.drawString(te.ownerName, xSize - 7 - fontRenderer.getStringWidth(te.ownerName), -14, 0x404040);
		
		if (sm != null) {
			fontRenderer.drawString(sm.getShopName(), 7, -14, 0x404040);
			fontRenderer.drawString(te.shopName, 7, -2, 0x404040);
		}
		
		fontRenderer.drawString(EconUtils.formatBalance(te.priceList[0]), 31, 14, 0x404040);
		fontRenderer.drawString(EconUtils.formatBalance(te.priceList[1]), 31, 38, 0x404040);
		fontRenderer.drawString(EconUtils.formatBalance(te.priceList[2]), 31, 62, 0x404040);
		fontRenderer.drawString(EconUtils.formatBalance(te.priceList[3]), 31, 86, 0x404040);
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button == buy_1) {
			System.out.println("Buying slot 1");
			FurenikusEconomy.network.sendToServer(new AddToCartPacket(0));
		}
		if (button == buy_2) {
			FurenikusEconomy.network.sendToServer(new AddToCartPacket(1));	
		}
		if (button == buy_3) {
			FurenikusEconomy.network.sendToServer(new AddToCartPacket(2));
		}
		if (button == buy_4) {
			FurenikusEconomy.network.sendToServer(new AddToCartPacket(3));
		}
	}
	
	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawString(text, (x - fontRendererIn.getStringWidth(text) / 2), y, color);
    }
	
	@Override
	public void initGui() {
		super.initGui();
		
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        buy_1 = new GuiButton(0, left + 99, top + 29,  70, 20, I18n.format("econ.tooltip.shops.add_to_cart"));
        buy_2 = new GuiButton(1, left + 99, top + 53,  70, 20, I18n.format("econ.tooltip.shops.add_to_cart"));
        buy_3 = new GuiButton(2, left + 99, top + 77,  70, 20, I18n.format("econ.tooltip.shops.add_to_cart"));
        buy_4 = new GuiButton(3, left + 99, top + 101, 70, 20, I18n.format("econ.tooltip.shops.add_to_cart"));

    	buttonList.add(buy_1);
    	buttonList.add(buy_2);
    	buttonList.add(buy_3);
    	buttonList.add(buy_4);
    	
    	if (te.priceList[0] <= 0 || te.inventory.getStackInSlot(0).isEmpty()) {
    		buy_1.enabled = false;
    	}
    	if (te.priceList[1] <= 0 || te.inventory.getStackInSlot(1).isEmpty()) {
    		buy_2.enabled = false;
    	}
    	if (te.priceList[2] <= 0 || te.inventory.getStackInSlot(2).isEmpty()) {
    		buy_3.enabled = false;
    	}
    	if (te.priceList[3] <= 0 || te.inventory.getStackInSlot(3).isEmpty()) {
    		buy_4.enabled = false;
    	}
	}
}
