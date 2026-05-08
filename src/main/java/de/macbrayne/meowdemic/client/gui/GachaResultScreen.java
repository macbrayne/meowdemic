package de.macbrayne.meowdemic.client.gui;

import com.mojang.blaze3d.platform.Window;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Upgrades;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.*;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class GachaResultScreen extends Screen {
    private final Upgrades upgrade;
    protected GachaResultScreen(Upgrades upgrade) {
        super(Component.literal("Gacha Result"));
        this.upgrade = upgrade;
    }

    @Override
    protected void init() {
        Window window = minecraft.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();

        // The root panel that will contain everything
        Panel root = Panel.builder()
                .dimensions(width, height)
                .spacing(1)
                .build();

        // Add elements to root
        Panel body = ScrollPanel.ofMenu()
                .dimensions(true, true)
                .alignCenter()
                .padding(10, 0)
                .spacing(10)
                .build();
        Panel footer = Panel.builder()
                .dimensions(true, 32)
                .alignCenter()
                .alignMiddle()
                .padding(20, 0)
                .spacing(8)
                .flowAxis(FlowAxis.HORIZONTAL)
                .build();

        root.addChild(CommonGui.addSimpleHeader(Component.translatable("gui.meowdemic.gacha_result.title")));
        root.addChild(body);
        root.addChild(footer);

        body.addChild(Paragraph.builder()
                .text(Component.translatable("gui.meowdemic.gacha_result.congrats"))
                .width(true)
                .height(true)
                .build());
        body.addChild(Image.builder()
                .image(Meowdemic.id("textures/gui/upgrades/" + upgrade.getSerializedName() + ".png"))
                .dimensions(32, 32)
                        .tooltip(Tooltip.create(Component.translatable("gui.meowdemic.upgrades." + upgrade.getSerializedName())))
                .build());

        body.addChild(Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrades." + upgrade.getSerializedName() + ".description"))
                .width(true)
                .padding(0, 0, 4, 0)
                .build());

        // Add elements to footer
        Button returnButton = Button.builder()
                .text(CommonComponents.GUI_DONE)
                .width(200)
                .onPress(_ -> minecraft.setScreen(new UpgradeGachaScreen()))
                .build();

        footer.addChild(returnButton);

        // Add root as drawable child
        addRenderableWidget(root);
    }
}
