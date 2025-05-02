package net.somedom.taboo.entity.custom;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class SpiritEntity extends Monster {

    public SpiritEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAtrributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1d)
                .add(Attributes.MOVEMENT_SPEED, 1d)
                .add(Attributes.FOLLOW_RANGE, 8d);
    }
}