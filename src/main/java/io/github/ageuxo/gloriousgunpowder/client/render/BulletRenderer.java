package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.BulletModel;
import io.github.ageuxo.gloriousgunpowder.entity.ModEntities;
import io.github.ageuxo.gloriousgunpowder.entity.projectile.BulletProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BulletRenderer<T extends BulletProjectile> extends EntityRenderer<T> {
    private static final ResourceLocation BULLET_LOCATION = GloriousGunpowderMod.rl("textures/entity/bullet.png");
    private final BulletModel<T> model;
    public BulletRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new BulletModel<>(pContext.bakeLayer(ModEntities.BULLET));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(T pEntity) {
        return BULLET_LOCATION;
    }

    public void render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.scale(0.5F, 0.5F, 0.5F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

}
