package de.macbrayne.meowdemic.client.gui;

import com.mojang.blaze3d.platform.Window;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.data.Upgrades;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import dev.chailotl.bento_gui.client.FlowAxis;
import dev.chailotl.bento_gui.client.elements.Image;
import dev.chailotl.bento_gui.client.elements.Label;
import dev.chailotl.bento_gui.client.elements.Panel;
import dev.chailotl.bento_gui.client.elements.ScrollPanel;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class GachaPullScreen extends Screen {
    private static final RandomSource random = RandomSource.create();
    private final Upgrades upgrade;

    public GachaPullScreen(Upgrades upgrade) {
        super(Component.literal("Gacha Pull"));
        this.upgrade = upgrade;
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

        Panel header = Panel.builder()
                .dimensions(true, 32)
                .flowAxis(FlowAxis.HORIZONTAL)
                .alignCenter()
                .alignMiddle()
                .padding(10, 0)
                .spacing(8)
                .build();
        Panel body = ScrollPanel.ofMenu()
                .dimensions(true, true)
                .alignCenter()
                .padding(0, 0)
                .spacing(5)
                .build();

        Label title = Label.builder()
                .text(Component.translatable("gui.meowdemic.gacha_pull.title"))
                .build();

        Panel empty = Panel.builder()
                .dimensions(true, 32)
                .build();

        header.addChild(title);
        header.addChild(empty);

        RandomSource random = RandomSource.create();
        int numPanels = height / 32 - 2;
        int chosenPanel = random.nextInt(numPanels);
        for(int i = 0; i < numPanels; i++) {
            body.addChild(animatedPanel(width, i % 2 == 0, i == chosenPanel));
        }

        root.addChild(header);
        root.addChild(body);


        // Add root as drawable child
        addRenderableWidget(root);
    }

    public Panel animatedPanel(int width, boolean reversed, boolean chosen) {
        int items = width / (32 + 5) + 4;
        int chosenItem = chosen ? random.nextInt(4, items - 4) : -1;
        AnimatedPanelRenderState renderState = new AnimatedPanelRenderState(minecraft, width, height, reversed, chosenItem);
        Panel panel = Panel.builder()
                .dimensions(width, 32)
                .alignCenter()
                .alignMiddle()
                .flowAxis(FlowAxis.HORIZONTAL)
                .renderOperations(renderState::render)
                .build();
        RandomSource random = RandomSource.create();
        // Get a random selection of upgrades
        List<Upgrades> list = new ArrayList<>();
        for(int i = 0; i < items; i++) {
            if(random.nextFloat() < Upgrades.Rarity.COMMON.chance) {
                list.add(Upgrades.COMMON_POOL.get(random.nextInt(Upgrades.COMMON_POOL.size())));
            } else {
                list.add(Upgrades.UNCOMMON_POOL.get(random.nextInt(Upgrades.UNCOMMON_POOL.size())));
            }
        }
        for(int i = 0; i < items; i++) {
            Upgrades upgrade = list.get(i);
            if(chosen && i == chosenItem) {
                upgrade = this.upgrade;
            }
            Image icon = Image.builder()
                    .image(Meowdemic.id("textures/gui/upgrades/" + upgrade.getSerializedName() + ".png"))
                    .dimensions(32, 32)
                    .build();
            panel.addChild(icon);
        }
        panel.addChild(Image.builder()
                .image(Meowdemic.id("textures/gui/upgrades/highlight.png"))
                .dimensions(32, 32)
                .build());
        return panel;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta) {
        super.extractRenderState(graphics, mouseX, mouseY, tickDelta);

    }
}
