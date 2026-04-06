package net.somedom.taboo.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.effect.ModEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, Taboo.MOD_ID);

    public static final Holder<Potion> HOLY_WATER = POTIONS.register("taboo_holy_water",
            () -> new Potion(new MobEffectInstance(ModEffects.GRACE, 600, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
