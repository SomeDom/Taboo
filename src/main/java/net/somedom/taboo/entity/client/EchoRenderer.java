package net.somedom.taboo.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.entity.custom.EchoEntity;

public class EchoRenderer extends MobRenderer<EchoEntity, EchoModel<EchoEntity>> {
    public EchoRenderer(EntityRendererProvider.Context context) {
        super(context, new EchoModel<>(context.bakeLayer(EchoModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EchoEntity echoEntity) {
        return ResourceLocation.fromNamespaceAndPath(Taboo.MOD_ID, "textures/entity/wendigo/wendigo.png");
    }

    @Override
    public void render(EchoEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
