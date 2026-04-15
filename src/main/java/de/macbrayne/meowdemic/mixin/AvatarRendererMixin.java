package de.macbrayne.meowdemic.mixin;

import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.client.renderer.CatEarsLayer;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.util.CatEarsStateAccessor;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    public AvatarRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    public void meowdemic$extractContext(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if(entity instanceof Player player && TransmissionComponent.get(player).hasSymptom(Symptoms.CAT_EARS)) {
            ((CatEarsStateAccessor) state).meowdemic$setShowCatEars(true);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At("RETURN"))
    void meowdemic$init(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        addLayer(new CatEarsLayer(this, context.getModelSet()));
    }
}
