package net.somedom.taboo.mixin.activity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.schedule.Activity;
import net.somedom.taboo.Taboo;

public interface IActivityExtension {

    Activity STALK = Registry.register(
            BuiltInRegistries.ACTIVITY,
            ResourceLocation.fromNamespaceAndPath(Taboo.MOD_ID, "stalk"),
            new Activity("taboo:stalk")
    );

}
