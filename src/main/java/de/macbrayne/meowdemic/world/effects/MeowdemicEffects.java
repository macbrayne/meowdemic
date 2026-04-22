package de.macbrayne.meowdemic.world.effects;

import de.macbrayne.meowdemic.Meowdemic;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class MeowdemicEffects {
    public static final Holder<MobEffect> INFECTED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Meowdemic.id("infected"), new InfectionEffect());
    public static final Holder<MobEffect> IMMUNE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Meowdemic.id("immune"), new ImmunityEffect());
    public static final Holder<MobEffect> INCUBATING = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Meowdemic.id("incubating"), new IncubationEffect());

    public static void init() {

    }
}
