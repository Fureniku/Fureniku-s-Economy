package com.silvaniastudios.econ.api.store.container.old;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.api.store.entity.TileEntityAdminShop;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.network.AdminShopClientPacket;
import com.silvaniastudios.econ.network.AdminShopPricePacket;
import com.silvaniastudios.econ.network.SalePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAdminShop extends GuiContainer {
	
	public EconUtils econ = new EconUtils();
	
	int sellMode = 0;
	int x;
	int y;
	int z;
	
	private ItemStack slot0;
	private ItemStack slot1;
	private ItemStack slot2;
	private ItemStack slot3;
	
	private TileEntityAdminShop shelvesEntity;
	
	String buyPrice1 = "" + AdminShopPricePacket.buyPrice1;		
	String sellPrice1 = "" + AdminShopPricePacket.sellPrice1;		
	String buyPrice2 = "" + AdminShopPricePacket.buyPrice2;		
	String sellPrice2 = "" + AdminShopPricePacket.sellPrice2;		
	String buyPrice3 = "" + AdminShopPricePacket.buyPrice3;		
	String sellPrice3 = "" + AdminShopPricePacket.sellPrice3;		
	String buyPrice4 = "" + AdminShopPricePacket.buyPrice4;		
	String sellPrice4 = "" + AdminShopPricePacket.sellPrice4;
	
	String displayBuy1 = "";
	String displayBuy2 = "";
	String displayBuy3 = "";
	String displayBuy4 = "";
	String displaySell1 = "";
	String displaySell2 = "";
	String displaySell3 = "";
	String displaySell4 = "";

	public GuiAdminShop(InventoryPlayer invPlayer, TileEntityAdminShop shelvesEntity) {
		super(new ContainerAdminShop(invPlayer, shelvesEntity));
		this.shelvesEntity = shelvesEntity;
		
		x = shelvesEntity.getPos().getX();
		y = shelvesEntity.getPos().getY();
		z = shelvesEntity.getPos().getZ();
		
		displayBuy1 = "" + shelvesEntity.buyPrice1;
		displayBuy2 = "" + shelvesEntity.buyPrice2;
		displayBuy3 = "" + shelvesEntity.buyPrice3;
		displayBuy4 = "" + shelvesEntity.buyPrice4;
		displaySell1 = "" + shelvesEntity.sellPrice1;
		displaySell2 = "" + shelvesEntity.sellPrice2;
		displaySell3 = "" + shelvesEntity.sellPrice3;
		displaySell4 = "" + shelvesEntity.sellPrice4;
		
		this.slot0 = this.shelvesEntity.getStackInSlot(0);
		this.slot1 = this.shelvesEntity.getStackInSlot(1);
		this.slot2 = this.shelvesEntity.getStackInSlot(2);
		this.slot3 = this.shelvesEntity.getStackInSlot(3);
		
		xSize = 256;
		ySize = 223;
	}

	private static final ResourceLocation texture = new ResourceLocation("flenixcities", "textures/gui/floatingshelves.png");
	//private static final ResourceLocation textureOwnerBuy = new ResourceLocation("flenixcities", "textures/gui/floatingshelvesownerbuy.png");
	//private static final ResourceLocation textureOwnerSell = new ResourceLocation("flenixcities", "textures/gui/floatingshelvesownersell.png");
	public GuiTextField buy1Text;
	public GuiTextField buy2Text;
	public GuiTextField buy3Text;
	public GuiTextField buy4Text;
	public GuiTextField sell1Text;
	public GuiTextField sell2Text;
	public GuiTextField sell3Text;
	public GuiTextField sell4Text;
	public GuiButton buyButton1;
	public GuiButton sellButton1;
	public GuiButton buyButton2;
	public GuiButton sellButton2;
	public GuiButton buyButton3;
	public GuiButton sellButton3;
	public GuiButton buyButton4;
	public GuiButton sellButton4;
	
	int slot1Qty = 1;
	int slot2Qty = 1;
	int slot3Qty = 1;
	int slot4Qty = 1;
	
	public boolean isShopOwner() {
		if (mc.player.capabilities.isCreativeMode) {
			return true;
		}
		return false;
	}
	
	public void unfocusAllTextInputs() {
		updateTileEntity();
		buy1Text.setFocused(false);
		sell1Text.setFocused(false);
		buy2Text.setFocused(false);
		sell2Text.setFocused(false);
		buy3Text.setFocused(false);
		sell3Text.setFocused(false);
		buy4Text.setFocused(false);
		sell4Text.setFocused(false);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.slot0 = this.shelvesEntity.getStackInSlot(0);
		this.slot1 = this.shelvesEntity.getStackInSlot(1);
		this.slot2 = this.shelvesEntity.getStackInSlot(2);
		this.slot3 = this.shelvesEntity.getStackInSlot(3);
		
		buttonList.add(new InvisibleButton(1, guiLeft + 34, guiTop + 51, 38, 14, "")); //Buy 1
		buttonList.add(new InvisibleButton(2, guiLeft + 34, guiTop + 73, 38, 14, "")); //Buy 2
		buttonList.add(new InvisibleButton(3, guiLeft + 34, guiTop + 95, 38, 14, "")); //Buy 3
		buttonList.add(new InvisibleButton(4, guiLeft + 34, guiTop + 117, 38, 14, "")); //Buy 4
		buttonList.add(new InvisibleButton(5, guiLeft + 90, guiTop + 51, 38, 14, "")); //Sell 1
		buttonList.add(new InvisibleButton(6, guiLeft + 90, guiTop + 73, 38, 14, "")); //Sell 2
		buttonList.add(new InvisibleButton(7, guiLeft + 90, guiTop + 95, 38, 14, "")); //Sell 3
		buttonList.add(new InvisibleButton(8, guiLeft + 90, guiTop + 117, 38, 14, "")); //Sell 4
		
		buttonList.add(new InvisibleButton(9, guiLeft - 23, guiTop + 14, 26, 28, "")); //Buy View Tab
		buttonList.add(new InvisibleButton(10, guiLeft - 23, guiTop + 43, 26, 28, "")); //Sell Overview Tab
		
		buyButton1 = new GuiButton(16, guiLeft + 187, guiTop + 48, 30, 20, "Buy");
		sellButton1 = new GuiButton(17, guiLeft + 221, guiTop + 48, 30, 20, "Sell");
		buyButton2 = new GuiButton(18, guiLeft + 187, guiTop + 70, 30, 20, "Buy");
		sellButton2 = new GuiButton(19, guiLeft + 221, guiTop + 70, 30, 20, "Sell");
		buyButton3 = new GuiButton(20, guiLeft + 187, guiTop + 92, 30, 20, "Buy");
		sellButton3 = new GuiButton(21, guiLeft + 221, guiTop + 92, 30, 20, "Sell");
		buyButton4 = new GuiButton(22, guiLeft + 187, guiTop + 114, 30, 20, "Buy");
		sellButton4 = new GuiButton(23, guiLeft + 221, guiTop + 114, 30, 20, "Sell");
		buttonList.add(buyButton1);
		buttonList.add(sellButton1);
		buttonList.add(buyButton2);
		buttonList.add(sellButton2);
		buttonList.add(buyButton3);
		buttonList.add(sellButton3);
		buttonList.add(buyButton4);
		buttonList.add(sellButton4);
		
		buy1Text = new GuiTextField(0, this.fontRenderer, 34, 51, 38, 14);
		buy2Text = new GuiTextField(1, this.fontRenderer, 34, 73, 38, 14);
		buy3Text = new GuiTextField(2, this.fontRenderer, 34, 95, 38, 14);
		buy4Text = new GuiTextField(3, this.fontRenderer, 34, 117, 38, 14);
		sell1Text = new GuiTextField(4, this.fontRenderer, 90, 51, 38, 14);
		sell2Text = new GuiTextField(5, this.fontRenderer, 90, 73, 38, 14);
		sell3Text = new GuiTextField(6, this.fontRenderer, 90, 95, 38, 14);
		sell4Text = new GuiTextField(7, this.fontRenderer, 90, 117, 38, 14);
		
		buy1Text.setFocused(true);
		buy1Text.setText("" + buyPrice1);
		sell1Text.setText("" + sellPrice1);
		buy2Text.setText("" + buyPrice2);
		sell2Text.setText("" + sellPrice2);
		buy3Text.setText("" + buyPrice3);
		sell3Text.setText("" + sellPrice3);
		buy4Text.setText("" + buyPrice4);
		sell4Text.setText("" + sellPrice4);
	}
	
	public void actionPerformed(GuiButton button) {
		if (sellMode == 1) {
			switch(button.id) {
			case 1:
				unfocusAllTextInputs();
				buy1Text.setFocused(true);
				break;
			case 2:
				unfocusAllTextInputs();
				buy2Text.setFocused(true);
				break;
			case 3:
				unfocusAllTextInputs();
				buy3Text.setFocused(true);
				break;
			case 4:
				unfocusAllTextInputs();
				buy4Text.setFocused(true);
				break;
			case 5:
				unfocusAllTextInputs();
				sell1Text.setFocused(true);
				break;
			case 6:
				unfocusAllTextInputs();
				sell2Text.setFocused(true);
				break;
			case 7:
				unfocusAllTextInputs();
				sell3Text.setFocused(true);
				break;
			case 8:
				unfocusAllTextInputs();
				sell4Text.setFocused(true);
				break;
			}
		}
		switch(button.id) {
		case 9:
			if (isShopOwner()) {
				sellMode = 0;
				sendSalePacket("buttonSwitch", sellMode);
			}
			break;
		case 10:
			if (isShopOwner()) {
				sellMode = 1;
				sendSalePacket("buttonSwitch", sellMode);
			}
			break;
		}
		if (sellMode == 0) {
			switch(button.id) {
			case 16:
				sendSalePacket("salePacket", 1);
				break;
			case 17:
				sendSalePacket("buyPacket", 1);
				break;
			case 18:
				sendSalePacket("salePacket", 2);
				break;
			case 19:
				sendSalePacket("buyPacket", 2);
				break;
			case 20:
				sendSalePacket("salePacket", 3);
				break;
			case 21:
				sendSalePacket("buyPacket", 3);
				break;
			case 22:
				sendSalePacket("salePacket", 4);
				break;
			case 23:
				sendSalePacket("buyPacket", 4);
				break;
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		if (sell1Text.getText().equals("0") || sell1Text.getText().equals("0.0") || (econ.parseDouble(sell1Text.getText()) > econ.parseDouble(buy1Text.getText()))) {
			sellButton1.enabled = false;
		}
		if (sell2Text.getText().equals("0") || sell2Text.getText().equals("0.0") || (econ.parseDouble(sell2Text.getText()) > econ.parseDouble(buy2Text.getText()))) {
			sellButton2.enabled = false;
		}
		if (sell3Text.getText().equals("0") || sell3Text.getText().equals("0.0") || (econ.parseDouble(sell3Text.getText()) > econ.parseDouble(buy3Text.getText()))) {
			sellButton3.enabled = false;
		}
		if (sell4Text.getText().equals("0") || sell4Text.getText().equals("0.0") || (econ.parseDouble(sell4Text.getText()) > econ.parseDouble(buy4Text.getText()))) {
			sellButton4.enabled = false;
		}
		if (slot0 == null) {
			buyButton1.enabled = false;
			sellButton1.enabled = false;
		}
		if (slot1 == null) {
			buyButton2.enabled = false;
			sellButton2.enabled = false;
		}
		if (slot2 == null) {
			buyButton3.enabled = false;
			sellButton3.enabled = false;
		}
		if (slot3 == null) {
			buyButton4.enabled = false;
			sellButton4.enabled = false;
		}
		
		String mode = "";
		if (sellMode == 0) {
			mode = "Customer Interface";
		} else if (sellMode == 1) {
			mode = "Store Owner Interface";
		}
		
    	fontRenderer.drawString("Buy", 44, 39, 0x00A012);
    	fontRenderer.drawString("Sell", 101, 39, 0xA80000);
    	
		if (sellMode == 1) {
			buy1Text.drawTextBox();
			buy2Text.drawTextBox();
			buy3Text.drawTextBox();
			buy4Text.drawTextBox();
			sell1Text.drawTextBox();
			sell2Text.drawTextBox();
			sell3Text.drawTextBox();
			sell4Text.drawTextBox();
		} else {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			
			String buy1 = nf.format(econ.parseDouble("" + displayBuy1));
			String buy2 = nf.format(econ.parseDouble("" + displayBuy2));
			String buy3 = nf.format(econ.parseDouble("" + displayBuy3));
			String buy4 = nf.format(econ.parseDouble("" + displayBuy4));
			
			String sell1 = nf.format(econ.parseDouble("" + displaySell1));
			String sell2 = nf.format(econ.parseDouble("" + displaySell2));
			String sell3 = nf.format(econ.parseDouble("" + displaySell3));
			String sell4 = nf.format(econ.parseDouble("" + displaySell4));
			
			
			fontRenderer.drawString("$" + buy1, 51 - fontRenderer.getStringWidth(buy1) / 2, 54, 4210752);
			fontRenderer.drawString("$" + buy2, 51 - fontRenderer.getStringWidth(buy2) / 2, 77, 4210752);
			fontRenderer.drawString("$" + buy3, 51 - fontRenderer.getStringWidth(buy3) / 2, 99, 4210752);
			fontRenderer.drawString("$" + buy4, 51 - fontRenderer.getStringWidth(buy4) / 2, 121, 4210752);
			
			fontRenderer.drawString("$" + sell1, 107 - fontRenderer.getStringWidth(sell1) / 2, 54, 4210752);
			fontRenderer.drawString("$" + sell2, 107 - fontRenderer.getStringWidth(sell2) / 2, 77, 4210752);
			fontRenderer.drawString("$" + sell3, 107 - fontRenderer.getStringWidth(sell3) / 2, 99, 4210752);
			fontRenderer.drawString("$" + sell4, 107 - fontRenderer.getStringWidth(sell4) / 2, 121, 4210752);

		}
	    
		fontRenderer.drawString(mode, 5, 19, 4210752);
		fontRenderer.drawString("Server Shop", 36, 5, 4210752);
    	//fontRenderer.drawString("You have: " + econ.reqClientInventoryBalance(), 100, 5, 4210752);
	}
	
	public int focus() {
		if (buy1Text.isFocused()) {
			return 1;
		} else if (sell1Text.isFocused()) {
			return 2;
		} else if (buy2Text.isFocused()) {
			return 3;
		} else if (sell2Text.isFocused()) {
			return 4;
		} else if (buy3Text.isFocused()) {
			return 5;
		} else if (sell3Text.isFocused()) {
			return 6;
		} else if (buy4Text.isFocused()) {
			return 7;
		} else if (sell4Text.isFocused()) {
			return 8;
		}
		return 0;
	}
	
	@Override
	protected void keyTyped(char c, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			updateTileEntity();
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
		if (sellMode == 0) {
			if (keyCode == Keyboard.KEY_RETURN) {
				unfocusAllTextInputs();
			}
		}
		if (sellMode == 1) {
			if (keyCode == Keyboard.KEY_RETURN) {
				unfocusAllTextInputs();
			}
			//Tab between the 8 boxes
			if (focus() == 1) {
				sell4Text.setFocused(false);
				buy1Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					sell1Text.setFocused(true);
					buy1Text.setFocused(false);
				}
			} else if (focus() == 2) {
				buy1Text.setFocused(false);
				sell1Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					buy2Text.setFocused(true);
					sell1Text.setFocused(false);
				}
			} else if (focus() == 3) {
				sell1Text.setFocused(false);
				buy2Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					sell2Text.setFocused(true);
					buy2Text.setFocused(false);
				}
			} else if (focus() == 4) {
				buy2Text.setFocused(false);
				sell2Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					buy3Text.setFocused(true);
					sell2Text.setFocused(false);
				}
			} else if (focus() == 5) {
				sell2Text.setFocused(false);
				buy3Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					sell3Text.setFocused(true);
					buy3Text.setFocused(false);
				}
			} else if (focus() == 6) {
				buy3Text.setFocused(false);
				sell3Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					buy4Text.setFocused(true);
					sell3Text.setFocused(false);
				}
			} else if (focus() == 7) {
				sell3Text.setFocused(false);
				buy4Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					sell4Text.setFocused(true);
					buy4Text.setFocused(false);
				}
			} else if (focus() == 8) {
				buy4Text.setFocused(false);
				sell4Text.textboxKeyTyped(c, keyCode);
				if (keyCode == Keyboard.KEY_TAB) {
					updateTileEntity();
					buy1Text.setFocused(true);
					sell4Text.setFocused(false);
				}
			}
		}
	}
	
	public void updateTileEntity() {
        if (isShopOwner()) {
        	FurenikusEconomy.network.sendToServer(new AdminShopClientPacket(
        			buy1Text.getText(), 
        			sell1Text.getText(), 
        			buy2Text.getText(), 
        			sell2Text.getText(), 
        			buy3Text.getText(), 
        			sell3Text.getText(), 
        			buy4Text.getText(), 
        			sell4Text.getText(), 
        			x, y, z));
        }
	}
	
	public void sendSalePacket(String pktId, int slotId) {
		System.out.println("sending sale packet: " + pktId + ", slot: " + slotId);
		FurenikusEconomy.network.sendToServer(new SalePacket(pktId, slotId, x, y, z));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (isShopOwner()) {
			drawTexturedModalRect(guiLeft + 80, guiTop + 39, 0, 223, 2, 32);
			drawTexturedModalRect(guiLeft + 80, guiTop + 71, 0, 223, 2, 32);
			drawTexturedModalRect(guiLeft + 80, guiTop + 103, 0, 223, 2, 30);
			drawTexturedModalRect(guiLeft + 136, guiTop + 39, 0, 223, 2, 32);
			drawTexturedModalRect(guiLeft + 136, guiTop + 71, 0, 223, 2, 32);
			drawTexturedModalRect(guiLeft + 136, guiTop + 103, 0, 223, 2, 30);
			//Tabs?
			drawTexturedModalRect(guiLeft - 23, guiTop + 14, 62, 223, 26, 28);
			drawTexturedModalRect(guiLeft - 23, guiTop + 43, 88, 223, 26, 28);
			
			if (sellMode == 0) {
				//Click Tab 1
				drawTexturedModalRect(guiLeft - 26, guiTop + 14, 2, 223, 30, 28);
			} else if (sellMode == 1) {
				drawTexturedModalRect(guiLeft - 26, guiTop + 43, 32, 223, 30, 28);
			}
		}
		//Stock Slot Boxes
		drawTexturedModalRect(guiLeft + 7, guiTop + 49, 114, 223, 18, 18);
		drawTexturedModalRect(guiLeft + 7, guiTop + 71, 114, 223, 18, 18);
		drawTexturedModalRect(guiLeft + 7, guiTop + 93, 114, 223, 18, 18);
		drawTexturedModalRect(guiLeft + 7, guiTop + 115, 114, 223, 18, 18);
	}
	
	public boolean canPlayerAfford(int qty, double price) {
		double salePrice = qty * price;
		double invCash = 0;//ClientPacketHandler.invBalance;
    	
		if (invCash >= salePrice) {
			return true;
		}
		return false;
	}
}