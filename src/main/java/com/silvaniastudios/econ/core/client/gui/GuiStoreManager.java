package com.silvaniastudios.econ.core.client.gui;

import java.util.ArrayList;

import com.silvaniastudios.econ.api.store.management.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.shops.ShopBaseEntity;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiStoreManager extends GuiContainer {
	
	private static final ResourceLocation TEXTURE_LEFT = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_left.png");
	private static final ResourceLocation TEXTURE_RIGHT = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_right.png");
	StoreManagerEntity te;
	
	ShopListScrollable list;
	
	private int buttonId = 2;

	public GuiStoreManager(StoreManagerEntity entity, StoreManagerContainer container) {
		super(container);
		this.te = entity;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
        this.mc.getTextureManager().bindTexture(TEXTURE_LEFT);
		drawTexturedModalRect(left, top, 0, 0, 256, ySize);
		
		this.mc.getTextureManager().bindTexture(TEXTURE_RIGHT);
		drawTexturedModalRect(left+256, top, 0, 0, 128, ySize);
		
		list.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void initGui() {
		super.initGui();
		
		list = new ShopListScrollable(100, 400, 10, 410, 10, 100, width, height);
		
		for (int i = 0; i < te.shopPosArray.size(); i++) {
			buttonList.add(addShopElementToList(i));
		}
		
		list.registerScrollButtons(this.buttonList, -1, -1);

	}
	
	protected ButtonShopWidget addShopElementToList(int id) {
		BlockPos shopPos = te.shopPosArray.get(id);
		TileEntity e = Minecraft.getMinecraft().world.getTileEntity(shopPos);
		
		if (e instanceof ShopBaseEntity) {
			ShopBaseEntity shopEntity = (ShopBaseEntity) e;
			int slots = shopEntity.inventory.getSlots();
			
			ArrayList<ItemStack> items = new ArrayList<>();
			ArrayList<Integer> prices = new ArrayList<>();
			
			for (int i = 0; i < slots; i++) {
				items.add(shopEntity.inventory.getStackInSlot(i));
				prices.add(shopEntity.priceList[i]);
			}
			
			return new ButtonShopWidget(buttonId-1, 0, 0, 100, 400, items, prices, this.itemRender);
		}
		return null;
	}

}
