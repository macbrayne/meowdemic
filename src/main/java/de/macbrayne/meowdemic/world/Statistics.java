package de.macbrayne.meowdemic.world;

import de.macbrayne.meowdemic.Meowdemic;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class Statistics {
    public static final Stat<Identifier> TOTAL_INFECTED = register("totalInfected", StatFormatter.DEFAULT);
    public static final Stat<Identifier> CURRENTLY_INFECTED = register("currentlyInfected", StatFormatter.DEFAULT);

    public static Stat<Identifier> register(String name, StatFormatter formatter) {
        Identifier registered = Registry.register(BuiltInRegistries.CUSTOM_STAT, name, Meowdemic.id(name));
        return Stats.CUSTOM.get(registered, formatter);
    }

    public static void init() {
    }
}
