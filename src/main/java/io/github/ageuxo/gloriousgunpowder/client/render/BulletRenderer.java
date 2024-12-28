package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.client.BulletModel;
import io.github.ageuxo.gloriousgunpowder.entity.ModEntities;
import io.github.ageuxo.gloriousgunpowder.entity.projectile.BulletProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BulletRenderer<E extends BulletProjectile> extends EntityRenderer<E, BulletRenderState> {
    private static final ResourceLocation BULLET_LOCATION = GloriousGunpowderMod.rl("textures/entity/bullet.png");
    private final BulletModel<BulletRenderState> model;
    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BulletModel<>(context.bakeLayer(ModEntities.BULLET));
    }

    @Override
    public @NotNull BulletRenderState createRenderState() {
        return new BulletRenderState();
    }

    public @NotNull ResourceLocation getTextureLocation(BulletRenderState state) {
        return BULLET_LOCATION;
    }

    @Override
    public void render(@NotNull BulletRenderState state, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(state)));
        this.model.setupAnim(state);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(state, poseStack, bufferSource, packedLight);
    }

}
