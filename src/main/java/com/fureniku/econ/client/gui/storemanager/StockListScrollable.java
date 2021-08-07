package com.fureniku.econ.client.gui.storemanager;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.network.OpenGuiServerSide;
import com.fureniku.econ.store.management.BackToStockChestEntity;
import com.fureniku.econ.store.management.StockChestEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class StockListScrollable extends GuiScrollingList_Mod {
	
	private FontRenderer fontRenderer;
	private Minecraft client;
	private GuiStoreManager gui;
	private ArrayList<StockChestEntity> stockChests;
	private ArrayList<BackToStockChestEntity> backToStocks;
	
	private static final ResourceLocation TEXTURE_WIDGET = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_widget.png");

	public StockListScrollable(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight, FontRenderer font, Minecraft mc, GuiStoreManager gui, ArrayList<StockChestEntity> stockChests, ArrayList<BackToStockChestEntity> backToStocks) {
		super(mc, width, height, top, height+top, left, entryHeight, screenWidth, screenHeight);
		this.fontRenderer = font;
		this.client = mc;
		this.gui = gui;
		this.stockChests = stockChests;
		this.backToStocks = backToStocks;
	}

	@Override
	protected int getSize() {
		return stockChests.size() + backToStocks.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		if (index < stockChests.size()) {
			FurenikusEconomy.log(3, "Stock Chest " + index + " has been clicked in stock manager at " + gui.te.getPos().getX() + ", " + gui.te.getPos().getY() + ", " + gui.te.getPos().getZ() + 
					". Attempting to remotely open the Stock Chest GUI.");
			StockChestEntity sce = stockChests.get(index);
			FurenikusEconomy.network.sendToServer(new OpenGuiServerSide(sce.getPos().getX(), sce.getPos().getY(), sce.getPos().getZ()));
		} else {
			BackToStockChestEntity sce = backToStocks.get(index-stockChests.size());
			FurenikusEconomy.network.sendToServer(new OpenGuiServerSide(sce.getPos().getX(), sce.getPos().getY(), sce.getPos().getZ()));
		}
	}

	@Override protected boolean isSelected(int index) { return false; }
	@Override protected void drawBackground() {}
	
	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glColor4f(1,1,1,1);
        
		int left = (screenWidth - gui.xSize) / 2 + 172;
		int top = (screenHeight - gui.ySize) / 2;
		
		int slottWidth = 89;
		int slotHeight = 32;
		
		//Mouse hovering over this slot
		boolean hovering = (mouseX >= left && mouseX <= left + slottWidth && mouseY >= slotTop && mouseY >= top + 18 && mouseY <= slotTop + slotHeight && mouseY <= top + 18 + listHeight);

		if (slotIdx < stockChests.size()) {
			StockChestEntity stockEntity = stockChests.get(slotIdx);
			if (stockEntity.inventory != null) {
				ItemStack stack = stockEntity.getInventoryTypeItemStack();

				//Change texture to a lighter variant when hovering over a shop
				int offset = hovering ? 96 : 0;
				this.client.getTextureManager().bindTexture(TEXTURE_WIDGET);
				gui.drawTexturedModalRect(left, slotTop, 0, 32+offset, slottWidth, slotHeight);
				
				if (stack != ItemStack.EMPTY) {
					drawItemStack(stack, left + 8, slotTop + 8);
				}
				fontRenderer.drawString(stockEntity.getInventoryTypeItemStack().getDisplayName(), left + 29, slotTop+6, 0x404040);
				fontRenderer.drawString("x" + stockEntity.getTotalItemNumber(), left + 29, slotTop+16, 0x404040);
				
				if (hovering) {
					createTooltip(stockEntity);
				}
			}
		} else {
			BackToStockChestEntity bts = backToStocks.get(slotIdx - stockChests.size());
			//Change texture to a lighter variant when hovering over a shop
			int offset = hovering ? 32 : 0;
			this.client.getTextureManager().bindTexture(TEXTURE_WIDGET);
			gui.drawTexturedModalRect(left, slotTop, 0, 192+offset, slottWidth, slotHeight);		
			
			fontRenderer.drawString(bts.stockChestName, left + 6, slotTop+6, 0x404040);
			if (bts.getInUseSlots() == 1) {
				fontRenderer.drawString(bts.getInUseSlots() + " item.", left + 6, slotTop+18, 0x404040);
			} else {
				fontRenderer.drawString(bts.getInUseSlots() + " items.", left + 6, slotTop+18, 0x404040);
			}
			if (hovering) {
				createTooltip(bts);
			}
		}
	}
	
	protected void createTooltip(StockChestEntity stockEntity) {
		gui.renderTooltip = true;
		gui.tooltipList.clear();
		gui.tooltipList.add(stockEntity.stockChestName);
		gui.tooltipList.add(stockEntity.getInventoryTypeItemStack().getDisplayName());
		gui.tooltipList.add("(" + stockEntity.getPos().getX() + ", " + stockEntity.getPos().getY() + ", " + stockEntity.getPos().getZ() + ")");
	}
	
	protected void createTooltip(BackToStockChestEntity bts) {
		gui.renderTooltip = true;
		gui.tooltipList.clear();
		gui.tooltipList.add(bts.stockChestName);
		gui.tooltipList.add(bts.getInUseSlots() + " items.");
		gui.tooltipList.add("(" + bts.getPos().getX() + ", " + bts.getPos().getY() + ", " + bts.getPos().getZ() + ")");
		gui.tooltipList.add("---------------------");
		for (int i = 0; i < bts.inventory.getSlots(); i++) {
			ItemStack stack = bts.inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				gui.tooltipList.add(stack.getDisplayName() + " x" + stack.getCount());
			}
		}
	}
	
	private void drawItemStack(ItemStack stack, int x, int y) {
		//Move the itemstack forward (towards the player) to give a "stacking" look
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
        client.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GlStateManager.translate(0.0F, 0.0F, -32.0F);
    }
}
