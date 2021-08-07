package com.fureniku.econ.client.gui.shop;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.shop.ShelvesFullEntity;
import com.fureniku.econ.blocks.shop.containers.ShelvesFullContainer;
import com.fureniku.econ.client.gui.GuiCurrencyField;
import com.fureniku.econ.network.OpenGuiServerSide;
import com.fureniku.econ.network.ShopUpdatePacket;
import com.fureniku.econ.store.management.StoreManagerEntity;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiShelvesFull extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/floating_shelves_full.png");
	
	ShelvesFullEntity te;
	
	private GuiCurrencyField price_1;
	private GuiCurrencyField price_2;
	private GuiCurrencyField price_3;
	private GuiCurrencyField price_4;
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
	
	private int editingSlotId = 0;
	
	public GuiShelvesFull(ShelvesFullEntity entity, ShelvesFullContainer inventorySlotsIn) {
		super(inventorySlotsIn);
		this.te = entity;
	}
	 
	public EconUtils econ = new EconUtils();

	protected int xSize = 176; //TODO rework because this is wrong
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
			fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_name") + ": " + sm.getShopName(), 7, 86, 0x404040);
		} else {
			fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_not_set"), 7, 75, 0x404040);
		}
		
		drawCenteredString(fontRenderer, I18n.format("econ.gui.shop_shelves_full.modify_item_position"), 230, 19, 0x404040);
		
		if (editingSlotId > 0) {
			int s = editingSlotId-1;
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
			
			drawCenteredString(fontRenderer, "" + Math.round(te.slot_scale[s] * 100) / 100.0f, 231, 74, 0x404040);
			
			drawCenteredString(fontRenderer, "" + Math.round(te.slot_x_pos[s] * 100) / 100.0f, 203, 114, 0x404040);
			drawCenteredString(fontRenderer, "" + Math.round(te.slot_y_pos[s] * 100) / 100.0f, 203, 138, 0x404040);
			drawCenteredString(fontRenderer, "" + Math.round(te.slot_z_pos[s] * 100) / 100.0f, 203, 162, 0x404040);
			
			drawCenteredString(fontRenderer, "" + te.slot_x_rot[s], 260, 114, 0x404040);
			drawCenteredString(fontRenderer, "" + te.slot_y_rot[s], 260, 138, 0x404040);
			drawCenteredString(fontRenderer, "" + te.slot_z_rot[s], 260, 162, 0x404040);
		}
		
		price_1.drawTextBox();
		price_2.drawTextBox();
		price_3.drawTextBox();
		price_4.drawTextBox();
		
		shelf_name.drawTextBox();
		
		if (open_store_manager.isMouseOver()) {
			if (te.hasManager()) {
				this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.store_manager_linked"), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
			} else {
				this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.store_manager_unlinked"), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
			}
		}
		
		if (isMouseOverText(mouseX, mouseY, price_1)) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.slot_price", 1) + price_1.getText(), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
		if (isMouseOverText(mouseX, mouseY, price_2)) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.slot_price", 2) + price_2.getText(), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
		if (isMouseOverText(mouseX, mouseY, price_3)) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.slot_price", 3) + price_3.getText(), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
		if (isMouseOverText(mouseX, mouseY, price_4)) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.slot_price", 4) + price_4.getText(), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
		
		if (isMouseOverText(mouseX, mouseY, shelf_name)) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.shelf_name"), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
		
		if (modify_render_slot_1.isMouseOver() || modify_render_slot_2.isMouseOver() || modify_render_slot_3.isMouseOver() || modify_render_slot_4.isMouseOver()) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.render"), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
		
		if (x_rot_increase.isMouseOver() || x_rot_decrease.isMouseOver() || y_rot_increase.isMouseOver() || y_rot_decrease.isMouseOver() || z_rot_increase.isMouseOver() || z_rot_decrease.isMouseOver()) {
			this.drawHoveringText(I18n.format("econ.gui.shop_shelves_full.tooltip.rotation"), mouseX - ((width - xSize) / 2), mouseY - ((height - ySize) / 2));
		}
	}
	
	private void displayExtraButtons(boolean display) {
		scale_increase.visible = display;
		scale_decrease.visible = display;
		x_pos_increase.visible = display;
		x_pos_decrease.visible = display;
		y_pos_increase.visible = display;
		y_pos_decrease.visible = display;
		z_pos_increase.visible = display;
		z_pos_decrease.visible = display;
		x_rot_increase.visible = display;
		x_rot_decrease.visible = display;
		y_rot_increase.visible = display;
		y_rot_decrease.visible = display;
		z_rot_increase.visible = display;
		z_rot_decrease.visible = display;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button == open_store_manager) {
			StoreManagerEntity sm = te.getManager();
			if (sm != null) {
				FurenikusEconomy.network.sendToServer(new OpenGuiServerSide(sm.getPos().getX(), sm.getPos().getY(), sm.getPos().getZ()));
			}
		}
		if (button == modify_render_slot_1) {
			disableModifyButton(button);
			displayExtraButtons(true);
			editingSlotId = 1;
		}
		if (button == modify_render_slot_2) {
			disableModifyButton(button);
			displayExtraButtons(true);
			editingSlotId = 2;
		}
		if (button == modify_render_slot_3) {
			disableModifyButton(button);
			displayExtraButtons(true);
			editingSlotId = 3;
		}
		if (button == modify_render_slot_4) {
			disableModifyButton(button);
			displayExtraButtons(true);
			editingSlotId = 4;
		}
		
		if (editingSlotId > 0) {
			int s = editingSlotId-1;
			if (button == this.scale_increase && te.slot_scale[s] < 2.0f) {
				te.slot_scale[s] += 0.1f; 
			}
			if (button == this.scale_decrease && te.slot_scale[s] > 0.1f) {
				te.slot_scale[s] -= 0.1f; 
			}
			
			if (button == this.x_pos_increase && te.slot_x_pos[s] < 0.5) {
				te.slot_x_pos[s] += 0.05f;
			}
			if (button == this.x_pos_decrease && te.slot_x_pos[s] > -0.5) {
				te.slot_x_pos[s] -= 0.05f;
			}
			
			if (button == this.y_pos_increase && te.slot_y_pos[s] < 0.5) {
				te.slot_y_pos[s] += 0.05f;
			}
			if (button == this.y_pos_decrease && te.slot_y_pos[s] > -0.5) {
				te.slot_y_pos[s] -= 0.05f;
			}
			
			if (button == this.z_pos_increase && te.slot_z_pos[s] < 0.5) {
				te.slot_z_pos[s] += 0.05f;
			}
			if (button == this.z_pos_decrease && te.slot_z_pos[s] > -0.5) {
				te.slot_z_pos[s] -= 0.05f;
			}
			
			int rotScale = (GuiScreen.isCtrlKeyDown() || GuiScreen.isShiftKeyDown()) ? 15 : 1;
			
			if (button == this.x_rot_increase) {
				if (te.slot_x_rot[s] < 359) {
					te.slot_x_rot[s] += rotScale;
				}
			}
			if (button == this.x_rot_decrease) {
				if (te.slot_x_rot[s] > 0) {
					te.slot_x_rot[s] -= rotScale;
				}
			}
			
			if (button == this.y_rot_increase) {
				if (te.slot_y_rot[s] < 359) {
					te.slot_y_rot[s] += rotScale;
				}
			}
			if (button == this.y_rot_decrease) {
				if (te.slot_y_rot[s] > 0) {
					te.slot_y_rot[s] -= rotScale;
				}
			}
			
			if (button == this.z_rot_increase) {
				if (te.slot_z_rot[s] < 359) {
					te.slot_z_rot[s] += rotScale;
				}
			}
			if (button == this.z_rot_decrease) {
				if (te.slot_z_rot[s] > 0) {
					te.slot_z_rot[s] -= rotScale;
				}
			}
			
			
			if (te.slot_x_rot[s] > 359) {
				te.slot_x_rot[s] -= 360;
			}
			if (te.slot_y_rot[s] > 359) {
				te.slot_y_rot[s] -= 360;
			}
			if (te.slot_z_rot[s] > 359) {
				te.slot_z_rot[s] -= 360;
			}
			if (te.slot_x_rot[s] < 0) {
				te.slot_x_rot[s] += 360;
			}
			if (te.slot_y_rot[s] < 0) {
				te.slot_y_rot[s] += 360;
			}
			if (te.slot_z_rot[s] < 0) {
				te.slot_z_rot[s] += 360;
			}
		}
		
	}
	
	//Disable whichever button was clicked, re-enable the rest
	private void disableModifyButton(GuiButton button) {
		modify_render_slot_1.enabled = modify_render_slot_1 != button;
		modify_render_slot_2.enabled = modify_render_slot_2 != button;
		modify_render_slot_3.enabled = modify_render_slot_3 != button;
		modify_render_slot_4.enabled = modify_render_slot_4 != button;
	}
	
	@Override
	public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawString(text, (x - fontRendererIn.getStringWidth(text) / 2), y, color);
    }
	
	@Override
	public void keyTyped(char c, int i) throws IOException {
		if (i == Keyboard.KEY_ESCAPE) {
			int[] prices = {price_1.getValue(), price_2.getValue(), price_3.getValue(), price_4.getValue()};
			FurenikusEconomy.network.sendToServer(new ShopUpdatePacket(prices, shelf_name.getText(), te, editingSlotId));
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
		
        this.price_1 = new GuiCurrencyField(0, this.fontRenderer, 29,  3, 54, 18);
        this.price_2 = new GuiCurrencyField(1, this.fontRenderer, 29, 29, 54, 18);
        this.price_3 = new GuiCurrencyField(2, this.fontRenderer, 93,  3, 54, 18);
        this.price_4 = new GuiCurrencyField(3, this.fontRenderer, 93, 29, 54, 18);
        
        shelf_name = new GuiTextField(4, this.fontRenderer, 7, 52, 162, 18);

        price_1.setText("" + te.priceList[0]);
        price_2.setText("" + te.priceList[1]);
        price_3.setText("" + te.priceList[2]);
        price_4.setText("" + te.priceList[3]);
        
        //"write" text to force the custom formatting when they're initiated
        price_1.writeText("");
        price_2.writeText("");
        price_3.writeText("");
        price_4.writeText("");
        
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
    	
    	modify_render_slot_1.enabled = te.hasManager();
		modify_render_slot_2.enabled = te.hasManager();
		modify_render_slot_3.enabled = te.hasManager();
		modify_render_slot_4.enabled = te.hasManager();
		open_store_manager.enabled = te.hasManager();
		
		displayExtraButtons(false);
		
		if (te.selectedId == 1) { this.actionPerformed(modify_render_slot_1); }
		if (te.selectedId == 2) { this.actionPerformed(modify_render_slot_2); }
		if (te.selectedId == 3) { this.actionPerformed(modify_render_slot_3); }
		if (te.selectedId == 4) { this.actionPerformed(modify_render_slot_4); }
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isMouseOverText(mouseX, mouseY, price_1)) {
			selectTexts(price_1);
		}
		if (isMouseOverText(mouseX, mouseY, price_2)) {
			selectTexts(price_2);
		}
		if (isMouseOverText(mouseX, mouseY, price_3)) {
			selectTexts(price_3);
		}
		if (isMouseOverText(mouseX, mouseY, price_4)) {
			selectTexts(price_4);
		}
		if (isMouseOverText(mouseX, mouseY, shelf_name)) {
			selectTexts(shelf_name);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private void selectTexts(GuiTextField text) {
		price_1.setFocused(text == price_1);
		price_2.setFocused(text == price_2);
		price_3.setFocused(text == price_3);
		price_4.setFocused(text == price_4);
		shelf_name.setFocused(text == shelf_name);
	}
	
	private boolean isMouseOverText(int mouseX, int mouseY, GuiTextField text) {
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
		int x = left + text.x;
		int y = top + text.y + 15;
		if (mouseX >= x && mouseX <= x + text.width && mouseY >= y && mouseY <= y + text.height) {
			return true;
		}
		return false;
	}

	public int parseInt(String s) {
		try {
			return Integer.parseInt("" + s);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
}
