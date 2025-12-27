package net.somedom.taboo.entity.custom;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.somedom.taboo.mixin.activity.IActivityExtension;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TangoEntity extends PathfinderMob implements SmartBrainOwner <TangoEntity> {

    public TangoEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public List<ExtendedSensor<TangoEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyPlayersSensor<>(),
                new NearbyBlocksSensor<>(),
                new UnreachableTargetSensor<>()
        );
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends TangoEntity>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends TangoEntity>> activities = new HashMap<>();

        activities.put(Activity.CORE,
                new BrainActivityGroup<TangoEntity>(Activity.CORE)
                        .priority(10)
                        .behaviours(
                                new LookAtTarget<>(),
                                new MoveToWalkTarget<>()
                        ));

        activities.put(Activity.IDLE,
                new BrainActivityGroup<TangoEntity>(Activity.IDLE)
                        .priority(10)
                        .behaviours(
                                new FirstApplicableBehaviour<TangoEntity>(
                                        new TargetOrRetaliate<>(),
                                        new SetPlayerLookTarget<>(),
                                        new SetRandomLookTarget<>()),
                                new OneRandomBehaviour<>(
                                        new SetRandomWalkTarget<>(),
                                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                                )));

        /*activities.put(
                IActivityExtension.STALK,
                new BrainActivityGroup<TangoEntity>(IActivityExtension.STALK)
                        .priority(10)
                        .behaviours(
                                //nun for now
                        ));*/

        return activities;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float v) {
        if (!this.level().isClientSide) {
            if (!damageSource.is(DamageTypes.IN_FIRE) || v < 2.0F) {
                return false;
            }
            return super.hurt(damageSource, v);
        }
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 35.0);
    }

    private static class TangoMoodManagerBehavior extends ExtendedBehaviour<TangoEntity> {

        @Override
        protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
            return List.of();
        }


        @Override
        protected void start(TangoEntity entity) {

            if (entity.tickCount % 20 != 0) {
                return;
            }

            if (entity.getTarget() != null) {
                entity.getBrain().setActiveActivityIfPossible(IActivityExtension.STALK);
                return;
            }

            entity.getBrain().setActiveActivityIfPossible(Activity.IDLE);
        }
    }

}
