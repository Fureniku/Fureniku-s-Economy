package com.fureniku.econ.client.render;

import com.fureniku.econ.blocks.shop.ShelvesFullEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class ShelvesFullRenderer extends TileEntitySpecialRenderer<ShelvesFullEntity>{

	@Override
	public void render(ShelvesFullEntity te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
		GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();
        renderItem(te, 0);
        renderItem(te, 1);
        renderItem(te, 2);
        renderItem(te, 3);
		
		GlStateManager.popMatrix();
        GlStateManager.popAttrib();
	}
	
	private void renderItem(ShelvesFullEntity te, int slot) {
		ItemStack item = te.inventory.getStackInSlot(slot);
		int meta = te.getBlockType().getMetaFromState(this.getWorld().getBlockState(te.getPos()));
		
		if (!item.isEmpty()) {
			RenderHelper.enableStandardItemLighting();
			GlStateManager.enableLighting();
			GlStateManager.pushMatrix();
			
			//Initial setup to get a basis consistent across shops
			GlStateManager.translate(.5, .5, .5);
			GlStateManager.rotate(meta*90, 0, -1, 0);
			if (slot == 0) { GlStateManager.translate(-0.25,  0.25, -0.1875); }
			if (slot == 1) { GlStateManager.translate( 0.25,  0.25, -0.1875); }
			if (slot == 2) { GlStateManager.translate(-0.25, -0.25, -0.1875); }
			if (slot == 3) { GlStateManager.translate( 0.25, -0.25, -0.1875); }
			GlStateManager.scale(.375f, .375f, .375f);
    
			//And now we let the player mess with it a bit :)
			GlStateManager.scale(te.slot_scale[slot], te.slot_scale[slot], te.slot_scale[slot]);
			GlStateManager.rotate(te.slot_x_rot[slot], 0, 1, 0);
			GlStateManager.rotate(te.slot_y_rot[slot], 1, 0, 0);
			GlStateManager.rotate(te.slot_z_rot[slot], 0, 0, 1);
			
			GlStateManager.translate(te.slot_x_pos[slot], te.slot_y_pos[slot], te.slot_z_pos[slot]);
            
            Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
            
            GlStateManager.popMatrix();
		}
	}
}
