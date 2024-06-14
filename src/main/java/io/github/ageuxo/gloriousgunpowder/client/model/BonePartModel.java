package io.github.ageuxo.gloriousgunpowder.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BonePartModel implements IDynamicBakedModel {
    private final Map<Direction, List<BakedQuad>> directionQuads = new HashMap<>();
    private final ImmutableMap<String, BakedModel> bakedParts;
    private final boolean gui3d;
    private final boolean useBlockLight;
    private final boolean useAmbientOcclusion;
    private final TextureAtlasSprite particle;
    private final ItemTransforms itemTransforms;
    private final ItemOverrides itemOverrides;

    public BonePartModel(ImmutableMap<String, BakedModel> bakedParts, boolean gui3d, boolean useBlockLight, boolean useAmbientOcclusion, TextureAtlasSprite particle, ItemTransforms itemTransforms, ItemOverrides itemOverrides) {
        this.bakedParts = bakedParts;
        this.gui3d = gui3d;
        this.useBlockLight = useBlockLight;
        this.useAmbientOcclusion = useAmbientOcclusion;
        this.particle = particle;
        this.itemTransforms = itemTransforms;
        this.itemOverrides = itemOverrides;
    }

    public ImmutableSet<ImmutableMap.Entry<String, BakedModel>> getBones(){
        return bakedParts.entrySet();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        return directionQuads.computeIfAbsent(side, dir->computeQuadsForSide(dir, rand, extraData, renderType));
    }

    @Override
    public boolean useAmbientOcclusion() {
        return useAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return gui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return useBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return itemOverrides;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemTransforms getTransforms() {
        return itemTransforms;
    }

    private List<BakedQuad> computeQuadsForSide(Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType){
        List<BakedQuad> quads = new ArrayList<>();
        for (var model : bakedParts.values()){
            quads.addAll(model.getQuads(null, side, rand, data, renderType));
        }
        return quads;
    }
}
