package net.somedom.taboo.block;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.somedom.taboo.Taboo;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Taboo.MOD_ID);



    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
