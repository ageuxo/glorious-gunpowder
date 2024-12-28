package io.github.ageuxo.gloriousgunpowder.client;

import io.github.ageuxo.gloriousgunpowder.client.render.BulletRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class BulletModel <T extends BulletRenderState> extends EntityModel<T> {
    public static final String MAIN = "main";
    protected final ModelPart main = this.root.getChild(MAIN);

    public BulletModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild(
                MAIN,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
                        .addBox(2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO
        );
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(@NotNull T renderState) {
        super.setupAnim(renderState);
        main.xScale = 0.5f;
        main.yScale = 0.5f;
        main.zScale = 0.5f;

        main.xRot = renderState.xRot;
        main.yRot = renderState.yRot;
    }
}
