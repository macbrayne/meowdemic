package de.macbrayne.meowdemic.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public class CommandRoot {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("meowdemic")
                .then(Commands.argument("player", EntityArgument.player()).executes(context -> {
                    Player player = EntityArgument.getPlayer(context, "player");
                    Strain strain = new Strain("Test Strain", List.of(Symptoms.MEOWING, Symptoms.PURRING), 1, 1, 1);
                    TransmissionComponent.get(player).setIfNone(new TransmissionEvent(Optional.empty(), player.getUUID(), strain));
                    return Command.SINGLE_SUCCESS; // Return a success code
                })));
    }
}
