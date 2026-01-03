package net.somedom.taboo.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.component.ModDataComponents;
import net.somedom.taboo.item.EmfReader;
import net.somedom.taboo.item.ModItems;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.EMF_READER.get(),
                ResourceLocation.fromNamespaceAndPath(Taboo.MOD_ID, "emf_level"),
                (stack, level, entity, seed) -> {
                    return stack.getOrDefault(ModDataComponents.EMF_LEVEL, 0);
                });
    }
}
