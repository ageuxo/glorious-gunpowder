package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.client.render.AssembledGunRenderer;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.datagen.MaterialProvider;
import io.github.ageuxo.gloriousgunpowder.datagen.PartShapeProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AssembledFirearm extends BaseFirearm{

    public AssembledFirearm(Properties pProperties) {
        super(pProperties
                .component(GunDataComponents.GUN_COMPONENTS,
                        List.of(
                                new GunComponents(MaterialProvider.WOOD.location(), PartShapeProvider.STANDARD_STOCK.location()),
                                new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.MATCHLOCK.location()),
                                new GunComponents(MaterialProvider.IRON.location(), PartShapeProvider.UNRIFLED_BARREL.location())
                        )
                )
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
}
