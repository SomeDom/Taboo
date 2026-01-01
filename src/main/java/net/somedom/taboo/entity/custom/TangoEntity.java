package net.somedom.taboo.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.somedom.taboo.entity.behavior.AvoidEntitySpecific;
import net.somedom.taboo.entity.behavior.PossessNearestLivingEntity;
import net.somedom.taboo.entity.behavior.WalkToNearestLivingEntity;
import net.somedom.taboo.manifestation.stigma.StigmaManager;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothAmphibiousPathNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.InWaterSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.List;

public class TangoEntity extends PathfinderMob implements SmartBrainOwner <TangoEntity> {

    public TangoEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothAmphibiousPathNavigation(this, level);
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
                new InWaterSensor<>(),
                new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<TangoEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new FloatToSurfaceOfFluid<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<TangoEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<TangoEntity>(
                        new TargetOrRetaliate<TangoEntity>()
                                .attackablePredicate(entity -> {
                                    if (!(entity instanceof ServerPlayer player)) {
                                        return false;
                                    }
                                    return StigmaManager.getStigma(player) >= 20;
                                })
                                .startCondition(entity -> entity.getPersistentData().getBoolean("possessing"))
                                .cooldownFor(entity -> {
                                    LivingEntity target = entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
                                    if (!(target instanceof ServerPlayer player)) {
                                        return 0;
                                    }
                                    int stigma = StigmaManager.getStigma(player);
                                    return (60 * 20) / (stigma / 10);
                                }),

                        new PossessNearestLivingEntity<>()
                                .distance(1.2d)
                                .filter(e -> e instanceof Animal && !e.getPersistentData().getBoolean("tango_possessed")),

                        new WalkToNearestLivingEntity<>()
                                .speedModifier(2.0f)
                                .filter(e -> e instanceof Animal && !e.getPersistentData().getBoolean("tango_possessed")),

                        new AvoidEntitySpecific<>()
                                .avoiding(e -> e instanceof Player)
                                .noCloserThan(32.0f)
                                .stopCaringAfter(32.0f)
                                .speedModifier(2.0f)
                ),
                new OneRandomBehaviour<>(

                        new SetRandomWalkTarget<>(),

                        new Idle<>()
                                .runFor(entity -> entity.getRandom().nextInt(30, 60))
                ).startCondition(entity -> entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).isEmpty() || entity.getPersistentData().getBoolean("possessing"))
        );
    }

    @Override
    public BrainActivityGroup<? extends TangoEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(

                new InvalidateAttackTarget<TangoEntity>()
                        .invalidateIf((entity, target) -> !entity.getPersistentData().getBoolean("possessing")),

                new SetWalkTargetToAttackTarget<TangoEntity>()
                        .speedMod((entity, target) -> 1.5f)
                        .startCondition(entity -> entity.getPersistentData().getBoolean("possessing")),

                new AnimatableMeleeAttack<TangoEntity>(0)
                        .startCondition(entity -> entity.getPersistentData().getBoolean("possessing"))
        );
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force) {
        if (!(vehicle instanceof Animal) || vehicle.getPersistentData().getBoolean("tango_possessed")) {
            return false;
        }

        playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 0.5f, 1.5f);

        setInvisible(true);
        setInvulnerable(true);

        getPersistentData().putBoolean("possessing", true);
        vehicle.getPersistentData().putBoolean("tango_possessed", true);

        return super.startRiding(vehicle, force);
    }

    @Override
    public void stopRiding() {
        if (getVehicle() instanceof Entity entity) {
            if (!(entity instanceof Animal)) {
                return;
            }

            playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0f, 2f);
            setDeltaMovement(0.0f, 1.0f, 0.0f);

            setInvisible(false);
            setInvulnerable(false);

            getPersistentData().putBoolean("possessing", false);
            entity.getPersistentData().putBoolean("tango_possessed", false);
        }
        super.stopRiding();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float v) {
        if (!this.level().isClientSide) {
            if (!damageSource.is(DamageTypes.IN_FIRE) || v < 2.0F) {
                if (!damageSource.is(DamageTypes.LIGHTNING_BOLT)) {
                    return false;
                }
                return super.hurt(damageSource, v);
            }
            return super.hurt(damageSource, v);
        }
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 5.0d)
                .add(Attributes.ATTACK_DAMAGE, 5.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.23d)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

}
