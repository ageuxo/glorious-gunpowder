package io.github.ageuxo.gloriousgunpowder.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupsModel implements IDynamicBakedModel {
    private final Map<Direction, Map<RenderType, List<BakedQuad>>> directionMapMap = new HashMap<>();
    private final List<BoneGroup> groups;
    private final boolean useAmbientOcclusion;
    private final boolean isGui3d;
    private final boolean usesBlockLight;
    private final TextureAtlasSprite particleIcon;
    private final ItemOverrides overrides;

    public GroupsModel(List<BoneGroup> groups, boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particleIcon, ItemOverrides overrides) {
        this.groups = groups;
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particleIcon = particleIcon;
        this.overrides = overrides;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        return directionMapMap.computeIfAbsent(side, (dir)->new HashMap<>()).computeIfAbsent(renderType, (type)->recursiveGetQuads(this.groups, state, side, rand, extraData, type));
    }

    public List<BoneGroup> getTopLevelGroups() {
        return groups;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return useAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return usesBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return particleIcon;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return overrides;
    }

    private List<BakedQuad> recursiveGetQuads(@NotNull List<BoneGroup> groups, @Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType){
        List<BakedQuad> quads = new ArrayList<>();
        for (BoneGroup group : groups){
            getGroupQuads(quads, group, state, side, rand, modelData, renderType);
            getAllSubQuads(quads, group, state, side, rand, modelData, renderType);
        }
        return quads;
    }

    private void getAllSubQuads(List<BakedQuad> quadList, BoneGroup group, @Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType){
        quadList.addAll(recursiveGetQuads(group.getChildGroups(), state, side, rand, modelData, renderType));
    }

    private void getGroupQuads(List<BakedQuad> quadList, BoneGroup group, @Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType){
        quadList.addAll(group.getBakedModel().getQuads(state, side, rand, modelData, renderType));
    }
}
