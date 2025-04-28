package net.somedom.taboo.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.somedom.taboo.Taboo;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Taboo.MOD_ID);

    public static final DeferredItem<Item> SALT  = ITEMS.register("salt",
            () -> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
