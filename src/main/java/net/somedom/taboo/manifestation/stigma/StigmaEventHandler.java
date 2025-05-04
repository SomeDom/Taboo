package net.somedom.taboo.manifestation.stigma;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEnchantItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.somedom.taboo.Taboo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = Taboo.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class StigmaEventHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            LivingEntity victim = event.getEntity();

            if(victim.getType().getCategory() == MobCategory.CREATURE) {
                if (victim instanceof WanderingTrader) {
                    StigmaManager.addStigma(player, 10, true);
                }
                else {
                    StigmaManager.addStigma(player, 1, true);
                }
            }

            if (victim.isBaby() && victim.getType().getCategory() != MobCategory.MONSTER) {
                if (victim.getType().getCategory() == MobCategory.CREATURE) {
                    StigmaManager.addStigma(player, 4, true);
                }
                else {
                    StigmaManager.addStigma(player, 5, true);
                }
            }

            if (victim instanceof Villager) {
                StigmaManager.addStigma(player, 10, true);
            }

            if (
                victim instanceof Witch ||
                victim instanceof Evoker
            ) {
                StigmaManager.addStigma(player, 15, true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemConsumed(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            ItemStack item = event.getItem();

            if (
                item.getItem() == Items.POISONOUS_POTATO ||
                item.getItem() == Items.PUFFERFISH ||
                item.getItem() == Items.ROTTEN_FLESH ||
                item.getItem() == Items.SPIDER_EYE
            ) {
                StigmaManager.addStigma(player, 3, true);
            }

            if (
                item.getItem() == Items.SUSPICIOUS_STEW ||
                item.getItem() == Items.POTION
            ) {
                StigmaManager.addStigma(player, 5, true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            ItemStack item = event.getItemStack();

            if (
                item.getItem() == Items.ENDER_PEARL ||
                item.getItem() == Items.LINGERING_POTION ||
                item.getItem() == Items.SPLASH_POTION ||
                item.getItem() == Items.ENDER_EYE
            ) {
                StigmaManager.addStigma(player, 5, true);
            }
        }
    }

    @SubscribeEvent
    public static void onDeathByTotem(LivingUseTotemEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            StigmaManager.addStigma(player, 20, true);
        }
    }

    @SubscribeEvent
    public static void onEnchantItem(PlayerEnchantItemEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            StigmaManager.addStigma(player, 5, true);
        }
    }

    public static final Map<UUID, Integer> darkTicks = new HashMap<>();

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            UUID uuid = player.getUUID();

            if (!darkTicks.containsKey(uuid)) {
                darkTicks.put(uuid, 0);
            }

            int currentTicks = darkTicks.get(uuid);
            int lightLevel = player.level().getMaxLocalRawBrightness(player.blockPosition());

            if (lightLevel < 4) {
                currentTicks++;
                if (currentTicks >= 600) {
                    StigmaManager.addStigma(player, 1, true);
                    currentTicks = 0;
                }
            } else {
                if (currentTicks > 0) {
                    currentTicks--;
                }
            }

            darkTicks.put(uuid, currentTicks);
        }
    }
}
