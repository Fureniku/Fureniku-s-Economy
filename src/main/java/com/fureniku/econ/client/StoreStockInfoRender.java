package com.fureniku.econ.client;

import java.util.Iterator;
import java.util.List;

import com.fureniku.econ.EconUtils;
import com.fureniku.econ.store.shops.ShopBaseBlock;
import com.fureniku.econ.store.shops.ShopBaseEntity;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StoreStockInfoRender extends Gui {

	public Minecraft mc = Minecraft.getMinecraft();
	protected FontRenderer fontRendererObj;
	public EconUtils econ = new EconUtils();
	
	public StoreStockInfoRender() {
		super();
	}
	
	@SubscribeEvent
	public void onRenderStock(RenderGameOverlayEvent.Text event) {
		WorldClient world = mc.world;
		RayTraceResult ray = mc.getRenderViewEntity().rayTrace(EntityPlayer.REACH_DISTANCE.getDefaultValue(), 1.0f);
		FontRenderer font = mc.fontRenderer;
		
		if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = ray.getBlockPos();
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			
			if (block instanceof ShopBaseBlock) {
				TileEntity te = world.getTileEntity(pos);
				
				if (te instanceof ShopBaseEntity) {
					ShopBaseEntity shop = (ShopBaseEntity) te;
					int targetSlot = slotHit(ray.hitVec, pos, ray.sideHit, (ShopBaseBlock) block);
					if (targetSlot >= 0 && targetSlot <= shop.shopSize) {
						ItemStack item = shop.inventory.getStackInSlot(targetSlot);
						
						if (ray.sideHit.getOpposite() == state.getProperties().get(ShopBaseBlock.FACING) && !item.isEmpty()) {
							GlStateManager.pushMatrix();
							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
							GlStateManager.disableLighting();
							GlStateManager.scale(1f, 1f, 1f);
							renderToolTip(item, mc.displayWidth/4 + 20, mc.displayHeight/4 + 20, ChatFormatting.GOLD + " x" + item.getCount() + ChatFormatting.DARK_GREEN + " [" + EconUtils.formatBalance(shop.priceList[targetSlot]) + "]", mc.player, font);
							GlStateManager.popMatrix();
						}
					}
				}
			}
		}
	}
	
	public int slotHit(Vec3d vector, BlockPos pos, EnumFacing facing, ShopBaseBlock shop) {
		double xCoord = vector.x - pos.getX();
		double yCoord = vector.y - pos.getY();
		double zCoord = vector.z - pos.getZ();
		
		float horizontalSegment = 1.0f / shop.getShopSlotWidth();
		float verticalSegment = 1.0f / shop.getShopSlotHeight();
		
		double xHit = 0;
		double yHit = Math.abs(yCoord-1);
		
		if (facing == EnumFacing.SOUTH) {
			xHit = xCoord;
		}
		if (facing == EnumFacing.WEST) {
			xHit = zCoord;
		}
		if (facing == EnumFacing.NORTH) {
			xHit = Math.abs(xCoord-1);
		}
		if (facing == EnumFacing.EAST) {
			xHit = Math.abs(zCoord-1);
		}
		
		int slotH = 0;
		int slotV = 0;
		while (xHit > 0) {
			xHit -= horizontalSegment;
			slotH++;
		}
		
		while (yHit > 0) {
			yHit -= verticalSegment;
			slotV++;
		}
		
		return (shop.getShopSlotWidth() * (slotV - 1)) + slotH - 1;
	}
	
	public void renderToolTip(ItemStack item, int posX, int posZ, String str, EntityPlayerSP player, FontRenderer font) {
		List<String> list = item.getTooltip((EntityPlayer)player, ITooltipFlag.TooltipFlags.NORMAL);
		
		for (int k = 0; k < list.size(); ++k) {
			if (k == 0) {
				list.set(k, item.getRarity().rarityColor + (String)list.get(k));
			} else {
				list.set(k, ChatFormatting.GRAY + (String)list.get(k));
			}
		}

		drawHoveringText(list, posX, posZ, font, str);
	}
	
	public void drawHoveringText(List<String> list, int x, int z, FontRenderer fnt, String str) {
		if (!list.isEmpty()) {
			int k = 0;
			list.set(0, list.get(0) + str);
			
			Iterator<String> iterator = list.iterator();

			while (iterator.hasNext()) {
				String s = (String)iterator.next();
				int l = fnt.getStringWidth(s);

				if (l > k) {
					k = l;
				}
			}

			int j2 = x + 12;
			int k2 = z - 12;
			int i1 = 8;

			if (list.size() > 1) {
				i1 += 2 + (list.size() - 1) * 10;
			}

			int width = this.mc.displayWidth;
			int height = this.mc.displayHeight;
			
			if (j2 + k > width) {
				j2 -= 28 + k;
			}

			if (k2 + i1 + 6 > height) {
				k2 = height - i1 - 6;
			}

			int j1 = -267386864;
			this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < list.size(); ++i2) {
				String s1 = (String)list.get(i2);
				fnt.drawStringWithShadow(s1, j2, k2, -1);

				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}
		}
	}
}
