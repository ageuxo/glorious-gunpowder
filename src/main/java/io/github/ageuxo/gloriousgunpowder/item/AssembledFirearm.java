package io.github.ageuxo.gloriousgunpowder.item;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.gloriousgunpowder.GloriousGunpowderMod;
import io.github.ageuxo.gloriousgunpowder.anim.AnimationSyncManager;
import io.github.ageuxo.gloriousgunpowder.client.anim.AnimatableInstance;
import io.github.ageuxo.gloriousgunpowder.client.render.AssembledGunRenderer;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.datagen.MaterialProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeTagProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AssembledFirearm extends BaseFirearm{
    public static final Logger LOGGER = LogUtils.getLogger();

    @NotNull
    public static HashMap<String, ResourceLocation> createTestModelLookup() {
        HashMap<String, ResourceLocation> map = new HashMap<>();
        map.put("root", GloriousGunpowderMod.rl("root"));
        map.put(PartShapeTagProvider.STOCKS.location().getPath(), PartShapeProvider.STANDARD_STOCK.location());
        map.put(PartShapeTagProvider.LOCKWORKS.location().getPath(), PartShapeProvider.MATCHLOCK.location());
        map.put(PartShapeTagProvider.BARRELS.location().getPath(), PartShapeProvider.UNRIFLED_BARREL.location());
        return map;
    }

    public AssembledFirearm(Properties pProperties) {
        super(pProperties
                .component(GunDataComponents.GUN_COMPONENTS,
                        List.of(
                                new GunComponents(MaterialProvider.WOOD.location(), PartShapeProvider.STANDARD_STOCK.location()),
                                new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.MATCHLOCK.location()),
                                new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.UNRIFLED_BARREL.location())
                        )
                )
                .component(GunDataComponents.MODEL_LOOKUP, AssembledFirearm.createTestModelLookup())
                .component(GunDataComponents.ANIM_INSTANCE_ID, 0L) //TODO set this via AnimatableTracker when assembling
        );
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AssembledGunRenderer.INSTANCE;
            }
        });
    }

    @Override
    public void animateTrigger(Level level, LivingEntity livingEntity, ItemStack stack) {
        animate(level, livingEntity, stack, "lockworks", "trigger");
    }

    @Override
    public void animateRearm(Level level, LivingEntity livingEntity, ItemStack stack) {
        animate(level, livingEntity, stack, "lockworks", "trigger");
    }

    public void animate(Level level, LivingEntity livingEntity, ItemStack stack, String part, String animation){
        Long id = stack.getComponents().get(GunDataComponents.ANIM_INSTANCE_ID.get());
        if (id != null){
            if (level.isClientSide){
                AnimatableInstance instance = AssembledGunRenderer.INSTANCE.getInstance(id);
                var modelLookup = stack.getComponents().get(GunDataComponents.MODEL_LOOKUP.get());
                if (modelLookup != null) {
                    instance.playAnimation(modelLookup.getOrDefault(part, GloriousGunpowderMod.rl("empty")), animation, level.getGameTime());
                } else {
                    LOGGER.warn("Attempted to play non-existent animation on instance: {}, item: {}, entity: {}", id, stack, livingEntity);
                }
            } else {
                AnimationSyncManager.syncAnimation(livingEntity, id, animation);
            }
        }
    }
}
