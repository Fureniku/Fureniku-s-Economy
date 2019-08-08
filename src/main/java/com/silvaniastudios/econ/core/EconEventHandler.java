package com.silvaniastudios.econ.core;

import java.util.Random;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EconEventHandler {
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		String uuid = EntityPlayer.getUUID(player.getGameProfile()).toString();
		player.sendMessage(new TextComponentString(TextFormatting.GOLD + "You have logged in! There may or may not be completed transactions."));
	}

	@SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayerMP && EconConfig.mobsDropMoney) {
    		Random rand = new Random();
            int chance = rand.nextInt(200);
            if (event.getEntityLiving() instanceof EntityZombie) {
            	if (chance < 20) {
            		event.getEntityLiving().dropItem(EconItems.coin1, 1);
            	} else if (chance < 40) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 58) {
                    event.getEntityLiving().dropItem(EconItems.coin2, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                } else if (chance < 70) {
                    event.getEntityLiving().dropItem(EconItems.note100, 1);
                } else if (chance < 75) {
                    event.getEntityLiving().dropItem(EconItems.note200, 1);
                } else if (chance < 80) {
                    event.getEntityLiving().dropItem(EconItems.note500, 1);
                }
            }
            if (event.getEntityLiving() instanceof EntitySkeleton) {
                if (chance < 40) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 58) {
                    event.getEntityLiving().dropItem(EconItems.coin2, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 70) {
                    event.getEntityLiving().dropItem(EconItems.coin100, 1);
                } else if (chance < 75) {
                    event.getEntityLiving().dropItem(EconItems.coin50, 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(4) + 1);
                } else if (chance < 80) {
                    event.getEntityLiving().dropItem(EconItems.coin100, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin50, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(6) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(4) + 1);
                }
            }
            if (event.getEntityLiving() instanceof EntityCreeper) {
                if (chance < 40) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                } else if (chance < 60) {
                    event.getEntityLiving().dropItem(EconItems.coin50, 1);
                    event.getEntityLiving().dropItem(EconItems.coin100, 1);
                }
            }
            if (event.getEntityLiving() instanceof EntityEnderman) {
                if (chance < 12) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 26) {
                    event.getEntityLiving().dropItem(EconItems.coin2, rand.nextInt(5) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(3) + 1);
                } else if (chance < 32) {
                    event.getEntityLiving().dropItem(EconItems.coin100, 1);
                } else if (chance < 45) {
                    event.getEntityLiving().dropItem(EconItems.coin50, 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(4) + 1);
                } else if (chance < 52) {
                    event.getEntityLiving().dropItem(EconItems.note200, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.note100, 1);
                }
            }
            if (event.getEntityLiving() instanceof EntityPigZombie) {
                if (chance < 30) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 58) {
                    event.getEntityLiving().dropItem(EconItems.coin2, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                } else if (chance < 70) {
                    event.getEntityLiving().dropItem(EconItems.note100, 1);
                } else if (chance < 75) {
                    event.getEntityLiving().dropItem(EconItems.note200, 1);
                } else if (chance < 80) {
                    event.getEntityLiving().dropItem(EconItems.note500, 1);
                }
            }
            if (event.getEntityLiving() instanceof EntityWitch) {
                if (chance < 30) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 58) {
                    event.getEntityLiving().dropItem(EconItems.coin2, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                } else if (chance < 70) {
                    event.getEntityLiving().dropItem(EconItems.coin100, 1);
                } else if (chance < 75) {
                    event.getEntityLiving().dropItem(EconItems.coin50, 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt (2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(3) + 1);
                } else if (chance < 80) {
                    event.getEntityLiving().dropItem(EconItems.coin100, 1);
                    event.getEntityLiving().dropItem(EconItems.coin50, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(4) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(3) + 1);
                }
            }
            if (event.getEntityLiving() instanceof EntityVillager) {
                if (chance < 20) {
                    event.getEntityLiving().dropItem(EconItems.coin50, 1);
                } else if (chance < 48) {
                    event.getEntityLiving().dropItem(EconItems.note100, 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, 1);
                } else if (chance < 60) {
                    event.getEntityLiving().dropItem(EconItems.note200, 1);
                } else if (chance < 63) {
                    event.getEntityLiving().dropItem(EconItems.note200, 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(3) + 1);
                } else if (chance < 65) {
                    event.getEntityLiving().dropItem(EconItems.note100, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin50, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(4) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(3) + 1);
                }
            } else if (event.getEntityLiving() instanceof IMob) {
                if (chance < 40) {
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 58) {
                    event.getEntityLiving().dropItem(EconItems.coin2, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(1) + 1);
                } else if (chance < 70) {
                    event.getEntityLiving().dropItem(EconItems.coin100, 1);
                } else if (chance < 75) {
                    event.getEntityLiving().dropItem(EconItems.coin50, 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(4) + 1);
                } else if (chance < 80) {
                    event.getEntityLiving().dropItem(EconItems.coin100, rand.nextInt(1) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin50, rand.nextInt(2) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin25, rand.nextInt(3) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin10, rand.nextInt(6) + 1);
                    event.getEntityLiving().dropItem(EconItems.coin5, rand.nextInt(4) + 1);
                }
            }
        }
    }
}