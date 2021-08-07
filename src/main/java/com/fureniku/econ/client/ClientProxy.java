package com.fureniku.econ.client;

import com.fureniku.econ.CommonProxy;
import com.fureniku.econ.FurenikusEconomy;
import com.fureniku.econ.blocks.shop.ShelvesFullEntity;
import com.fureniku.econ.client.render.ShelvesFullRenderer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
    
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + id, "inventory"));
	}
	
	@Override
	public void preInit() {
		System.out.println("Register store stock info renderer");
		MinecraftForge.EVENT_BUS.register(new StoreStockInfoRender());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
	
	@Override
	public void init() {
		ClientRegistry.bindTileEntitySpecialRenderer(ShelvesFullEntity.class, new ShelvesFullRenderer());
	}
}