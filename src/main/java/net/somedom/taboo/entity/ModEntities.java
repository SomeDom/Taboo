package net.somedom.taboo.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.entity.custom.EchoEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Taboo.MOD_ID);

    public static final Supplier<EntityType<EchoEntity>> ECHO =
            ENTITY_TYPES.register("echo", () -> EntityType.Builder.of(EchoEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.2f).build("echo"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
