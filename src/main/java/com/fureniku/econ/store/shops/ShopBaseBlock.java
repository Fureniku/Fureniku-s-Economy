package com.fureniku.econ.store.shops;

import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.items.StoreStockPairer;
import com.fureniku.econ.store.management.StoreManagerEntity;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShopBaseBlock extends Block {
	
	protected String name;
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	//How many slots wide the shop is visually
	private int shopSlotWidth;
	//How many slots tall the shop is visually
	private int shopSlotHeight;
	//These two things multiplied together should be equal to the Tile Entity's inventory size.
	
	public ShopBaseBlock(String name, int shopWidth, int shopHeight) {
		super(Material.IRON);
		this.name = name;
		setUnlocalizedName(FurenikusEconomy.MODID + "." + name);
		setRegistryName(name);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setHardness(1.5F);
		this.setHarvestLevel("pickaxe", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.shopSlotWidth = shopWidth;
		this.shopSlotHeight = shopHeight;
	}
	
	public int getShopSlotWidth() {
		return shopSlotWidth;
	}
	
	public int getShopSlotHeight() {
		return shopSlotHeight;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		if (placer.getHorizontalFacing() == EnumFacing.NORTH) { return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH); }
		if (placer.getHorizontalFacing() == EnumFacing.EAST)  { return this.getDefaultState().withProperty(FACING, EnumFacing.EAST); }
		if (placer.getHorizontalFacing() == EnumFacing.SOUTH) { return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH); }
		if (placer.getHorizontalFacing() == EnumFacing.WEST)  { return this.getDefaultState().withProperty(FACING, EnumFacing.WEST); }
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);
	}
	
	public int getMetaFromState(IBlockState state) {
		if (state.getValue(FACING).equals(EnumFacing.NORTH)) {
			return 0;
		} else if (state.getValue(FACING).equals(EnumFacing.EAST)) {
			return 1;
		} else if (state.getValue(FACING).equals(EnumFacing.SOUTH)) {
			return 2;
		} else if (state.getValue(FACING).equals(EnumFacing.WEST)) {
			return 3;
		}
		
		return 0;
	}
	
	public IBlockState getStateFromMeta(int meta) {
		if (meta == 0) { return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH); }
		if (meta == 1) { return this.getDefaultState().withProperty(FACING, EnumFacing.EAST);  }
		if (meta == 2) { return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH); }
		return this.getDefaultState().withProperty(FACING, EnumFacing.WEST); 	
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	public void openGui(World world, BlockPos pos, EntityPlayer player, int guiId) {
		TileEntity te = world.getTileEntity(pos);
		if (!world.isRemote) {
			if (te != null && te instanceof ShopBaseEntity) {
				player.openGui(FurenikusEconomy.instance, guiId, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}
	
	public boolean registerToShopManager(EntityPlayer player, EnumHand hand, World world, BlockPos pos) {
		System.out.println("Begin registering to shop manager");
		ItemStack stack = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		if (stack.getItem() instanceof StoreStockPairer && te != null && te instanceof ShopBaseEntity) {
			ShopBaseEntity shop = (ShopBaseEntity) te;
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null) {
				System.out.println("Valid stock pairer");
				BlockPos managerPos = new BlockPos(nbt.getInteger("manager_x"), nbt.getInteger("manager_y"), nbt.getInteger("manager_z"));
				TileEntity t = world.getTileEntity(managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					System.out.println("Found manager");
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					if (sm.getOwnerUuid().equalsIgnoreCase(player.getCachedUniqueIdString())) {
						System.out.println("Correct player. everything should work?");
						if (sm.addShopToManager(pos)) {
							shop.setManager(managerPos);
							return true;
						}
						return false;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!unregisterFromManager(worldIn, pos)) {
			System.out.println("[Fureniku's Economy] A destroyed shop interface didn't correctly unregister from its manager at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
		}
		
		super.breakBlock(worldIn, pos, state);
    }
	
	public boolean unregisterFromManager(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof ShopBaseEntity) {
			System.out.println("TE found");
			ShopBaseEntity sbe = (ShopBaseEntity) te;
			if (sbe.managerPos != null && sbe.managerPos.getY() > 0) {
				TileEntity t = world.getTileEntity(sbe.managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					System.out.println("Found manager");
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					return sm.removeShopFromManager(pos);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	public void registerItemModel(Item itemBlock) {
		FurenikusEconomy.proxy.registerItemRenderer(itemBlock, 0, name);
	}
	
	public Item createItemBlock() {
		return new ItemBlock(this).setRegistryName(getRegistryName());
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		StateMapperBase b = new DefaultStateMapper();
		BlockStateContainer bsc = this.getBlockState();
		ImmutableList<IBlockState> values = bsc.getValidStates();
		
		for(IBlockState state : values) {
			ModelResourceLocation mrl = new ModelResourceLocation(state.getBlock().getRegistryName(), b.getPropertyString(state.getProperties()));
			
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(state.getBlock()), this.getMetaFromState(state), mrl);
		}
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
