package io.github.ageuxo.gloriousgunpowder.client.model;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Function;

public class GroupGeometry implements IUnbakedGeometry<GroupGeometry> {
    private final BlockModel blockModel;
    private final List<BoneGroup> groups;

    public GroupGeometry(BlockModel blockModel, List<BoneGroup> groups) {
        this.blockModel = blockModel;
        this.groups = groups;
    }

    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker, @NotNull Function<Material, TextureAtlasSprite> spriteGetter, @NotNull ModelState modelState, @NotNull ItemOverrides overrides, @NotNull ResourceLocation modelLocation) {
        @SuppressWarnings("deprecation")
        List<BlockElement> elements = blockModel.getElements();
        Material particleLocation = context.getMaterial("particle");
        TextureAtlasSprite particle = spriteGetter.apply(particleLocation);

        if (this.groups.isEmpty()){
            this.groups.add(new BoneGroup("root", new Vector3f(), List.of()));
        }
        bakeGroups(context, baker, spriteGetter, modelState, overrides, modelLocation, elements, this.groups);

        return new GroupsModel(groups, context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(), particle, overrides);
    }

    private void bakeGroups(@NotNull IGeometryBakingContext context, @NotNull ModelBaker baker, @NotNull Function<Material, TextureAtlasSprite> spriteGetter, @NotNull ModelState modelState, @NotNull ItemOverrides overrides, @NotNull ResourceLocation modelLocation, List<BlockElement> elements, List<BoneGroup> boneGroups) {
        for (BoneGroup group : boneGroups){
            BakedModel bakedModel = new ElementsModel(group.bindElements(elements)).bake(context, baker, spriteGetter, modelState, overrides, modelLocation);
            group.setBakedModel(bakedModel);
            bakeGroups(context, baker, spriteGetter, modelState, overrides, modelLocation, elements, group.getChildGroups());
        }
    }

    @Override
    public void resolveParents(@NotNull Function<ResourceLocation, UnbakedModel> modelGetter, @NotNull IGeometryBakingContext context) {
        blockModel.resolveParents(modelGetter);
    }
}
