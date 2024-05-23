package io.github.ageuxo.gloriousgunpowder.datagen;

import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> block, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, block, GloriousGunpowderMod.MOD_ID, existingFileHelper);
    }
    @NotNull
    @Override
    public String getName() {
        return GloriousGunpowderMod.MOD_ID + " " + super.getName();
    }
    public static final TagKey<Item> BULLET = ItemTags.create(GloriousGunpowderMod.rl("bullet"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BULLET).add(ModItems.BULLET_ITEM.get());
    }
}
