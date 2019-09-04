package com.silvaniastudios.econ.core.client.gui;

import java.util.ArrayList;

import com.silvaniastudios.econ.api.EconUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ButtonShopWidget extends GuiButton {
	
	ArrayList<ItemStack> items;
	ArrayList<Integer> prices;
	RenderItem render;
	EconUtils utils = new EconUtils();

	public ButtonShopWidget(int buttonId, int x, int y, int widthIn, int heightIn, ArrayList<ItemStack> items, ArrayList<Integer> prices, RenderItem render) {
		super(buttonId, x, y, 200, 20 + (items.size()*20), "");
		this.items = items;
		this.prices = prices;
		this.render = render;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}
			for (int k = 0; k < items.size(); k++) {
				drawItemStack(items.get(k), x + 2, y + 10 + (items.size()*20), "");
				this.drawCenteredString(fontrenderer, utils.formatBalance(prices.get(k)), this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
			}
		}
	}
	
	//Borrowed from GuiContainer
	private void drawItemStack(ItemStack stack, int x, int y, String altText) {
		GlStateManager.translate(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		this.render.zLevel = 200.0F;
		net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
		this.render.renderItemAndEffectIntoGUI(stack, x, y);
		this.render.renderItemOverlayIntoGUI(font, stack, x, y, altText);
		this.zLevel = 0.0F;
		this.render.zLevel = 0.0F;
	}
}
