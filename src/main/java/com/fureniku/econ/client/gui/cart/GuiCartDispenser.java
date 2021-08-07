package com.fureniku.econ.client.gui.cart;

import java.util.List;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.network.CloseCartPacket;
import com.fureniku.econ.network.OpenGuiServerSide;
import com.fureniku.econ.store.Customer;
import com.fureniku.econ.store.management.CartDispenserContainer;
import com.fureniku.econ.store.management.CartDispenserEntity;
import com.fureniku.econ.store.management.StoreManagerEntity;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCartDispenser extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(FurenikusEconomy.MODID, "textures/gui/cart_dispenser.png");
	
	private CartDispenserEntity te;
	private int cartId;
	private boolean isOwner;
	
	StoreManagerEntity sm;
	Customer customer;
	
	GuiButton cart1;
	GuiButton cart2;
	GuiButton cart3;
	GuiButton cart4;
	
	GuiButton closeCart;
	
	public GuiCartDispenser(CartDispenserEntity entity, CartDispenserContainer inventorySlotsIn, int cartId, boolean isOwner) {
		super(inventorySlotsIn);
		this.te = entity;
		this.cartId = cartId;
		this.xSize = 176;
		this.ySize = 157;
		//Just enables the buttons, which are verified server-side anyway.
		this.isOwner = isOwner;
		System.out.println("Opening cart dispenser with ID " + cartId);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;

		this.mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

		if (isOwner) {
			drawTexturedModalRect(left +  17, top + 6, te.slotUsed[0] ? 44 : 0, 234, 22, 22);
			drawTexturedModalRect(left +  57, top + 6, te.slotUsed[1] ? 44 : 0, 234, 22, 22);
			drawTexturedModalRect(left +  97, top + 6, te.slotUsed[2] ? 44 : 0, 234, 22, 22);
			drawTexturedModalRect(left + 137, top + 6, te.slotUsed[3] ? 44 : 0, 234, 22, 22);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		
		if (!isOwner) {
			fontRenderer.drawString(I18n.format("econ.gui.store_manager.store_manager_owner") + ": " + te.ownerName, 7, 7, 0x404040);
			fontRenderer.drawString("Shop: " + te.getManager().getShopName(), 7, 17, 0x404040);
		} else {	
			fontRenderer.drawString("Shop: " + te.getManager().getShopName(), 7, 119, 0x404040);
		}
		if (customer != null) {
			fontRenderer.drawString(I18n.format("econ.gui.cart.customer_name", customer.getCustomerPlayer(te.getWorld()).getDisplayNameString()), 7, 89, 0x404040);
			fontRenderer.drawString(I18n.format("econ.gui.cart.value", EconUtils.formatBalance(customer.getBalance(te.getWorld()))), 7, 99, 0x404040);
		}
		
		int slot = getHoveredSlot(mouseX-left, mouseY-top) + (27*cartId);
		if (slot >= 0 && slot <= 26) {
			ItemStack stack = inventorySlots.getSlot(slot).getStack();
			if (!stack.isEmpty()) {
				renderToolTip(stack, mouseX-left, mouseY-top, ChatFormatting.GOLD + " x" + stack.getCount() + ChatFormatting.DARK_GREEN + " [" + EconUtils.formatBalance(te.getSlotPrice(cartId, slot)) + "]");
			}
		}
	}
	
	private int getHoveredSlot(int mouseX, int mouseY) {
		//x:7/y:31
		if (mouseX >= 7 && mouseX <= 7 + (9*18) && mouseY >= 31 && mouseY <= 31 + (3*18)) {
			int slotX = (int) Math.floor((mouseX -  7) / 18.0);
			int slotY = (int) Math.floor((mouseY - 31) / 18.0);
			
			return slotX + 9*slotY;
		}
		return -1;
	}
	
	
	public void renderToolTip(ItemStack item, int posX, int posZ, String str) {
		List<String> list = item.getTooltip((EntityPlayer) mc.player, ITooltipFlag.TooltipFlags.NORMAL);
		
		for (int k = 0; k < list.size(); ++k) {
			if (k == 0) {
				list.set(k, item.getRarity().rarityColor + (String)list.get(k));
			} else {
				list.set(k, ChatFormatting.GRAY + (String)list.get(k));
			}
		}
		
		list.set(0, list.get(0) + str);

		drawHoveringText(list, posX, posZ);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        
		sm = te.getManager();
		System.out.println("init gui");
		
		if (sm != null) {
			System.out.println("SM isn't null");
			customer = sm.getCustomerInCart(te, cartId);
			
			if (isOwner) {
				cart1 = new GuiButton(0, left +  18, top + 7, 20, 20, "1");
				cart2 = new GuiButton(1, left +  58, top + 7, 20, 20, "2");
				cart3 = new GuiButton(2, left +  98, top + 7, 20, 20, "3");
				cart4 = new GuiButton(3, left + 138, top + 7, 20, 20, "4");
				
				closeCart = new GuiButton(4, left + 7, top + ySize - 27, xSize - 14, 20, "Close Cart");
		    	
				buttonList.add(cart1);
				buttonList.add(cart2);
				buttonList.add(cart3);
				buttonList.add(cart4);
				buttonList.add(closeCart);
				
				cart1.enabled = cartId != 0;
				cart2.enabled = cartId != 1;
				cart3.enabled = cartId != 2;
				cart4.enabled = cartId != 3;
			}
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (isOwner) {
			int id = -1;
			if (button == cart1) {
				id = 0;
			}
			if (button == cart2) {
				id = 1;
			}
			if (button == cart3) {
				id = 2;
			}
			if (button == cart4) {
				id = 3;
			}
			
			if (id >= 0) {
				FurenikusEconomy.network.sendToServer(new OpenGuiServerSide(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), id));
				//Minecraft.getMinecraft().player.openGui(FurenikusEconomy.instance, EconConstants.Gui.CART_DISPENSER_OWNER_1 + id, Minecraft.getMinecraft().world, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
			}
			
			if (button == closeCart) {
				FurenikusEconomy.network.sendToServer(new CloseCartPacket(this.cartId));
			}
		}
	}
}
