package net.somedom.taboo.entity.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AvoidEntitySpecific<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1).hasMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected Predicate<LivingEntity> avoidingPredicate = target -> false;
    protected float noCloserThanSqr = 9f;
    protected float stopAvoidingAfterSqr = 49f;
    protected float speedModifier = 1f;

    private Path runPath = null;

    public AvoidEntitySpecific<E> avoiding(Predicate<LivingEntity> predicate) {
        this.avoidingPredicate = predicate;
        return this;
    }

    public AvoidEntitySpecific<E> noCloserThan(float blocks) {
        this.noCloserThanSqr = blocks * blocks;
        return this;
    }

    public AvoidEntitySpecific<E> stopCaringAfter(float blocks) {
        this.stopAvoidingAfterSqr = blocks * blocks;
        return this;
    }

    public AvoidEntitySpecific<E> speedModifier(float modifier) {
        this.speedModifier = modifier;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        Optional<LivingEntity> target = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).findClosest(this.avoidingPredicate);

        if (target.isEmpty()) {
            return false;
        }

        if (entity.getPersistentData().getBoolean("possessing")) {
            return false;
        }

        LivingEntity avoidingEntity = target.get();
        double distToTarget = avoidingEntity.distanceToSqr(entity);

        if (distToTarget > this.noCloserThanSqr)
            return false;

        Vec3 runPos = LandRandomPos.getPosAway(entity, 16, 7, avoidingEntity.position());

        if (runPos == null || avoidingEntity.distanceToSqr(runPos.x, runPos.y, runPos.z) < distToTarget)
            return false;

        this.runPath = entity.getNavigation().createPath(runPos.x, runPos.y, runPos.z, 0);

        return this.runPath != null;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return !this.runPath.isDone();
    }

    @Override
    protected void start(E entity) {
        entity.getNavigation().moveTo(this.runPath, this.speedModifier);
    }

    @Override
    protected void stop(E entity) {
        this.runPath = null;

        entity.getNavigation().setSpeedModifier(1);
    }
}
