package net.somedom.taboo.entity.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Predicate;

public class WalkToNearestLivingEntity<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = MemoryTest.builder(1).usesMemories(MemoryModuleType.NEAREST_LIVING_ENTITIES);

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected Predicate<LivingEntity> targetPredicate = entity -> true;
    protected float speedModifier = 1.0f;

    public WalkToNearestLivingEntity<E> speedModifier(float modifier) {
        this.speedModifier = modifier;
        return this;
    }

    public WalkToNearestLivingEntity<E> filter(Predicate<LivingEntity> predicate) {
        this.targetPredicate = predicate;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (entity.isPassenger()) {
            return false;
        }

        NearestVisibleLivingEntities nearbyEntities = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

        if (nearbyEntities == null) return false;

        return nearbyEntities.findClosest(this.targetPredicate).isPresent();
    }

    @Override
    protected void start(E entity) {
        NearestVisibleLivingEntities nearbyEntities = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

        if(nearbyEntities != null) {
            LivingEntity target = nearbyEntities.findClosest(this.targetPredicate).orElse(null);

            if (target != null && target != entity && !target.getPersistentData().getBoolean("tango_possessed")) {
                EntityTracker tracker = new EntityTracker(target, true);
                WalkTarget walkTarget = new WalkTarget(tracker, this.speedModifier, 1);

                BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, walkTarget);
            }
        }
    }

}
