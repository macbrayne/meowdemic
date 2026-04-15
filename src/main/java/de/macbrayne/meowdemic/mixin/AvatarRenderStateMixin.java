package de.macbrayne.meowdemic.mixin;

import de.macbrayne.meowdemic.util.CatEarsStateAccessor;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin extends HumanoidRenderState implements CatEarsStateAccessor {
    @Unique
    private boolean showCatEars = false;

    @Override
    public boolean meowdemic$getShowCatEars() {
        return showCatEars;
    }

    @Override
    public void meowdemic$setShowCatEars(boolean showCatEars) {
        this.showCatEars = showCatEars;
    }
}
