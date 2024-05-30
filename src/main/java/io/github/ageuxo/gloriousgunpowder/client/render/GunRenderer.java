package io.github.ageuxo.gloriousgunpowder.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GunRenderer extends BlockEntityWithoutLevelRenderer {
    public static final List<ResourceLocation> MODEL_LOCATIONS = new ArrayList<>();
    public static final Map<ResourceLocation, BakedModel> MODEL_CACHE = new HashMap<>();
    public static GunRenderer INSTANCE;

    public GunRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(@NotNull ItemStack pStack, @NotNull ItemDisplayContext pDisplayContext, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        List<GunComponents> components = pStack.getComponents().get(GunDataComponents.GUN_COMPONENTS.get());
        Minecraft minecraft = Minecraft.getInstance();
        ModelBlockRenderer modelRenderer = minecraft.getBlockRenderer().getModelRenderer();
        ClientLevel level = minecraft.level;
        if (components != null && level != null){
            VertexConsumer consumer = pBuffer.getBuffer(RenderType.cutout());
            pPoseStack.pushPose();
            for (GunComponents component : components){
                BakedModel partModel = MODEL_CACHE.computeIfAbsent(component.shape(), GunRenderer::modelFromPartLocation);
                partModel = partModel.applyTransform(pDisplayContext, pPoseStack, false);
                pPoseStack.pushPose();
                modelRenderer.renderModel(pPoseStack.last(), consumer, null, partModel, 1f, 1f, 1f, pPackedLight, pPackedOverlay, ModelData.EMPTY, RenderType.cutout());

                pPoseStack.popPose();
            }
            pPoseStack.popPose();
        }
    }

    public static BakedModel modelFromPartLocation(ResourceLocation partLocation){
        ResourceLocation modelLocation = partLocation.withPrefix(GloriousGunpowderMod.MOD_ID+"/");
        ModelManager manager = Minecraft.getInstance().getModelManager();
        return manager.getModel(modelLocation);
    }
}
