package net.somedom.taboo.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.entity.ModEntities;
import net.somedom.taboo.entity.client.EchoModel;
import net.somedom.taboo.entity.custom.EchoEntity;

@EventBusSubscriber(modid = Taboo.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EchoModel.LAYER_LOCATION, EchoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ECHO.get(), EchoEntity.createAttributes().build());
    }
}
