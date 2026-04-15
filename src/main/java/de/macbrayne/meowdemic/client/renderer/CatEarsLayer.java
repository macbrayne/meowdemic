package de.macbrayne.meowdemic.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import de.macbrayne.meowdemic.util.CatEarsStateAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class CatEarsLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final HumanoidModel<AvatarRenderState> model;

    public CatEarsLayer(final RenderLayerParent<AvatarRenderState, PlayerModel> renderer, final EntityModelSet modelSet) {
        super(renderer);
        this.model = new CatEarsModel(modelSet.bakeLayer(CatEarsModel.LAYER_LOCATION));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if(!state.isInvisible && ((CatEarsStateAccessor) state).meowdemic$getShowCatEars()) {
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
            submitNodeCollector.submitModel(
                    this.model, state, poseStack, RenderTypes.entitySolid(state.skin.body().texturePath()), lightCoords, overlayCoords, state.outlineColor, null
            );
        }
    }
}
