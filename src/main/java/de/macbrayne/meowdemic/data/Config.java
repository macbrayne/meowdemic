package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;

public record Config(float incubationTimeMultiplier, float recoveryTimeMultiplier, float immunityTimeMultiplier,
                     float radiusMultiplier, float vaccineMultiplier, float foodMultiplier, int minimumSpreadTime,
                     float spreadTimeModifier) {
    public static Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("incubationTimeMultiplier").forGetter(Config::incubationTimeMultiplier),
            Codec.FLOAT.fieldOf("recoveryTimeMultiplier").forGetter(Config::recoveryTimeMultiplier),
            Codec.FLOAT.fieldOf("immunityTimeMultiplier").forGetter(Config::immunityTimeMultiplier),
            Codec.FLOAT.fieldOf("radiusMultiplier").forGetter(Config::radiusMultiplier),
            Codec.FLOAT.fieldOf("vaccineMultiplier").forGetter(Config::vaccineMultiplier),
            Codec.FLOAT.fieldOf("foodMultiplier").forGetter(Config::foodMultiplier),
            Codec.INT.fieldOf("minimumSpreadTime").forGetter(Config::minimumSpreadTime),
            Codec.FLOAT.fieldOf("spreadTimeModifier").forGetter(Config::spreadTimeModifier)
    ).apply(instance, Config::new));

    public Config withIncubationTimeMultiplier(float newIncubationTimeMultiplier) {
        return new Config(newIncubationTimeMultiplier, recoveryTimeMultiplier(), immunityTimeMultiplier(), radiusMultiplier(), vaccineMultiplier(), foodMultiplier(), minimumSpreadTime(), spreadTimeModifier());
    }

    public Config withRecoveryTimeMultiplier(float newRecoveryTimeMultiplier) {
        return new Config(incubationTimeMultiplier(), newRecoveryTimeMultiplier, immunityTimeMultiplier(), radiusMultiplier(), vaccineMultiplier(), foodMultiplier(), minimumSpreadTime(), spreadTimeModifier());
    }

    public Config withImmunityTimeMultiplier(float newImmunityTimeMultiplier) {
        return new Config(incubationTimeMultiplier(), recoveryTimeMultiplier(), newImmunityTimeMultiplier, radiusMultiplier(), vaccineMultiplier(), foodMultiplier(), minimumSpreadTime(), spreadTimeModifier());
    }

    public Config withRadiusMultiplier(float newRadiusMultiplier) {
        return new Config(incubationTimeMultiplier(), recoveryTimeMultiplier(), immunityTimeMultiplier(), newRadiusMultiplier, vaccineMultiplier(), foodMultiplier(), minimumSpreadTime(), spreadTimeModifier());
    }

    public Config withVaccineMultiplier(float newVaccineMultiplier) {
        return new Config(incubationTimeMultiplier(), recoveryTimeMultiplier(), immunityTimeMultiplier(), radiusMultiplier(), newVaccineMultiplier, foodMultiplier(), minimumSpreadTime(), spreadTimeModifier());
    }

    public Config withFoodMultiplier(float newFoodMultiplier) {
        return new Config(incubationTimeMultiplier(), recoveryTimeMultiplier(), immunityTimeMultiplier(), radiusMultiplier(), vaccineMultiplier(), newFoodMultiplier, minimumSpreadTime(), spreadTimeModifier());
    }

    public Config withMinimumSpreadTime(int newMinimumSpreadTime) {
        return new Config(incubationTimeMultiplier(), recoveryTimeMultiplier(), immunityTimeMultiplier(), radiusMultiplier(), vaccineMultiplier(), foodMultiplier(), newMinimumSpreadTime, spreadTimeModifier());
    }

    public Config withSpreadTimeModifier(float newSpreadTimeModifier) {
        return new Config(incubationTimeMultiplier(), recoveryTimeMultiplier(), immunityTimeMultiplier(), radiusMultiplier(), vaccineMultiplier(), foodMultiplier(), minimumSpreadTime(), newSpreadTimeModifier);
    }

    public static Config load() {
        return ConfigHelper.attemptLoad(FabricLoader.getInstance().getConfigDir().resolve("meowdemic.json"), CODEC).orElseGet(Config::defaultConfig);
    }

    public static void save(Config config) {
        ConfigHelper.save(FabricLoader.getInstance().getConfigDir().resolve("meowdemic.json"), config, CODEC);
    }

    public static Config defaultConfig() {
        return new Config(1.0f, 1.0f, 1.0f, 10.0f, 2.0f, 0.85f, 160, 1f);
    }
}
