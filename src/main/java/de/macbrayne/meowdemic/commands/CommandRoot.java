package de.macbrayne.meowdemic.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.ServerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Optional;

public class CommandRoot {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("meowdemic")
                .then(Commands.literal("infect")
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .executes(context -> {
                                    Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                    Strain strain = new Strain("α.1 Cat", Symptoms.all(), 1, 1, 1);
                                    int infectedCount = 0;
                                    for (Entity entity : entities) {
                                        if (entity instanceof LivingEntity livingEntity && IncubationAttachment.get(livingEntity).tryIncubate(new TransmissionEvent(Optional.empty(), livingEntity.getUUID(), strain))) {
                                            infectedCount++;
                                        }
                                    }
                                    ServerStatsAttachment.get(context.getSource().getLevel()).addCurrentlyInfected(infectedCount);
                                    return infectedCount; // Return a success code
                                })))
                .then(Commands.literal("stats")
                        .then(Commands.literal("player").then(
                                Commands.argument("player", EntityArgument.player()).executes(context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                    int timesInfected = PlayerStatsAttachment.get(target).getEntitiesInfected();
                                    int timesCured = PlayerStatsAttachment.get(target).getTimesCured();
                                    int points = PlayerStatsAttachment.get(target).getPoints();
                                    context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.stats.player", timesInfected, timesCured, points), false);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                        .then(Commands.literal("global").then(
                                Commands.literal("reset").executes(context -> {
                                            boolean reset = ServerStatsAttachment.get(context.getSource().getLevel()).reset();
                                            if (!reset) {
                                                context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.stats.reset.confirm"), false);
                                            } else {
                                                context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.stats.reset"), false);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }
                                )).executes(context -> {
                            CommandSourceStack source = context.getSource();
                            ServerStatsAttachment.ServerStatsData stats = ServerStatsAttachment.get(source.getLevel());

                            int currentlyInfected = stats.getCurrentlyInfected();
                            int totalInfected = stats.getTotalInfected();
                            int speciesBarriersCrossed = stats.getSpeciesBarriersCrossed();
                            int strainsCreated = stats.getStrainsCreated();

                            source.sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.stats", currentlyInfected, totalInfected, speciesBarriersCrossed, strainsCreated), false);
                            return Command.SINGLE_SUCCESS;
                        }))));
    }
}
