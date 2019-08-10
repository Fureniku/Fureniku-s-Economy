package com.silvaniastudios.econ.core.client;

import com.silvaniastudios.econ.core.CommonProxy;
import com.silvaniastudios.econ.core.FurenikusEconomy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
    
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(FurenikusEconomy.MODID + ":" + id, "inventory"));
	}
}