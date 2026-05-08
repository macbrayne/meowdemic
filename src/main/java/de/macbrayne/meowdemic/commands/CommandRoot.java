package de.macbrayne.meowdemic.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Config;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.ServerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.ImmunityAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.PlayerStatsAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class CommandRoot {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("meowdemic")
                .requires(Permissions.require("meowdemic.meowdemic", PermissionLevel.GAMEMASTERS))
                .then(Commands.literal("infect")
                        .requires(Permissions.require("meowdemic.meowdemic.infect", PermissionLevel.GAMEMASTERS))
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .then(Commands.argument("incubationFactor", FloatArgumentType.floatArg(0.1f, 10f))
                                        .then(Commands.argument("transmissionFactor", FloatArgumentType.floatArg(0.1f, 10f))
                                                .then(Commands.argument("recoveryFactor", FloatArgumentType.floatArg(0.1f, 10f))
                                                        .then(Commands.argument("immunityFactor", FloatArgumentType.floatArg(0.1f, 10f))
                                                                .then(Commands.argument("symptoms", StringArgumentType.greedyString())
                                                                        .executes(context -> {
                                                                            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                                                            float incubationFactor = FloatArgumentType.getFloat(context, "incubationFactor");
                                                                            float transmissionFactor = FloatArgumentType.getFloat(context, "transmissionFactor");
                                                                            float recoveryFactor = FloatArgumentType.getFloat(context, "recoveryFactor");
                                                                            float immunityFactor = FloatArgumentType.getFloat(context, "immunityFactor");
                                                                            String symptomsInput = StringArgumentType.getString(context, "symptoms");
                                                                            String[] symptomsArray = symptomsInput.split(",");
                                                                            HashSet<Symptoms> symptomsList = new HashSet<>();
                                                                            for (String symptomName : symptomsArray) {
                                                                                try {
                                                                                    Symptoms symptom = Symptoms.valueOf(symptomName.trim().toUpperCase());
                                                                                    symptomsList.add(symptom);
                                                                                } catch (IllegalArgumentException e) {
                                                                                    context.getSource().sendFailure(Component.translatable("commands.meowdemic.meowdemic.infect.invalid_symptom", symptomName));
                                                                                    return 0; // Return 0 to indicate failure
                                                                                }
                                                                            }
                                                                            Strain strain = new Strain(symptomsList, Strain.defaultEntitySet(), incubationFactor, transmissionFactor, recoveryFactor, immunityFactor);
                                                                            return infect(entities, strain, context);
                                                                        }))))))
                                .executes(context -> {
                                    RandomSource randomSource = RandomSource.create();
                                    Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                    Strain strain = Strain.random(randomSource);
                                    return infect(entities, strain, context); // Return a success code
                                })))
                .then(Commands.literal("stats")
                        .requires(Permissions.require("meowdemic.meowdemic.stats", PermissionLevel.GAMEMASTERS))
                        .then(Commands.literal("player").requires(Permissions.require("meowdemic.meowdemic.stats.player", PermissionLevel.GAMEMASTERS))
                                .then(
                                Commands.argument("player", EntityArgument.player()).executes(context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                    int timesInfected = PlayerStatsAttachment.get(target).getEntitiesInfected();
                                    int timesCured = PlayerStatsAttachment.get(target).getTimesCured();
                                    int points = PlayerStatsAttachment.get(target).getPoints();
                                    context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.stats.player", timesInfected, timesCured, points), false);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                        .then(Commands.literal("global").requires(Permissions.require("meowdemic.meowdemic.stats.global", PermissionLevel.GAMEMASTERS)).then(
                                Commands.literal("reset").requires(Permissions.require("meowdemic.meowdemic.stats.reset", PermissionLevel.ADMINS)).executes(context -> {
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
                        })))
                .then(Commands.literal("cure")
                        .requires(Permissions.require("meowdemic.meowdemic.cure", PermissionLevel.GAMEMASTERS))
                        .then(Commands.argument("entities", EntityArgument.entities())
                                .executes(context -> {
                                    return cure(EntityArgument.getEntities(context, "entities"));
                                })))
                .then(getConfig()));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> getConfig() {
        return Commands.literal("config")
                .requires(Permissions.require("meowdemic.meowdemic.config", PermissionLevel.ADMINS))
                .then(Commands.literal("reload")
                        .requires(Permissions.require("meowdemic.meowdemic.reload", PermissionLevel.ADMINS))
                        .executes(context -> {
                            Meowdemic.reloadConfig();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("get")
                        .requires(Permissions.require("meowdemic.meowdemic.config.get", PermissionLevel.ADMINS))
                        .executes(context -> {
                            Config config = Meowdemic.getConfig();
                            String incubationTimeMultiplier = String.format("%.2f", config.incubationTimeMultiplier());
                            String recoveryTimeMultiplier = String.format("%.2f", config.recoveryTimeMultiplier());
                            String immunityTimeMultiplier = String.format("%.2f", config.immunityTimeMultiplier());
                            String radiusMultiplier = String.format("%.2f", config.radiusMultiplier());
                            String vaccineMultiplier = String.format("%.2f", config.vaccineMultiplier());
                            String foodMultiplier = String.format("%.2f", config.foodMultiplier());
                            context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.config.get",
                                    incubationTimeMultiplier, recoveryTimeMultiplier, immunityTimeMultiplier,
                                    radiusMultiplier, vaccineMultiplier, foodMultiplier), false);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("set")
                        .requires(Permissions.require("meowdemic.meowdemic.config.set", PermissionLevel.ADMINS))
                        .then(Commands.literal("incubationTimeMultiplier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f, 10f))
                                        .executes(context ->
                                                modifyFloatConfig(value -> Meowdemic.getConfig().withIncubationTimeMultiplier(value), "incubationTimeMultiplier", context))))
                        .then(Commands.literal("recoveryTimeMultiplier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f, 10f))
                                        .executes(context ->
                                                modifyFloatConfig(value -> Meowdemic.getConfig().withRecoveryTimeMultiplier(value), "recoveryTimeMultiplier", context))))
                        .then(Commands.literal("immunityTimeMultiplier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f, 10f))
                                        .executes(context ->
                                                modifyFloatConfig(value -> Meowdemic.getConfig().withImmunityTimeMultiplier(value), "immunityTimeMultiplier", context))))
                        .then(Commands.literal("radiusMultiplier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f, 10f))
                                        .executes(context ->
                                                modifyFloatConfig(value -> Meowdemic.getConfig().withRadiusMultiplier(value), "radiusMultiplier", context))))
                        .then(Commands.literal("vaccineMultiplier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f))
                                        .executes(context ->
                                                modifyFloatConfig(value -> Meowdemic.getConfig().withVaccineMultiplier(value), "vaccineMultiplier", context))))
                        .then(Commands.literal("foodSymptomDurationMultiplier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f))
                                        .executes(context ->
                                                modifyFloatConfig(value -> Meowdemic.getConfig().withFoodMultiplier(value), "foodSymptomDurationMultiplier", context))))
                        .then(Commands.literal("minimumSpreadTime")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            int value = IntegerArgumentType.getInteger(context, "value");
                                            Meowdemic.setConfig(Meowdemic.getConfig().withMinimumSpreadTime(value));
                                            Meowdemic.saveConfig();
                                            context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.config.set", Component.translatable("commands.meowdemic.meowdemic.config.set.minimumSpreadTime"), value), false);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("spreadTimeModifier")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f))
                                        .executes(context -> modifyFloatConfig(value -> Meowdemic.getConfig().withSpreadTimeModifier(value), "spreadTimeModifier", context)))));
    }

    private static int modifyFloatConfig(Float2ObjectFunction<Config> function, String langKey, CommandContext<CommandSourceStack> context) {
        float value = FloatArgumentType.getFloat(context, "value");
        Meowdemic.setConfig(function.get(value));
        Meowdemic.saveConfig();
        context.getSource().sendSuccess(() -> Component.translatable("commands.meowdemic.meowdemic.config.set", Component.translatable("commands.meowdemic.meowdemic.config.set." + langKey), value), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int infect(Collection<? extends Entity> entities, Strain strain, CommandContext<CommandSourceStack> context) {
        int infectedCount = 0;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && IncubationAttachment.get(livingEntity).tryIncubate(new TransmissionEvent(Optional.empty(), livingEntity.getUUID(), strain))) {
                infectedCount++;
            }
        }
        ServerStatsAttachment.get(context.getSource().getLevel()).addCurrentlyInfected(infectedCount);
        return infectedCount;
    }

    private static int cure(Collection<? extends Entity> entities) {
        int curedCount = 0;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                IncubationAttachment.get(livingEntity).remove();
                TransmissionAttachment.get(livingEntity).remove();
                ImmunityAttachment.get(livingEntity).remove();
                curedCount++;
            }
        }
        return curedCount;
    }
}
