package net.somedom.taboo.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.somedom.taboo.Taboo;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Taboo.MOD_ID);

    public static final Holder<MobEffect> GRACE = MOB_EFFECTS.register("grace",
            () -> new GraceMobEffect(MobEffectCategory.BENEFICIAL, 0xfff9c4));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
