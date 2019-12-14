package com.silvaniastudios.econ.core.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.management.StoreManagerEntity;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.blocks.shop.ShelvesFullEntity;
import com.silvaniastudios.econ.core.blocks.shop.containers.ShelvesFullContainer;
import com.silvaniastudios.econ.network.OpenGuiServerSide;
import com.silvaniastudios.econ.network.ShopUpdatePacket;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiShelvesFull extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/floating_shelves_full.png");
	
	ShelvesFullEntity te;
	
	private GuiTextField price_1;
	private GuiTextField price_2;
	private GuiTextField price_3;
	private GuiTextField price_4;
	private GuiTextField shelf_name;
	
	private GuiButton open_store_manager;
	private GuiButton modify_render_slot_1;
	private GuiButton modify_render_slot_2;
	private GuiButton modify_render_slot_3;
	private GuiButton modify_render_slot_4;
	
	private GuiButton scale_increase;
	private GuiButton scale_decrease;
	private GuiButton x_pos_increase;
	private GuiButton x_pos_decrease;
	private GuiButton y_pos_increase;
	private GuiButton y_pos_decrease;
	private GuiButton z_pos_increase;
	private GuiButton z_pos_decrease;
	private GuiButton x_rot_increase;
	private GuiButton x_rot_decrease;
	private GuiButton y_rot_increase;
	private GuiButton y_rot_decrease;
	private GuiButton z_rot_increase;
	private GuiButton z_rot_decrease;
	
	 public GuiShelvesFull(ShelvesFullEntity entity, ShelvesFullContainer inventorySlotsIn) {
		super(inventorySlotsIn);
		this.te = entity;
	}
	 
	public EconUtils econ = new EconUtils();

	protected int xSize = 176;
	protected int ySize = 196;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
        this.mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        drawTexturedModalRect(left+xSize, top, 176, 0, 37, ySize);
        drawTexturedModalRect(left+xSize+37, top, 176, 0, 80, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		StoreManagerEntity sm = te.getManager();
		fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_owner") + ": " + te.ownerName, 7, -8, 0x404040);
		
		if (sm != null) {
			fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_short") + ": " + te.managerPos.getX() + ", " + te.managerPos.getY() + ", " + te.managerPos.getZ(), 7, 75, 0x404040);
			fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_name") + ": " + sm.shopName, 7, 86, 0x404040);
			
			drawCenteredString(fontRenderer, I18n.format("econ.gui.shop_shelves_full.modify_item_position"), 230, 19, 0x404040);
			
			
			drawCenteredString(fontRenderer, I18n.format("econ.gui.shop_shelves_full.modify_item_position.scale"), 231, 57, 0x404040);
			drawCenteredString(fontRenderer, I18n.format("econ.gui.shop_shelves_full.modify_item_position.position"), 203, 95, 0x404040);
			drawCenteredString(fontRenderer, I18n.format("econ.gui.shop_shelves_full.modify_item_position.rotation"), 260, 95, 0x404040);
			
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			drawCenteredString(fontRenderer,"X", 203*2, 107*2, 0x404040);
			drawCenteredString(fontRenderer,"Y", 203*2, 131*2, 0x404040);
			drawCenteredString(fontRenderer,"Z", 203*2, 155*2, 0x404040);
			drawCenteredString(fontRenderer,"Pitch", 260*2, 107*2, 0x404040);
			drawCenteredString(fontRenderer,"Yaw",   260*2, 131*2, 0x404040);
			drawCenteredString(fontRenderer,"Roll",  260*2, 155*2, 0x404040);
			GL11.glScalef(2.0F, 2.0F, 2.0F);
			
			drawCenteredString(fontRenderer,"1.0", 231, 74, 0x404040);
			
			drawCenteredString(fontRenderer,"0.0", 203, 114, 0x404040);
			drawCenteredString(fontRenderer,"0.0", 203, 138, 0x404040);
			drawCenteredString(fontRenderer,"0.0", 203, 162, 0x404040);
			
			drawCenteredString(fontRenderer,"0", 260, 114, 0x404040);
			drawCenteredString(fontRenderer,"0", 260, 138, 0x404040);
			drawCenteredString(fontRenderer,"0", 260, 162, 0x404040);
		} else {
			fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_not_set"), 7, 75, 0x404040);
		}
		
		price_1.drawTextBox();
		price_2.drawTextBox();
		price_3.drawTextBox();
		price_4.drawTextBox();
		
		shelf_name.drawTextBox();
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button == open_store_manager) {
			StoreManagerEntity sm = te.getManager();
			System.out.println("Store manager button");
			if (sm != null) {
				System.out.println("Go?");
				FurenikusEconomy.network.sendToServer(new OpenGuiServerSide(sm.getPos().getX(), sm.getPos().getY(), sm.getPos().getZ()));
			}
		}
	}
	
	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawString(text, (x - fontRendererIn.getStringWidth(text) / 2), y, color);
    }
	
	@Override
	public void keyTyped(char c, int i) throws IOException {
		if (i == Keyboard.KEY_ESCAPE) {
			int[] prices = {parseInt(price_1.getText()), parseInt(price_2.getText()), parseInt(price_3.getText()), parseInt(price_4.getText())};
			FurenikusEconomy.network.sendToServer(new ShopUpdatePacket(prices, shelf_name.getText()));
			super.keyTyped(c, i);
		}
		if (i == Keyboard.KEY_TAB) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if (shelf_name.isFocused()) {
					shelf_name.setFocused(false);
					price_4.setFocused(true);
				} else if (price_4.isFocused()) {
					price_4.setFocused(false);
					price_3.setFocused(true);
				} else if (price_3.isFocused()) {
					price_3.setFocused(false);
					price_2.setFocused(true);
				} else if (price_2.isFocused()) {
					price_2.setFocused(false);
					price_1.setFocused(true);
				} else if (price_1.isFocused()) {
					price_1.setFocused(false);
					shelf_name.setFocused(true);
				}
			} else {
				if (price_1.isFocused()) {
					price_1.setFocused(false);
					price_2.setFocused(true);
				} else if (price_2.isFocused()) {
					price_2.setFocused(false);
					price_3.setFocused(true);
				} else if (price_3.isFocused()) {
					price_3.setFocused(false);
					price_4.setFocused(true);
				} else if (price_4.isFocused()) {
					price_4.setFocused(false);
					shelf_name.setFocused(true);
				} else if (shelf_name.isFocused()) {
					shelf_name.setFocused(false);
					price_1.setFocused(true);
				}
			}
		} else {
			if (shelf_name.isFocused()) {
				shelf_name.textboxKeyTyped(c, i);
			} else {
				if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' ||
					c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ||
					i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT) {
					price_1.textboxKeyTyped(c, i);
					price_2.textboxKeyTyped(c, i);
					price_3.textboxKeyTyped(c, i);
					price_4.textboxKeyTyped(c, i);
				}
			}
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
		
        this.price_1 = new GuiTextField(0, this.fontRenderer, 29,  3, 54, 18);
        this.price_2 = new GuiTextField(1, this.fontRenderer, 29, 29, 54, 18);
        this.price_3 = new GuiTextField(2, this.fontRenderer, 93,  3, 54, 18);
        this.price_4 = new GuiTextField(3, this.fontRenderer, 93, 29, 54, 18);
        
        shelf_name = new GuiTextField(4, this.fontRenderer, 7, 52, 162, 18);

        price_1.setText("" + te.priceList[0]);
        price_2.setText("" + te.priceList[1]);
        price_3.setText("" + te.priceList[2]);
        price_4.setText("" + te.priceList[3]);
        
        price_1.setFocused(false);
        price_2.setFocused(false);
        price_3.setFocused(false);
        price_4.setFocused(false);
        
        shelf_name.setText(te.shopName);
        
        shelf_name.setFocused(true);
        
        open_store_manager = new GuiButton(5, left + 176, top + 7, 110, 20, I18n.format("econ.gui.store_manager.open_store_manager"));
    	modify_render_slot_1 = new GuiButton(6, left + 176, top + 45, 20, 20, "1");
    	modify_render_slot_2 = new GuiButton(7, left + 207, top + 45, 20, 20, "2");
    	modify_render_slot_3 = new GuiButton(8, left + 236, top + 45, 20, 20, "3");
    	modify_render_slot_4 = new GuiButton(9, left + 266, top + 45, 20, 20, "4");
    	
    	scale_decrease = new GuiButton(10, left + 199, top +  83, 10, 20, "-");
    	scale_increase = new GuiButton(11, left + 253, top +  83, 10, 20, "+");
    	
    	x_pos_decrease = new GuiButton(12, left + 176, top + 121, 10, 20, "-");
    	x_pos_increase = new GuiButton(13, left + 219, top + 121, 10, 20, "+");
    	y_pos_decrease = new GuiButton(14, left + 176, top + 145, 10, 20, "-");
    	y_pos_increase = new GuiButton(15, left + 219, top + 145, 10, 20, "+");
    	z_pos_decrease = new GuiButton(16, left + 176, top + 169, 10, 20, "-");
    	z_pos_increase = new GuiButton(17, left + 219, top + 169, 10, 20, "+");
    	
    	x_rot_decrease = new GuiButton(18, left + 233, top + 121, 10, 20, "-");
    	x_rot_increase = new GuiButton(19, left + 276, top + 121, 10, 20, "+");
    	y_rot_decrease = new GuiButton(20, left + 233, top + 145, 10, 20, "-");
    	y_rot_increase = new GuiButton(21, left + 276, top + 145, 10, 20, "+");
    	z_rot_decrease = new GuiButton(22, left + 233, top + 169, 10, 20, "-");
    	z_rot_increase = new GuiButton(23, left + 276, top + 169, 10, 20, "+");
    	
    	buttonList.add(open_store_manager);
    	buttonList.add(modify_render_slot_1);
    	buttonList.add(modify_render_slot_2);
    	buttonList.add(modify_render_slot_3);
    	buttonList.add(modify_render_slot_4);
    	
    	buttonList.add(scale_increase);
    	buttonList.add(scale_decrease);
    	buttonList.add(x_pos_increase);
    	buttonList.add(x_pos_decrease);
    	buttonList.add(y_pos_increase);
    	buttonList.add(y_pos_decrease);
    	buttonList.add(z_pos_increase);
    	buttonList.add(z_pos_decrease);
    	buttonList.add(x_rot_increase);
    	buttonList.add(x_rot_decrease);
    	buttonList.add(y_rot_increase);
    	buttonList.add(y_rot_decrease);
    	buttonList.add(z_rot_increase);
    	buttonList.add(z_rot_decrease);
	}

	public int parseInt(String s) {
		try {
			return Integer.parseInt("" + s);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
}
