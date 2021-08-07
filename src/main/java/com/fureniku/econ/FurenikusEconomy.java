package com.fureniku.econ;

import org.lwjgl.opengl.Display;

import com.fureniku.econ.capability.CapabilityEventHandler;
import com.fureniku.econ.capability.currency.CurrencyCapability;
import com.fureniku.econ.capability.currency.CurrencyStorage;
import com.fureniku.econ.capability.currency.ICurrency;
import com.fureniku.econ.capability.customer.CustomerCapability;
import com.fureniku.econ.capability.customer.CustomerStorage;
import com.fureniku.econ.capability.customer.ICustomer;
import com.fureniku.econ.network.AddToCartPacket;
import com.fureniku.econ.network.CloseCartPacket;
import com.fureniku.econ.network.OpenGuiServerSide;
import com.fureniku.econ.network.ShopUpdatePacket;
import com.fureniku.econ.network.StockUpdatePacket;
import com.fureniku.econ.network.StoreManagerClearLogPacket;
import com.fureniku.econ.network.StoreManagerUpdatePacket;
import com.fureniku.econ.network.StoreManagerWithdrawPacket;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=FurenikusEconomy.MODID, version=FurenikusEconomy.VERSION)
public class FurenikusEconomy { 
	
	public static final String MODID = "furenikuseconomy";
	public static final String VERSION = "1.0.0";
	
    @Instance(FurenikusEconomy.MODID)
    public static FurenikusEconomy instance;

    @SidedProxy(clientSide="com.fureniku.econ.client.ClientProxy", serverSide="com.fureniku.econ.CommonProxy")
    public static CommonProxy proxy;
    public static EconUtils utils = new EconUtils();
    
	public static CreativeTabs tabEcon = new CreativeTabs("tabEcon") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(EconItems.note10000, 1, 0);
		}
	};
	
	public static String configPath;
	public static SimpleNetworkWrapper network;
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	//This stuff only runs in my dev environment, where two customers and a seller launch alongside the server.
    	//This lets me rename the game windows so I can easily tell who is who on three identical Minecraft games :) 
    	if (event.getSide().equals(Side.CLIENT)) {
    		if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
    			devDebugSetup();
    		}
    	}
    	
    	CapabilityManager.INSTANCE.register(ICurrency.class, new CurrencyStorage(), CurrencyCapability.class);
    	CapabilityManager.INSTANCE.register(ICustomer.class, new CustomerStorage(), CustomerCapability.class);
    	
    	network = NetworkRegistry.INSTANCE.newSimpleChannel("FurenikusEconomy");
    	
    	network.registerMessage(StoreManagerUpdatePacket.Handler.class, StoreManagerUpdatePacket.class, 0, Side.SERVER);
    	network.registerMessage(StoreManagerClearLogPacket.Handler.class, StoreManagerClearLogPacket.class, 1, Side.SERVER);
    	network.registerMessage(StoreManagerWithdrawPacket.Handler.class, StoreManagerWithdrawPacket.class, 2, Side.SERVER);
    	
    	network.registerMessage(ShopUpdatePacket.Handler.class, ShopUpdatePacket.class, 4, Side.SERVER);
    	network.registerMessage(StockUpdatePacket.Handler.class, StockUpdatePacket.class, 5, Side.SERVER);
    	
    	network.registerMessage(OpenGuiServerSide.Handler.class, OpenGuiServerSide.class, 6, Side.SERVER);
    	
    	network.registerMessage(CloseCartPacket.Handler.class, CloseCartPacket.class, 7, Side.SERVER);
    	network.registerMessage(AddToCartPacket.Handler.class, AddToCartPacket.class, 8, Side.SERVER);
    	
    	
    	
    	//Handler class, Packet class, Packet ID (+1), RECIEVING Side
    	//network.registerMessage(ATMWithdrawPacket.Handler.class, ATMWithdrawPacket.class, 0, Side.SERVER);
    	//network.registerMessage(SoundPacket.Handler.class, SoundPacket.class, 1, Side.SERVER);
    	//network.registerMessage(ServerBalancePacket.Handler.class, ServerBalancePacket.class, 2, Side.CLIENT);
    	//network.registerMessage(AdminShopPricePacket.Handler.class, AdminShopPricePacket.class, 3, Side.CLIENT);
    	//network.registerMessage(SalePacket.Handler.class, SalePacket.class, 4, Side.SERVER);
    	//network.registerMessage(AdminShopClientPacket.Handler.class, AdminShopClientPacket.class, 5, Side.SERVER);
    	//network.registerMessage(FloatingShelvesPricePacket.Handler.class, FloatingShelvesPricePacket.class, 6, Side.CLIENT);
    	//network.registerMessage(FloatingShelvesClientPacket.Handler.class, FloatingShelvesClientPacket.class, 7, Side.SERVER);
    	//network.registerMessage(FloatingShelvesSalePacket.Handler.class, FloatingShelvesSalePacket.class, 8, Side.SERVER);
    	//network.registerMessage(StockChestUpdatePacket.Handler.class, StockChestUpdatePacket.class, 9, Side.SERVER);
	    MinecraftForge.EVENT_BUS.register(new EconEventHandler());
	    MinecraftForge.EVENT_BUS.register(new CapabilityEventHandler());
	    
	    proxy.preInit();
	    /*if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
	    	MinecraftForge.EVENT_BUS.register(new StoreStockInfoRender(Minecraft.getMinecraft()));
	    }*/
    }
               
    @EventHandler
    public void Init(FMLInitializationEvent event) {
    	proxy.init();
    	utils.init();
    	NetworkRegistry.INSTANCE.registerGuiHandler(FurenikusEconomy.instance, new GuiHandler());
    }
    
    public void devDebugSetup() {
    	String username = Minecraft.getMinecraft().getSession().getUsername();
		if (username.equalsIgnoreCase("Seller")) {
			Display.setTitle("Seller");
		}
		if (username.equalsIgnoreCase("Customer")) {
			Display.setTitle("Customer 1");
		}
		if (username.equalsIgnoreCase("Customer_2")) {
			Display.setTitle("Customer 2");
		}
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
    
    @Mod.EventBusSubscriber
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			EconItems.register(event.getRegistry());
			EconBlocks.registerItemBlocks(event.getRegistry());
		}
		
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			EconItems.registerModels();
			EconBlocks.registerModels();
		}
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			EconBlocks.register(event.getRegistry());
			EconBlocks.registerTileEntities();
		}
	}
    
    public static void log(int level, String msg) {
    	//if (level <= EconConfig.debugLevel) {
    		System.out.println("[Fureniku's Economy] " + msg);
    	//}
    }
};