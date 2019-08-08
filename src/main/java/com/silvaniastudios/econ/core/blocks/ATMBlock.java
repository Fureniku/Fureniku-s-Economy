package com.silvaniastudios.econ.core.blocks;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.EconItems;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.core.items.ItemMoney;
import com.silvaniastudios.econ.network.ServerBalancePacket;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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
        if (!world.isRemote) {
        	if (player.isSneaking()) {
        		econ.depositAllCash(player);
        	} else if (player.getHeldItemMainhand() != null) {
        		if (player.getHeldItemMainhand().getItem() == EconItems.debitCard) {
        			world.playSound(player, pos, atmSound, SoundCategory.BLOCKS, 1.0F, 1.0F);
        			FurenikusEconomy.proxy.openGui(0);
        			econ.getBalance(player);                 
        		} else {
        			ItemStack item = player.getHeldItemMainhand();

        			if (item.getItem() instanceof ItemMoney) {
        				ItemMoney money = (ItemMoney) item.getItem();
        				if (econ.addMoney(player, money.moneyValue)) {
	        				player.sendMessage(new TextComponentString(TextFormatting.GOLD + econ.formatBalance(money.getMoneyValue()) + TextFormatting.GREEN + " Deposited! Your balance is now " + TextFormatting.GOLD + econ.formatBalance(econ.getBalance(player))));
	        				player.sendMessage(new TextComponentString(TextFormatting.ITALIC + "Next time, try shift-right clicking with an empty hand to deposit " + TextFormatting.ITALIC + "all your money!"));
	
	        				item.setCount(item.getCount()-1);
        				}
        			} else {
        				FurenikusEconomy.proxy.openGui(1);
        			}
        		}
        	} else {
        		FurenikusEconomy.proxy.openGui(1);
        	}
        
        	EntityPlayerMP playerMP = (EntityPlayerMP) player;
        	FurenikusEconomy.network.sendTo(new ServerBalancePacket(""+econ.getBalance(player)), playerMP); //TODO
       		System.out.println("Current Balance Packet Sent! Balance: $" + econ.getBalance(player));
       	}
        return true;
    }
		
	public boolean renderAsNormalBlock() {
		return false;
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
    	return this.getMetaFromState(state);
    }
}