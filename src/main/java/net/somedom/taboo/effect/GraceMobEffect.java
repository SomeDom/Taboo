package net.somedom.taboo.effect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.somedom.taboo.manifestation.stigma.StigmaManager;

public class GraceMobEffect extends MobEffect {
    protected GraceMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof ServerPlayer player) {
            StigmaManager.removeStigma(player, 1);
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 30 == 0;
    }
}
