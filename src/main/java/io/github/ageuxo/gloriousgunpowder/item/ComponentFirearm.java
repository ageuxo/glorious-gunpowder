package io.github.ageuxo.gloriousgunpowder.item;

import io.github.ageuxo.gloriousgunpowder.GunRegistries;
import io.github.ageuxo.gloriousgunpowder.client.render.GunRenderer;
import io.github.ageuxo.gloriousgunpowder.data.GunComponents;
import io.github.ageuxo.gloriousgunpowder.data.GunDataComponents;
import io.github.ageuxo.gloriousgunpowder.data.Material;
import io.github.ageuxo.gloriousgunpowder.data.PartShape;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ComponentFirearm extends AbstractFirearm{
    public static Iterator<ResourceLocation> MATERIAL_ITERATOR;

    public ComponentFirearm(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isCrouching()){
            List<GunComponents> newComponents = new ArrayList<>();
            if (components().has(GunDataComponents.GUN_COMPONENTS.get())){
                if (MATERIAL_ITERATOR == null){
                   MATERIAL_ITERATOR = GunRegistries.MATERIALS.getRegistry().get().keySet().iterator();
                }
                List<GunComponents> components = components().get(GunDataComponents.GUN_COMPONENTS.get());
                if (components != null) {
                    for (GunComponents component : components){
                        newComponents.add(new GunComponents(MATERIAL_ITERATOR.next(), component.shape()));
                    }
                } else {
                    RegistryAccess registryAccess = pLevel.registryAccess();
                    newComponents.add(new GunComponents(registryAccess.registry(Material.KEY).get().getRandom(pLevel.random).get().key().location(), registryAccess.registry(PartShape.KEY).get().getRandom(pLevel.random).get().key().location()));
                    newComponents.add(new GunComponents(registryAccess.registry(Material.KEY).get().getRandom(pLevel.random).get().key().location(), registryAccess.registry(PartShape.KEY).get().getRandom(pLevel.random).get().key().location()));
                }
            }
            setNewComponents(pPlayer.getItemInHand(pHand), newComponents.toArray(GunComponents[]::new));
            return InteractionResultHolder.success(pPlayer.getItemInHand(pHand));
        } else {
            return super.use(pLevel, pPlayer, pHand);
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return GunRenderer.INSTANCE;
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        List<GunComponents> componentsList = pStack.getComponents().get(GunDataComponents.GUN_COMPONENTS.get());
        if (componentsList != null) {
            componentsList.forEach(components -> pTooltipComponents.add(Component.literal(components.toString())));
        }
    }
}
