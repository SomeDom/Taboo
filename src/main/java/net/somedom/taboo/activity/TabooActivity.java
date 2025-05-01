package net.somedom.taboo.activity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.activity.attachment.AttachmentRegistry;

import java.util.HashMap;

@EventBusSubscriber(modid = Taboo.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class TabooActivity {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel && event.getEntity() instanceof Villager) {
            if (
                    event.getSource().is(DamageTypes.IN_FIRE) ||
                    event.getSource().is(DamageTypes.ON_FIRE) ||
                    event.getSource().is(DamageTypes.CAMPFIRE) ||
                    event.getSource().is(DamageTypes.LAVA) ||
                    event.getSource().is(DamageTypes.HOT_FLOOR)
            ) {
                BlockPos deathPos = event.getEntity().blockPosition();
                ChunkAccess chunk = serverLevel.getChunk(deathPos);

                HashMap<String, Integer> activity = new HashMap<>(chunk.getData(AttachmentRegistry.ACTIVITY));

                activity.put("SPIRIT",activity.getOrDefault("SPIRIT", 0) + 5);
                chunk.setData(AttachmentRegistry.ACTIVITY, activity);
                chunk.setUnsaved(true);
            }
        }
    }
}