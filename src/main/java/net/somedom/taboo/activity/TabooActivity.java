package net.somedom.taboo.activity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.activity.attachment.AttachmentRegistry;

import java.util.Map;

enum tabooActivityType {POLTER};

@EventBusSubscriber(modid = Taboo.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class TabooActivity {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if(event.getEntity().level() instanceof ServerLevel serverLevel) {
            if (event.getSource().is(DamageTypes.FALL)) {
                serverLevel.setBlock(event.getEntity().blockPosition(), Blocks.SLIME_BLOCK.defaultBlockState(), 3);
                BlockPos deathPos = event.getEntity().blockPosition();
                ChunkAccess chunk = serverLevel.getChunk(deathPos);

                Map<String, Integer> activity = chunk.getData(AttachmentRegistry.ACTIVITY);
                activity.put("POLTER", activity.getOrDefault("POLTER", 0)+5);

                chunk.setData(AttachmentRegistry.ACTIVITY, activity);
            }
        }
    }
}