package com.fureniku.econ.store.management;

import com.fureniku.econ.EconConstants;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.EconBlockBase;
import com.fureniku.econ.items.StoreStockPairer;
import com.fureniku.econ.store.Customer;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CartDispenserBlock extends EconBlockBase {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public CartDispenserBlock(String name) {
		super(name, Material.IRON);
		this.setHardness(1.0F);
		this.setCreativeTab(FurenikusEconomy.tabEcon);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
   	public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
    }
   	
    @Override
   	public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
        	TileEntity te = world.getTileEntity(pos);
        	if (te != null && te instanceof CartDispenserEntity) {
        		CartDispenserEntity entity = (CartDispenserEntity) te;
        		
        		boolean isOwner = player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid);
        		
        		if (entity.ownerUuid.isEmpty()) {
        			entity.ownerUuid = player.getCachedUniqueIdString();
        			entity.ownerName = player.getDisplayName().getFormattedText();
        			player.sendMessage(new TextComponentTranslation("econ.shop.claimed", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
        		}
        		
        		if (isOwner) {
        			if (player.getHeldItem(hand).getItem() instanceof StoreStockPairer) {
	    				if (registerToShopManager(player, hand, world, pos)) {
	    					player.sendMessage(new TextComponentTranslation("econ.shop.registered", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
	    				} else {
	    					player.sendMessage(new TextComponentTranslation("econ.shop.register_failed", this.getPickBlock(state, null, world, pos, player).getDisplayName()));
	    				}
	    				return true;
	    			}
	    		}
        		
        		
        		TileEntity mngr = world.getTileEntity(entity.managerPos);
        		if (mngr != null && mngr instanceof StoreManagerEntity) {
        			StoreManagerEntity manager_entity = (StoreManagerEntity) mngr;
        			
        			if (!manager_entity.isShopOpen()) {
        				if (player.getUniqueID().toString().equalsIgnoreCase(entity.ownerUuid)) {
        					player.sendMessage(new TextComponentTranslation("econ.shop.owner.closed"));
        				} else {
        					player.sendMessage(new TextComponentTranslation("econ.shop.closed"));
        				}
        				return false;
        			}
        			
        			if (isOwner) {
        				openGui(player, EconConstants.Gui.CART_DISPENSER_OWNER_1, world, pos);
            		} else {
            			if (manager_entity.isActiveCustomer(player)) {
            				Customer customer = manager_entity.getCustomerByUuid(player.getCachedUniqueIdString());
            				openGui(player, EconConstants.Gui.CART_DISPENSER_NOT_OWNER_1 + customer.getCartId(), world, pos);
            			} else {
	            			manager_entity.addActiveCustomer(player, entity);
	            		}
            		}
        		} else {
        			if (isOwner) {
        				player.sendMessage(new TextComponentTranslation("econ.shop.cart.unregistered"));
        			} else {
        				player.sendMessage(new TextComponentTranslation("econ.shop.closed"));
        			}
        			
        		}
        	}
        }
        return true;
	}
	
	public void openGui(EntityPlayer player, int id, World world, BlockPos pos) {
		player.openGui(FurenikusEconomy.instance, id, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public boolean registerToShopManager(EntityPlayer player, EnumHand hand, World world, BlockPos pos) {
		System.out.println("Begin registering to shop manager");
		ItemStack stack = player.getHeldItem(hand);
		TileEntity te = world.getTileEntity(pos);
		if (stack.getItem() instanceof StoreStockPairer && te != null && te instanceof CartDispenserEntity) {
			CartDispenserEntity cart = (CartDispenserEntity) te;
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
						if (sm.addCartDispenserToManager(pos)) {
							cart.setManager(managerPos);
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
		if (te != null && te instanceof CartDispenserEntity) {
			System.out.println("TE found");
			CartDispenserEntity cart = (CartDispenserEntity) te;
			if (cart.managerPos != null && cart.managerPos.getY() > 0) {
				TileEntity t = world.getTileEntity(cart.managerPos);
				
				if (t != null && t instanceof StoreManagerEntity) {
					System.out.println("Found manager");
					StoreManagerEntity sm = (StoreManagerEntity) t;
					
					return sm.removeStockFromManager(pos);
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
    
    @Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
    	return new CartDispenserEntity();
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
}
