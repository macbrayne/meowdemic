package de.macbrayne.meowdemic.client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.resources.Identifier;

public class CatEarsModel extends PlayerModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("meowdemic", "cat_ears"), "main");

    public CatEarsModel(ModelPart root) {
        super(root, false);
    }

    public static LayerDefinition createEarsLayer() {
        MeshDefinition mesh = PlayerModel.createMesh(CubeDeformation.NONE, false);
        PartDefinition root = mesh.getRoot().clearRecursively();
        PartDefinition head = root.getChild("head");

        PartDefinition catEars = head.addOrReplaceChild("cat_ears", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, -7.0F, -8.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -6.0F, -8.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.0F, -6.0F, -8.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(3.0F, -6.0F, -8.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(1.0F, -6.0F, -8.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -7.0F, -8.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, -5.0F, -8.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -5.0F, -8.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 4.0F));

        return LayerDefinition.create(mesh, 16, 16);
    }
}
