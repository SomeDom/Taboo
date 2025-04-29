package net.somedom.taboo.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.block.ModBlocks;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Taboo.MOD_ID);

    public static final DeferredItem<BlockItem> SALT  = ITEMS.register("salt",
            () -> new BlockItem(ModBlocks.SALT.get(), new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
