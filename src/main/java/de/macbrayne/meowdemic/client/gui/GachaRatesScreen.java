package de.macbrayne.meowdemic.client.gui;

import com.mojang.blaze3d.platform.Window;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.data.Upgrades;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class GachaRatesScreen extends Screen {
    public GachaRatesScreen() {
        super(Component.literal("Details"));
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

        root.addChild(CommonGui.addHeader(this, minecraft.player, Component.translatable("gui.meowdemic.rates.title")));
        root.addChild(body);
        root.addChild(footer);

        // Add elements to body
        Label upgradeTitle = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.current.title", attachment.strain().name()))
                .padding(0, 0, 4, 0)
                .build();

        Paragraph common = Paragraph.builder()
                .text(Component.translatable("gui.meowdemic.rates.common", Upgrades.Rarity.COMMON.chance, Upgrades.Rarity.COMMON.pityChance))
                .width(true)
                .build();

        Paragraph uncommon = Paragraph.builder()
                .text(Component.translatable("gui.meowdemic.rates.uncommon", Upgrades.Rarity.UNCOMMON.chance, Upgrades.Rarity.UNCOMMON.pityChance))
                .width(true)
                .build();

        body.addChild(upgradeTitle);
        body.addChild(common);
        body.addChild(uncommon);

        // Add elements to footer
        Button gachaButton = Button.builder()
                .text(CommonComponents.GUI_DONE)
                .width(200)
                .onPress(_ -> minecraft.setScreen(new UpgradeGachaScreen()))
                .build();

        footer.addChild(gachaButton);

        // Add root as drawable child
        addRenderableWidget(root);
    }
}
