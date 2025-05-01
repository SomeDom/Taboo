package net.somedom.taboo.activity.attachment;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.somedom.taboo.Taboo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> CHUNK_ATTACHMENTS = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Taboo.MOD_ID
    );

    public static final Supplier<AttachmentType<Map<String, Integer>>> ACTIVITY = CHUNK_ATTACHMENTS.register(
            "activity",
            () -> AttachmentType.<Map<String, Integer>>builder(() -> new HashMap<>())
                    .serialize(Codec.unboundedMap(Codec.STRING, Codec.INT))
                    .build()
    );

    public static void register(IEventBus eventBus)
    {
        CHUNK_ATTACHMENTS.register(eventBus);
    }
}
