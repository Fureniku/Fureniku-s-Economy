package com.silvaniastudios.econ.api.store.entity;

import com.silvaniastudios.econ.api.EconUtils;
import com.silvaniastudios.econ.core.FurenikusEconomy;
import com.silvaniastudios.econ.network.AdminShopPricePacket;
import com.silvaniastudios.econ.network.ServerBalancePacket;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdminShopBlock extends BlockContainer implements IStoreBlock {
	
	public EconUtils econ = new EconUtils();

	float minX = 0.0F;
	float minY = 0.0F;
	float minZ = 0.0F;
	float maxX = 1.0F;
	float maxY = 1.0F;
	float maxZ = 1.0F;
	
	public AdminShopBlock() {
		super(Material.IRON);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setHardness(2.0F);
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntityAdminShop tileEntity = (TileEntityAdminShop) world.getTileEntity(pos);
        if (tileEntity != null) {
        	
        	if (!world.isRemote) {
        		FurenikusEconomy.network.sendTo(new AdminShopPricePacket(tileEntity.ownerName, tileEntity.buyPrice1, tileEntity.sellPrice1, tileEntity.buyPrice2, tileEntity.sellPrice2,
            			tileEntity.buyPrice3, tileEntity.sellPrice3, tileEntity.buyPrice4, tileEntity.sellPrice4), (EntityPlayerMP) player);
        		FurenikusEconomy.network.sendTo(new ServerBalancePacket(""+econ.getBalance(player)), (EntityPlayerMP) player);
        	}
        }
        player.openGui(FurenikusEconomy.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
    //TODO rotation on place

	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return new TileEntityAdminShop();
	}
	
	//TODO drop inventory when broken

    /*private void dropItems(World world, int x, int y, int z){
    	Random rand = new Random();

    	TileEntity tileEntity = world.getTileEntity(x, y, z);
    	if (!(tileEntity instanceof IInventory)) {
    		return;
    	}
    	IInventory inventory = (IInventory) tileEntity;

    	for (int i = 0; i < inventory.getSizeInventory(); i++) {
    		ItemStack item = inventory.getStackInSlot(i);

    		if (item != null && item.stackSize > 0) {
    			float rx = rand.nextFloat() * 0.8F + 0.1F;
    			float ry = rand.nextFloat() * 0.8F + 0.1F;
    			float rz = rand.nextFloat() * 0.8F + 0.1F;
    			
    			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));
    			
    			if (item.hasTagCompound()) {
    				entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
    			}

    			float factor = 0.05F;
    			entityItem.motionX = rand.nextGaussian() * factor;
    			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
    			entityItem.motionZ = rand.nextGaussian() * factor;
    			world.spawnEntityInWorld(entityItem);
    			item.stackSize = 0;
    		}
    	}
    }*/

}