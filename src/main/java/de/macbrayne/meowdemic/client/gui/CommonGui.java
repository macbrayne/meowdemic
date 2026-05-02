package de.macbrayne.meowdemic.client.gui;

import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.Button;
import dev.chailotl.bento_gui.client.elements.Label;
import dev.chailotl.bento_gui.client.elements.Panel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CommonGui {
    public static Panel addSimpleHeader(Component titleComponent) {
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

        header.addChild(title);
        header.addChild(empty);
        return header;
    }

    public static Panel addFullHeader(Screen screen, PlayerStatsAttachment.PlayerStatsData playerStats, Component titleComponent) {
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
