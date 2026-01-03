package net.somedom.taboo.item;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.somedom.taboo.entity.custom.TangoEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EmfReader extends Item {

    public EmfReader(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level instanceof ServerLevel serverLevel) {

            AABB testArea = new AABB(entity.blockPosition()).inflate(32);
            List<LivingEntity> livingEntities = serverLevel.getEntitiesOfClass(LivingEntity.class, testArea);
            AtomicInteger emfLevel = new AtomicInteger();

            emfLevel.set(0);

            livingEntities.forEach(e -> {

                if (e instanceof TangoEntity) {
                    if (e.distanceTo(entity) <= 8) {
                        emfLevel.set(3);
                    } else if (e.distanceTo(entity) <= 16) {
                        emfLevel.set(2);
                    } else if (e.distanceTo(entity) <= 32) {
                        emfLevel.set(1);
                    }
                }
            });
            emfLevel.set(0);
        }
    }
}