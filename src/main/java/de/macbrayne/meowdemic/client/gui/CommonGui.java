package de.macbrayne.meowdemic.client.gui;

import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.Button;
import dev.chailotl.bento_gui.client.elements.Label;
import dev.chailotl.bento_gui.client.elements.Panel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CommonGui {
    public static Panel addHeader(Screen screen, Player player, Component titleComponent) {
        TransmissionEvent attachment = TransmissionAttachment.get(player).getOptional().get();
        PlayerStatsAttachment.PlayerStatsData playerStats = PlayerStatsAttachment.get(player);

        Panel header = Panel.builder()
                .dimensions(true, 32)
                .flowAxis(FlowAxis.HORIZONTAL)
                .alignCenter()
                .alignMiddle()
                .padding(10, 0)
                .spacing(8)
                .build();

        Label title = Label.builder()
                .text(titleComponent)
                .build();

        Panel empty = Panel.builder()
                .dimensions(true, 32)
                .build();

        Label points = Label.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.points", playerStats.getPoints()))
                .build();

        Button exit = Button.builder()
                .text(Component.translatable("gui.meowdemic.upgrade_gui.exit"))
                .onPress(self -> screen.onClose())
                .padding(0)
                .width(16)
                .build();



        header.addChild(title);
        header.addChild(empty);
        header.addChild(points);
        header.addChild(exit);

        return header;
    }
}
