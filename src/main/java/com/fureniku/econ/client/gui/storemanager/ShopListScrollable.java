package com.fureniku.econ.client.gui.storemanager;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.network.OpenGuiServerSide;
import com.fureniku.econ.store.shops.ShopBaseEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class ShopListScrollable extends GuiScrollingList_Mod {
	
	private FontRenderer fontRenderer;
	private Minecraft client;
	private GuiStoreManager gui;
	private ArrayList<ShopBaseEntity> shops;
	
	private static final ResourceLocation TEXTURE_WIDGET = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_widget.png");

	public ShopListScrollable(int width, int height, int top, int left, int entryHeight, int screenWidth, int screenHeight, FontRenderer font, Minecraft mc, GuiStoreManager gui, ArrayList<ShopBaseEntity> shops) {
		super(mc, width, height, top, height+top, left, entryHeight, screenWidth, screenHeight);
		this.fontRenderer = font;
		this.client = mc;
		this.gui = gui;
		this.shops = shops;
	}

	@Override
	protected int getSize() {
		return shops.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		FurenikusEconomy.log(3, "Shop Interface " + index + " has been clicked in stock manager at " + gui.te.getPos().getX() + ", " + gui.te.getPos().getY() + ", " + gui.te.getPos().getZ() + 
				". Attempting to remotely open the Shop Interface GUI.");
		ShopBaseEntity sce = shops.get(index);
		FurenikusEconomy.network.sendToServer(new OpenGuiServerSide(sce.getPos().getX(), sce.getPos().getY(), sce.getPos().getZ()));
	}

	@Override protected boolean isSelected(int index) { return false; }
	@Override protected void drawBackground() {}
	
	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glColor4f(1,1,1,1);
        
		int left = (screenWidth - gui.xSize)/2 + 12;
		int top = (screenHeight - gui.ySize) / 2;
		
		int slottWidth = 144;
		int slotHeight = 32;
		
		//Mouse hovering over this slot
		boolean hovering = (mouseX >= left && mouseX <= left + slottWidth && mouseY >= slotTop && mouseY >= top + 18 && mouseY <= slotTop + slotHeight && mouseY <= top + 18 + listHeight);

		ShopBaseEntity shopEntity = shops.get(slotIdx);
		if (shopEntity.inventory != null) {
			int slots = shopEntity.inventory.getSlots();
			
			ArrayList<ItemStack> items = new ArrayList<>();
			
			for (int i = 0; i < slots; i++) {
				ItemStack stack = shopEntity.inventory.getStackInSlot(i);
				if (stack != ItemStack.EMPTY) {
					items.add(shopEntity.inventory.getStackInSlot(i));
				}
			}
			
			int slotCount = items.size() > 5 ? 5 : items.size();
			
			//Draw overlapping items for a quick view of what the shop interface sells
			for (int i = 0; i < slotCount; i++) {
				drawItemStack(items.get(i), left + 132 + (i*8) - (slotCount * 8), slotTop + 13, 30.0F - (i * 5.0F));
			}
			
			//Change texture to a lighter variant when hovering over a shop
			int offset = hovering ? 96 : 0;
			this.client.getTextureManager().bindTexture(TEXTURE_WIDGET);
			gui.drawTexturedModalRect(left, slotTop, 0, 0+offset, slottWidth, slotHeight);
			
			fontRenderer.drawString(TextFormatting.DARK_GRAY + shopEntity.shopName, left + 7, slotTop+6, 0x404040);
			fontRenderer.drawString(TextFormatting.GRAY + "(" + shopEntity.getPos().getX() + ", " + shopEntity.getPos().getY() + ", " + shopEntity.getPos().getZ() + ")", left + 7, slotTop+18, 0x404040);
			
			//Create the tooltip with detailed info on the shop interface when hovering over its slot
			//This is drawn later so it doesn't get overwritten by other list things.
			if (hovering) {
				createTooltip(slotIdx, items);
			}
		}
	}
	
	protected void createTooltip(int shopId, ArrayList<ItemStack> items) {
		gui.renderTooltip = true;
		ShopBaseEntity shop = shops.get(shopId);
		gui.tooltipList.clear();
		
		gui.tooltipList.add(shop.shopName);
		
		for (int i = 0; i < items.size(); i++) {
			gui.tooltipList.add(EconUtils.formatBalance(shop.priceList[i]) + " - " + items.get(i).getDisplayName() + " x" + items.get(i).getCount());
		}
		//Only draw it if there's actually items in the interface, not empty slots.
		if (items.size() <= 0) {
			gui.tooltipList.clear();
		}
	}
	
	private void drawItemStack(ItemStack stack, int x, int y, float z) {
		//Move the itemstack forward (towards the player) to give a "stacking" look
		GlStateManager.translate(0.0F, 0.0F, 32.0F+z);
        client.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        GlStateManager.translate(0.0F, 0.0F, -32.0F-z);
    }
}
