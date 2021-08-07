package com.fureniku.econ;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	@Instance
	public static FurenikusEconomy instance;
	
	public void registerItemRenderer(Item item, int meta, String id) {}
	
	public void preInit() {}
	public void init() {}
	public void postInit() {}
	
	public void openGui(int guiId) {}
}
