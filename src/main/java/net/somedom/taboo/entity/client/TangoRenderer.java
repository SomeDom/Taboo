package net.somedom.taboo.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.somedom.taboo.entity.custom.TangoEntity;

public class TangoRenderer extends EntityRenderer<TangoEntity> {

    public TangoRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TangoEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && entity.tickCount % 2 == 0 && !entity.isPassenger()) {

            double baseX = Mth.lerp(partialTick, entity.xo, entity.getX());
            double baseY = Mth.lerp(partialTick, entity.yo + 2, entity.getY());
            double baseZ = Mth.lerp(partialTick, entity.zo, entity.getZ());

            double offsetX = (entity.getRandom().nextDouble() - 0.5) * 0.4;
            double offsetY = (entity.getRandom().nextDouble() - 0.5) * 0.4;
            double offsetZ = (entity.getRandom().nextDouble() - 0.5) * 0.4;

            double speedX = (entity.getRandom().nextDouble() - 0.5) * 0.05;
            double speedY = entity.getRandom().nextDouble() * 0.0;
            double speedZ = (entity.getRandom().nextDouble() - 0.5) * 0.05;

            level.addParticle(ParticleTypes.SQUID_INK,
                    baseX + offsetX, baseY + offsetY, baseZ + offsetZ,
                    speedX, speedY, speedZ);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(TangoEntity entity) {
        return null;
    }
}
