package de.macbrayne.meowdemic.client.gui;

import com.mojang.blaze3d.platform.Window;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.network.ServerboundGachaRequestPacket;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

public class UpgradeGachaScreen extends Screen {
    private RandomSource random;
    private TransmissionEvent attachment;
    private PlayerStatsAttachment.PlayerStatsData playerStats;

    public UpgradeGachaScreen() {
        super(Component.literal("Upgrades"));
    }

    @Override
    protected void init() {
        attachment = TransmissionAttachment.get(minecraft.player).getOptional().get();
        playerStats = PlayerStatsAttachment.get(minecraft.player);

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

        root.addChild(CommonGui.addFullHeader(this, playerStats, Component.translatable("gui.meowdemic.upgrade_gui.title")));
        root.addChild(body);
        root.addChild(footer);

        // Add elements to body
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

        Label playerStatsLabel = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.player_stats.title"))
                .padding(0, 0, 4, 0)
                .build();

        Paragraph playerStatsDescription = Paragraph.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.player_stats.description", playerStats.getEntitiesInfected(), playerStats.getTimesCured() + 1, playerStats.getPoints()))
                .width(true)
                .height(true)
                .build();

        body.addChild(upgradeTitle);
        body.addChild(description);
        body.addChild(playerStatsLabel);
        body.addChild(playerStatsDescription);

        // Add elements to footer
        Button ratesButton = Button.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.details"))
                .width(60)
                .onPress(self -> minecraft.setScreen(new GachaRatesScreen(attachment, playerStats)))
                .build();
        Button historyButton = Button.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.history"))
                .width(60)
                .onPress(self -> minecraft.setScreen(new GachaHistoryScreen(attachment, playerStats)))
                .build();
        Panel emptyTwo = Panel.builder()
                .dimensions(true, 32)
                .build();
        Button gachaButton = Button.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.gacha"))
                .width(200)
                .onPress(self -> {
                    if(playerStats.numberOfPullsAffordable() > 0) {
                        ServerboundGachaRequestPacket packet = new ServerboundGachaRequestPacket();
                        ClientPlayNetworking.send(packet);
                    }
                })
                .build();
        if(playerStats.numberOfPullsAffordable() == 0) {
            gachaButton.setEnabled(false);
            gachaButton.setTooltip(Tooltip.create(Component.translatable("gui.meowdemic.upgrade_gui.gacha.tooltip.cant_afford").withStyle(ChatFormatting.RED)));
        }

        footer.addChild(ratesButton);
        footer.addChild(historyButton);
        footer.addChild(emptyTwo);
        footer.addChild(gachaButton);

        // Add root as drawable child
        addRenderableWidget(root);
    }

    public TransmissionEvent getTransmissionEvent() {
        return attachment;
    }

    public PlayerStatsAttachment.PlayerStatsData getPlayerStats() {
        return playerStats;
    }
}
