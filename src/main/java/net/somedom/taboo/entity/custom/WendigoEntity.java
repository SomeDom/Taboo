package net.somedom.taboo.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WendigoEntity extends Monster {

    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public WendigoEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 32.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Animal.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50d)
                .add(Attributes.ATTACK_DAMAGE, 6D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    private void setupAnimationStates() {
        if (this.level().isClientSide()) {
            boolean isMoving = this.getDeltaMovement().horizontalDistanceSqr() > 0.001D;

            if (isMoving) {
                this.walkAnimationState.startIfStopped(this.tickCount);
                this.idleAnimationState.stop();
            } else {
                this.walkAnimationState.stop();
                if (this.idleAnimationTimeout <= 0) {
                    this.idleAnimationTimeout = 60;
                    this.idleAnimationState.startIfStopped(this.tickCount);
                } else {
                    --this.idleAnimationTimeout;
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }
}
