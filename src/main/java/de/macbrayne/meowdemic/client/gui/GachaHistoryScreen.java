package de.macbrayne.meowdemic.client.gui;

import com.mojang.blaze3d.platform.Window;
import de.macbrayne.meowdemic.data.PullHistoryEvent;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.Button;
import dev.chailotl.bento_gui.client.elements.Label;
import dev.chailotl.bento_gui.client.elements.Panel;
import dev.chailotl.bento_gui.client.elements.ScrollPanel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GachaHistoryScreen extends Screen {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());
    protected GachaHistoryScreen() {
        super(Component.literal("Gacha History"));
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

        root.addChild(CommonGui.addHeader(this, minecraft.player, Component.translatable("gui.meowdemic.gacha_history.title")));
        root.addChild(body);
        root.addChild(footer);

        // Add elements to body
        Label strainName = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.current.title", attachment.strain().name()))
                .padding(0, 0, 4, 0)
                .build();
        body.addChild(strainName);

        Label historyDescription = Label.builder()
                .text(Component.translatable("gui.meowdemic.gacha_history.entries.description"))
                .width(true)
                .build();
        body.addChild(historyDescription);

        for(PullHistoryEvent upgrade : playerStats.getPullHistory()) {
            Label upgradeEntry = Label.builder()
                    .text(Component.translatable("gui.meowdemic.gacha_history.entries.entry", upgrade.upgrade().getSerializedName(), dateTimeFormatter.format(upgrade.timeReceived())))
                    .width(true)
                    .build();
            body.addChild(upgradeEntry);
        }

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
