package de.macbrayne.meowdemic.client.gui;

import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

public class UpgradeGachaScreen extends Screen {
    protected final LinearLayout layout = LinearLayout.vertical().spacing(8);
    private RandomSource random;

    public UpgradeGachaScreen() {
        super(Component.literal("Upgrades"));
    }

    @Override
    protected void init() {
        this.random = RandomSource.create();
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        this.layout.addChild(new StringWidget(this.title, this.font));
        this.layout.addChild(Button.builder(Component.literal("Upgrade"), b -> {
            Upgrades randUpgrade = Upgrades.getRandom(random);
            this.minecraft.getToastManager().addToast(
                    SystemToast.multiline(this.minecraft, SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.literal("Upgrade:"), Component.literal(randUpgrade + ""))
            );
            TransmissionAttachment.get(minecraft.player).mutate(randUpgrade.apply());
        }).width(280).build());

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }
}
