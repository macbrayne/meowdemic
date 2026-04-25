package de.macbrayne.meowdemic.client.gui;

import com.mojang.blaze3d.platform.Window;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

public class UpgradeGachaScreen extends Screen {
    private RandomSource random;

    public UpgradeGachaScreen() {
        super(Component.literal("Upgrades"));
    }

    @Override
    protected void init() {
        TransmissionEvent attachment = TransmissionAttachment.get(minecraft.player).getOptional().get();
        PlayerStatsAttachment.PlayerStatsData playerStats = PlayerStatsAttachment.get(minecraft.player);

        Window window = minecraft.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();

        // The root panel that will contain everything
        Panel root = Panel.builder()
                .dimensions(width, height)
                .spacing(1)
                .build();

        // Add elements to root
        Panel header = Panel.builder()
                .dimensions(true, 32)
                .flowAxis(FlowAxis.HORIZONTAL)
                .alignCenter()
                .alignMiddle()
                .padding(20, 40, 0, 0)
                .build();
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

        root.addChild(header);
        root.addChild(body);
        root.addChild(footer);


        Label title = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.title"))
                .build();

        Panel empty = Panel.builder()
                .dimensions(true, 32)
                .build();

        Label points = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.points", playerStats.getPoints() * 160))
                .build();

        header.addChild(title);
        header.addChild(empty);
        header.addChild(points);

        // Add elements to body
        Image image = Image.builder()
                .image(Meowdemic.id("textures/gui/upgrades/cat_ears.png"))
                .dimensions(16, 16)
                .build();

        Label upgradeTitle = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.current.title", attachment.strain().name()))
                .padding(0, 0, 4, 0)
                .build();
        Paragraph description = Paragraph.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.current.list", Component.translatable("gui.meowdemic.upgrade_gui.incubation", attachment.strain().incubationFactor()),
                        Component.translatable("gui.meowdemic.upgrade_gui.immunity", attachment.strain().immunityFactor()),
                        Component.translatable("gui.meowdemic.upgrade_gui.recovery", attachment.strain().recoveryFactor()),
                        Component.translatable("gui.meowdemic.upgrade_gui.transmission", attachment.strain().transmissionFactor())))
                .width(true)
                .height(true)
                .build();

        body.addChild(image);
        body.addChild(upgradeTitle);
        body.addChild(description);

        // Add elements to footer
        TextField<String> searchField = TextField.ofString()
                .placeholder(Component.literal("Search..."))
                .width(true)
                .maxWidth(200)
                .build();
        Button doneButton = Button.builder()
                .text(Component.literal("Done"))
                .onPress(self -> onClose())
                .build();

        footer.addChild(searchField);
        footer.addChild(doneButton);

        // Add root as drawable child
        addRenderableWidget(root);
    }
}
