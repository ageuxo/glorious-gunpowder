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
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> block) {
        super(output, provider, block, GloriousGunpowderMod.MOD_ID);
    }
    @NotNull
    @Override
    public String getName() {
        return GloriousGunpowderMod.MOD_ID + " " + super.getName();
    }
    public static final TagKey<Item> BULLET = ItemTags.create(GloriousGunpowderMod.rl("bullet"));

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BULLET).add(ModItems.BULLET_ITEM.get());
    }
}
