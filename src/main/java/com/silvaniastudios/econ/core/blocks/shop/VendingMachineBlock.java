package com.silvaniastudios.econ.core.blocks.shop;

import java.util.List;

import javax.annotation.Nullable;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.store.shops.ShopBaseBlock;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VendingMachineBlock extends ShopBaseBlock {
	
	public static final PropertyDirection ROTATION = BlockHorizontal.FACING;

	public VendingMachineBlock(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new VendingMachineEntity();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		openGui(world, pos, player, EconConstants.Gui.VENDING_MACHINE_OWNER);
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		 return new BlockStateContainer(this, new IProperty[] {ROTATION});
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ROTATION, EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumFacing) state.getValue(ROTATION)).getIndex();
		return meta;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("econ.tooltip.shops.floating_shelves.1"));
		tooltip.add(TextFormatting.ITALIC +""+ TextFormatting.YELLOW + (I18n.format("econ.tooltip.shops.floating_shelves.2")));
	}
}