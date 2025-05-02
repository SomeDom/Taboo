package net.somedom.taboo.stigma;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.somedom.taboo.Taboo;

@EventBusSubscriber(modid = Taboo.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class StigmaEventHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            LivingEntity victim = event.getEntity();

            // Killing passive mob adds 1 stigma # DEBUG
            if (victim.getType().getCategory().isFriendly()) {
                StigmaManager.addStigma(player, 1);
            }

            // Killing villager adds 5 stigma # DEBUG
            if (victim instanceof Villager) {
                StigmaManager.addStigma(player, 5);
            }
        }
    }
}
