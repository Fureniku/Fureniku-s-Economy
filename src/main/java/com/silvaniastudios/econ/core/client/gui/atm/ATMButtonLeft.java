package com.silvaniastudios.econ.core.client.gui.atm;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ATMButtonLeft extends GuiButton {

    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean drawButton;
    protected boolean field_82253_i;
    protected String textureString;
    
    protected static final ResourceLocation field_110332_a = new ResourceLocation("flenixcities", "textures/gui/atmbuttonleft.png");

    public ATMButtonLeft(int par1, int par2, int par3, int par4, int par5, String par6Str) {
    	super(par1, par2, par3, par4, par5, par6Str);
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.drawButton = true;
        this.id = par1;
        this.xPosition = par2;
        this.yPosition = par3;
        this.width = par4;
        this.height = par5;
        this.displayString = par6Str;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.drawButton) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(field_110332_a);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_82253_i);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width, this.yPosition, 200 - this.width, 46 + k * 20, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int l = 14737632;

            if (!this.enabled) {
                l = -6250336;
            }
            else if (this.field_82253_i) {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
}