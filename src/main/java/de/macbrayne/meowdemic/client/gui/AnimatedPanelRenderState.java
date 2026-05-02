package de.macbrayne.meowdemic.client.gui;

import de.macbrayne.meowdemic.data.Upgrades;
import dev.chailotl.bento_gui.client.RenderInfo;
import dev.chailotl.bento_gui.client.elements.BentoElement;
import dev.chailotl.bento_gui.client.elements.Panel;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

public class AnimatedPanelRenderState {
    private static final int ANIMATION_DURATION = 100; // Duration of the animation in ticks

    private final Minecraft client;
    private final Upgrades upgrade;
    private final boolean reversed;
    private final int width, height;
    private final int chosenItem;
    private float animationTick = 0;
    private float soundTick = 0;
    private boolean animationFinished = false, blastPlayed = false;

    public AnimatedPanelRenderState(Minecraft client, Upgrades upgrade, int width, int height, boolean reversed, int chosenItem) {
        this.client = client;
        this.upgrade = upgrade;
        this.width = width;
        this.height = height;
        this.reversed = reversed;
        this.chosenItem = chosenItem;
    }

    public void render(BentoElement self, RenderInfo render) {
        int multiplier = reversed ? -1 : 1;
        if (self instanceof Panel panel1) {
            float chosenItemEndLocation = 0;
            if(chosenItem != -1) {
                chosenItemEndLocation = panel1.getChildren().get(chosenItem).getX();
            }
            render.context().pose().pushMatrix();
            render.context().pose().translate((easeIn(animationTick / 100)) * width * multiplier, 0);
            for (BentoElement child : panel1.getChildren()) {
                if(child == panel1.getChildren().getLast()) {
                    if(chosenItem == -1) {
                        continue;
                    }
                    float clampedAnimationTick = Mth.clamp(animationTick, 0, ANIMATION_DURATION - 30) / (ANIMATION_DURATION - 30);
                    render.context().pose().pushMatrix();
                    render.context().pose().translate(chosenItemEndLocation - child.getX() - 2 * (easeIn(animationTick / 100)) * width * multiplier,
                            //(1 - easeInElastic(clampedAnimationTick)) * height);
                            (float) (height * Mth.cos(clampedAnimationTick * Math.PI * 9/2) * Math.exp(-clampedAnimationTick) * 0.5));
                }
                child.extractRenderState(render.context(), render.mouseX(), render.mouseY(), render.tickDelta());
                if(child == panel1.getChildren().getLast()) {
                    if(chosenItem == -1) {
                        continue;
                    }
                    render.context().pose().popMatrix();
                }
            }
            render.context().pose().popMatrix();

            if(animationTick < 100) {
                animationTick += render.tickDelta();
            } else if(!animationFinished) {
                animationFinished = true;
                client.player.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH);
            }

            if(animationFinished) {
                soundTick += render.tickDelta();
                if (soundTick >= 20 && !blastPlayed) {
                    blastPlayed = true;
                    client.player.playSound(SoundEvents.FIREWORK_ROCKET_BLAST);
                    client.player.playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE);
                    soundTick = 0;
                } else if (soundTick >= 40) {
                    client.setScreen(new GachaResultScreen(upgrade));
                }
            }
        }
    }

    float easeIn(float time) { // From 0 to 1, returns a value from 0 to 1 that represents the progress of the animation. The curve is ease-out quintic.
        return (float) (Math.pow(1 - time, 5));
    }

    float easeInElastic(float time) {
        double c4 = (2 * Math.PI) / 3;

        return time == 0
                ? 0
                : time == 1
                ? 1
                : (float)(Math.pow(2, -10 * time) * Math.sin((time * 10 - 0.75) * c4) + 1);
    }
}
