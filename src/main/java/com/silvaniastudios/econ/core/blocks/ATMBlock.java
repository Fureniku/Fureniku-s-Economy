package com.silvaniastudios.econ.core.blocks;

import com.silvaniastudios.econ.api.EconConstants;
import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.EconConfig;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.items.ItemMoney;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ATMBlock extends EconBlockBase {	
	
	public EconUtils econ = new EconUtils();
	
	SoundEvent atmSound = new SoundEvent(new ResourceLocation(FurenikusEconomy.MODID, "cardInsert"));
	
	public ATMBlock(String name) {
		super(name, Material.IRON);
		this.setHardness(1.0F);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setLightOpacity(0);
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	ItemStack item = player.getHeldItemMainhand();
        if (!world.isRemote) {
        	if (player.isSneaking()) {
        		econ.depositAllCash(player);
        	} else if (player.getHeldItemMainhand() != null) {
    			if (item.getItem() instanceof ItemMoney) {
    				ItemMoney money = (ItemMoney) item.getItem();
    				if (econ.addMoney(player, money.moneyValue)) {
        				player.sendMessage(new TextComponentString(TextFormatting.GOLD + econ.formatBalance(money.getMoneyValue()) + TextFormatting.GREEN + " Deposited! Your balance is now " + TextFormatting.GOLD + econ.formatBalance(econ.getBalance(player))));
        				if (EconConfig.fastDepositNotification) { player.sendMessage(new TextComponentString(TextFormatting.ITALIC + "Next time, try shift-right clicking with an empty hand to deposit " + TextFormatting.ITALIC + "all your money!")); }

        				item.setCount(item.getCount()-1);
    				}
        		}
        	}
       	}
        
        if (!(item.getItem() instanceof ItemMoney)) {
        	player.openGui(FurenikusEconomy.instance, EconConstants.Gui.ATM, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
    
    @Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		return new ATMEntity();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
	    int blockSet = this.getMetaFromState(state);
	    int direction = MathHelper.floor((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;
	    int newMeta = (blockSet * 4) + direction;
	    world.setBlockState(pos, this.getStateFromMeta(newMeta), 1);
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 4));
        list.add(new ItemStack(this, 1, 8));
        list.add(new ItemStack(this, 1, 12));
	}
	
    @Override
    public int damageDropped(IBlockState state) {
    	int meta = this.getMetaFromState(state);
    	if (meta < 4) { 
    		return 0;
    	} else if (meta < 8) { 
    		return 4;
    	} else if (meta < 12) {
    		return 8;
    	} else {
    		return 12;
    	}
    }
}