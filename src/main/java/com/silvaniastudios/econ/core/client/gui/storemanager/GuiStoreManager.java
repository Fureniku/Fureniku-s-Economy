package com.silvaniastudios.econ.core.client.gui.storemanager;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.management.StockChestEntity;
import com.silvaniastudios.econ.api.store.management.StoreManagerContainer;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.api.store.shops.ShopBaseEntity;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.network.StoreManagerClearLogPacket;
import com.silvaniastudios.econ.network.StoreManagerUpdatePacket;
import com.silvaniastudios.econ.network.StoreManagerWithdrawPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class GuiStoreManager extends GuiContainer {
	
	private static final ResourceLocation TEXTURE_LEFT = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_left.png");
	private static final ResourceLocation TEXTURE_RIGHT = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/store_manager_right.png");
	StoreManagerEntity te;
	
	ShopListScrollable listShops;
	StockListScrollable listStock;
	LogListScrollable listLog;
	
	private GuiTextField store_name;
	private GuiTextField budget_adjust;
	
	private GuiButton showHideShops;
	private GuiButton showHideStockChests;
	private GuiButton openCloseShop;
	private GuiButton clearLog;
	
	private GuiButton withdrawCashFromShop;
	private GuiButton toggleShopWideBudget;
	private GuiButton addToBudget;
	private GuiButton removeFromBudget;
	
	boolean show_hidden_shops = false;
	boolean show_hidden_stock_chests = false;
	boolean shop_open = true;
	boolean shop_budget = false;
	
	EconUtils econ = new EconUtils();
	ArrayList<String> tooltipList = new ArrayList<String>();
	boolean renderTooltip = false;
	
	protected int xSize = 512;
    protected int ySize = 256;

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
		drawTexturedModalRect(left+256, top, 0, 0, 256, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int left = -168;
		int top = -45;
		
		fontRenderer.drawString(I18n.format("econ.gui.store_manager.shop_interface_title"), left+12, top+7, 0x404040);
		fontRenderer.drawString(I18n.format("econ.gui.store_manager.stock_chest_title"), left+172, top+7, 0x404040);
		fontRenderer.drawString(I18n.format("econ.gui.store_manager.log_title"), left+277, top+7, 0x404040);
		
		fontRenderer.drawString(I18n.format("econ.gui.store_manager.balance_title"), left+277, top+130, 0x404040);
		fontRenderer.drawString(I18n.format("econ.gui.store_manager.budget_title"), left+277, top+166, 0x404040);
		
		
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		fontRenderer.drawString(I18n.format("" + econ.formatBalance(te.balance)), (left+277)/2, (top+142)/2, 0x404040);
		fontRenderer.drawString(I18n.format("$0.00"), (left+277)/2, (top+178)/2, 0x404040); //TODO
		GL11.glScalef(0.5F, 0.5F, 0.5F);

		store_name.drawTextBox();
		budget_adjust.drawTextBox();
		
		showHideShops.displayString = show_hidden_shops ? I18n.format("econ.gui.store_manager.show_all") : I18n.format("econ.gui.store_manager.show_active");
		showHideStockChests.displayString = show_hidden_stock_chests ? I18n.format("econ.gui.store_manager.show_all") : I18n.format("econ.gui.store_manager.show_active");
		openCloseShop.displayString = shop_open ? I18n.format("econ.gui.store_manager.close_shop") : I18n.format("econ.gui.store_manager.open_shop");
		toggleShopWideBudget.displayString = shop_budget ? ChatFormatting.DARK_GREEN + I18n.format("econ.gui.store_manager.hide_budget") : ChatFormatting.RED + I18n.format("econ.gui.store_manager.show_budget");
	}
	
	public void initGui() {
		super.initGui();
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
        show_hidden_shops = te.show_hidden_shops_ui;
    	show_hidden_stock_chests = te.show_hidden_stock_chests_ui;
    	shop_open = te.shop_open;
    	shop_budget = te.shop_budget;
        
        ArrayList<ShopBaseEntity> shops = new ArrayList<ShopBaseEntity>();
        ArrayList<StockChestEntity> stockChests = new ArrayList<StockChestEntity>();
        
        for (int i = 0; i < te.shopPosArray.size(); i++) {
	        BlockPos shopPos = te.shopPosArray.get(i);
			TileEntity e = Minecraft.getMinecraft().world.getTileEntity(shopPos);
	        
	        if (e instanceof ShopBaseEntity) {
				ShopBaseEntity shopEntity = (ShopBaseEntity) e;
				shops.add(shopEntity);
	        }
        }
        
        for (int i = 0; i < te.stockPosArray.size(); i++) {
	        BlockPos shopPos = te.stockPosArray.get(i);
			TileEntity e = Minecraft.getMinecraft().world.getTileEntity(shopPos);
	        
	        if (e instanceof StockChestEntity) {
	        	StockChestEntity shopEntity = (StockChestEntity) e;
				stockChests.add(shopEntity);
	        }
        }
        

        listShops = new ShopListScrollable(150, 176, top+19, left+12, 32, width, height, fontRenderer, mc, this, shops);
        listStock = new StockListScrollable(95, 176, top+19, left+172, 32, width, height, fontRenderer, mc, this, stockChests);
        listLog = new LogListScrollable(228, 106, top+19, left+277, 17, width, height, fontRenderer, mc, this, te.shopInteractions, te.shopInteractionsLocations, te.shopInteractionsExtra);
        
        showHideShops 		= new GuiButton(3, left +  11, top + 200, 152, 20, "show/hide shops");
    	showHideStockChests = new GuiButton(4, left + 171, top + 200,  98, 20, "show/hide stock");
    	openCloseShop 		= new GuiButton(5, left + 171, top + 225,  98, 20, "open/close shop");
    	clearLog 			= new GuiButton(6, left + 495, top + 8, 10, 10, TextFormatting.RED + "X");

    	withdrawCashFromShop 	= new GuiButton(7, left + 445, top + 130, 60, 20, I18n.format("econ.gui.store_manager.withdraw"));
    	toggleShopWideBudget 	= new GuiButton(8, left + 276, top + 225, 63, 20, "Toggle Budget");
    	addToBudget 			= new GuiButton(9, left + 343, top + 200, 68, 20, "Add $");
    	removeFromBudget 		= new GuiButton(10, left + 415, top + 200, 68, 20, "Remove $");
        
        left = -168;
		top = -45;
		
        store_name = new GuiTextField(0, this.fontRenderer, -156, 180, 151, 20);
        budget_adjust = new GuiTextField(1,this.fontRenderer, left + 277, top + 200, 60, 20);
        
        store_name.setText(te.shopName);
        budget_adjust.setText("0");
        store_name.setFocused(true);
        
        this.buttonList.add(showHideShops);
        this.buttonList.add(showHideStockChests);
        this.buttonList.add(openCloseShop);
    	this.buttonList.add(clearLog);
    	
    	this.buttonList.add(withdrawCashFromShop);
    	this.buttonList.add(toggleShopWideBudget);
    	this.buttonList.add(addToBudget);
    	this.buttonList.add(removeFromBudget);
	}
	
	@Override
	public void keyTyped(char c, int i) throws IOException {
		if (i == Keyboard.KEY_ESCAPE) {
			sendUpdate();
			super.keyTyped(c, i);
		}
		
		if (i == Keyboard.KEY_TAB) {
			if (store_name.isFocused()) {
				store_name.setFocused(false);
				budget_adjust.setFocused(true);
			} else {
				budget_adjust.setFocused(false);
				store_name.setFocused(true);
			}
		}
		
		if (store_name.isFocused()) {
			store_name.textboxKeyTyped(c, i);
		} else {
			if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' ||
				c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
				i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT) {
				//don't make people delete the leading zero, just replace it.
				if (budget_adjust.getText() == "0") {
					budget_adjust.setText("");
				}
				
				budget_adjust.textboxKeyTyped(c, i);
			}
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button == showHideShops) {
			if (show_hidden_shops) {
				show_hidden_shops = false;
			} else { 
				show_hidden_shops = true;
			}
			sendUpdate();
		}
		if (button == showHideStockChests) {
			if (show_hidden_stock_chests) {
				show_hidden_stock_chests = false;
			} else {
				show_hidden_stock_chests = true;
			}
			sendUpdate();
		}
        if (button == openCloseShop) {
        	if (shop_open) {
        		shop_open = false;
        	} else {
        		shop_open = true;
        	}
        	sendUpdate();
		}
        if (button == clearLog) {
        	FurenikusEconomy.network.sendToServer(new StoreManagerClearLogPacket());
		}
    	
    	if (button == withdrawCashFromShop) {
    		FurenikusEconomy.network.sendToServer(new StoreManagerWithdrawPacket());
		}
    	if (button == toggleShopWideBudget) {
    		if (shop_budget) {
    			shop_budget = false;
    		} else {
    			shop_budget = true;
    		}
    		sendUpdate();
		}
    	if (button == addToBudget) {

		}
    	if (button == removeFromBudget) {

		}
	}
	
	public void sendUpdate() {
		FurenikusEconomy.network.sendToServer(new StoreManagerUpdatePacket(store_name.getText(), show_hidden_shops, show_hidden_stock_chests, shop_open, shop_budget));
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		//Set to false at the start of every render tick, and set to true if anything should be rendering right now.
		renderTooltip = false;
		
		listShops.drawScreen(mouseX, mouseY, partialTicks);
		listStock.drawScreen(mouseX, mouseY, partialTicks);
		listLog.drawScreen(mouseX, mouseY, partialTicks);
		
		if (renderTooltip) {
			drawHoveringText(tooltipList, mouseX, mouseY);
		}
	}
}
