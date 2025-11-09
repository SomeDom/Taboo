package net.somedom.taboo.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.somedom.taboo.Taboo;
import net.somedom.taboo.entity.custom.WendigoEntity;

public class WendigoModel<T extends WendigoEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Taboo.MOD_ID, "wendigo"), "main");
    private final ModelPart body;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart torso_bone;
    private final ModelPart torso;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart head;

    public WendigoModel(ModelPart root) {
        this.body = root.getChild("body");
        this.right_leg = this.body.getChild("right_leg");
        this.left_leg = this.body.getChild("left_leg");
        this.torso_bone = this.body.getChild("torso_bone");
        this.torso = this.torso_bone.getChild("torso");
        this.right_arm = this.torso_bone.getChild("right_arm");
        this.left_arm = this.torso_bone.getChild("left_arm");
        this.head = this.torso_bone.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 0.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 9.0F, 0.0F));

        PartDefinition torso_bone = body.addOrReplaceChild("torso_bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition torso = torso_bone.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -14.0F, -2.0F, 8.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = torso_bone.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.0F, -12.0F, 0.0F));

        PartDefinition right_arm_r1 = right_arm.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition left_arm = torso_bone.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.0F, -12.0F, 0.0F));

        PartDefinition left_arm_r1 = left_arm.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition head = torso_bone.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(WendigoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        // Base anim: Walk if moving, else idle
        if (entity.walkAnimationState.isStarted()) {
            this.animate(entity.walkAnimationState, WendigoAnimations.walk, ageInTicks, 1f);
        } else {
            this.animate(entity.idleAnimationState, WendigoAnimations.idle, ageInTicks, 1f);
        }

        // Attack overrides (applies on top, non-looping so short)
        this.animate(entity.attackAnimationState, WendigoAnimations.attack, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch * ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        torso_bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return body;
    }
}
