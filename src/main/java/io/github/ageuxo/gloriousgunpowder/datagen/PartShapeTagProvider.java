package io.github.ageuxo.gloriousgunpowder.datagen;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PartShapeTagProvider extends TagsProvider<PartShape> {

    public PartShapeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, PartShape.KEY, lookup, GloriousGunpowderMod.MOD_ID, existingFileHelper);
    }



    public static final TagKey<PartShape> STOCKS = createKey("stocks");
    public static final TagKey<PartShape> BARRELS = createKey("barrels");
    public static final TagKey<PartShape> LOCKWORKS = createKey("lockworks");

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(STOCKS).add(PartShapeProvider.STANDARD_STOCK);
        tag(BARRELS).add(PartShapeProvider.UNRIFLED_BARREL);
        tag(LOCKWORKS).add(PartShapeProvider.MATCHLOCK);

    }

    private static TagKey<PartShape> createKey(String path){
        return TagKey.create(PartShape.KEY, GloriousGunpowderMod.rl(path));
    }
}
