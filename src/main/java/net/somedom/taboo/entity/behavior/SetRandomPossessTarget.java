package net.somedom.taboo.entity.behavior;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Predicate;

public class SetRandomPossessTarget<E extends LivingEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(Pair.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));

    protected Predicate<? super LivingEntity> targetPredicate = entity -> true;
    protected Predicate<E> canTargetPredicate = entity -> true;

    public SetRandomPossessTarget<E> targetPredicate(Predicate<? super LivingEntity> predicate) {
        this.targetPredicate = predicate;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return this.canTargetPredicate.test(entity);
    }

    @Override
    protected void start(E entity) {
        NearestVisibleLivingEntities nearbyEntities = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        LivingEntity target = nearbyEntities.findClosest(t -> this.targetPredicate.test(t)).orElse(null);

        if (target == null) { // No valid target, we'll make sure the entity isn't still targeting anything
            BrainUtils.clearMemory(entity, MemoryModuleType.ATTACK_TARGET);
        }
        else { // Target found, set the target in memory, and reset the unreachable target timer
            BrainUtils.setMemory(entity, MemoryModuleType.ATTACK_TARGET, target);
            BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
    }

}
