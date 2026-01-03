package net.somedom.taboo.item;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.somedom.taboo.component.ModDataComponents;
import net.somedom.taboo.entity.custom.TangoEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

            Optional<LivingEntity> closest = livingEntities.stream()
                    .filter(e -> e instanceof TangoEntity)
                    .min(Comparator.comparingDouble(e -> e.distanceTo(entity)));

            int emfLevel = 0;

            if (closest.isPresent()) {
                double distance = closest.get().distanceTo(entity);

                if (distance <= 8) emfLevel = 3;
                else if (distance <= 16) emfLevel = 2;
                else if (distance <= 32) emfLevel = 1;
                else emfLevel = 0;
            }

            stack.set(ModDataComponents.EMF_LEVEL, emfLevel);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;

        return !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        stack.set(ModDataComponents.EMF_LEVEL, 0);

        super.onStopUsing(stack, entity, count);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        item.set(ModDataComponents.EMF_LEVEL, 0);

        return super.onDroppedByPlayer(item, player);
    }
}