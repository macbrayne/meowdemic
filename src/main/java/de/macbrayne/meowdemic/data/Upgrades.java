package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public enum Upgrades implements StringRepresentable {
    TRANSMISSION_RATE("transmission_rate", Rarity.COMMON, strain -> strain.withTransmissionFactor(strain.transmissionFactor() * 1.1)),
    RECOVERY_RATE("recovery_rate", Rarity.COMMON, strain -> strain.withRecoveryFactor(strain.recoveryFactor() * 0.9)),
    IMMUNITY_RATE("immunity_rate", Rarity.COMMON, strain -> strain.withImmunityFactor(strain.immunityFactor() * 0.9)),
    INCUBATION_RATE("incubation_rate", Rarity.COMMON, strain -> strain.withIncubationFactor(strain.incubationFactor() * 0.9)),
    NEW_SYMPTOM_CHAT("symptom_chat", Rarity.UNCOMMON, strain -> strain.addSymptom(Symptoms.CHAT)),
    NEW_SYMPTOM_FOOD("symptom_food", Rarity.UNCOMMON, strain -> strain.addSymptom(Symptoms.FOOD)),
    NEW_SYMPTOM_CAT_EARS("symptom_cat_ears", Rarity.UNCOMMON, strain -> strain.addSymptom(Symptoms.CAT_EARS));

    private final UnaryOperator<Strain> effect;
    private final String id;
    private final Rarity rarity;
    public static final Codec<Upgrades> CODEC = StringRepresentable.fromEnum(Upgrades::values);
    public static final StreamCodec<ByteBuf, Upgrades> STREAM_CODEC = CodecUtils.ofEnum(Upgrades.class);

    public static final List<Upgrades> COMMON_POOL = Arrays.stream(values()).filter(upgrade -> upgrade.rarity == Rarity.COMMON).collect(Collectors.toList());;
    public static final List<Upgrades> UNCOMMON_POOL = Arrays.stream(values()).filter(upgrade -> upgrade.rarity == Rarity.UNCOMMON).collect(Collectors.toList());;

    Upgrades(String id, Rarity rarity, UnaryOperator<Strain> effect) {
        this.effect = effect;
        this.id = id;
        this.rarity = rarity;
    }

    public static Upgrades getRandom(RandomSource random, int pity) {
        float rand = random.nextFloat();
        float commonChance = pity >= 10 ? Rarity.COMMON.pityChance : Rarity.COMMON.chance;
        if (rand < commonChance) {
            return COMMON_POOL.get(random.nextInt(COMMON_POOL.size()));
        } else {
            return UNCOMMON_POOL.get(random.nextInt(UNCOMMON_POOL.size()));
        }
    }

    public UnaryOperator<Strain> apply() {
        return effect;
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public enum Rarity {
        COMMON(0.95f, 0.6f), UNCOMMON(0.05f, 0.4f);
        final public float chance;
        final public float pityChance;

        Rarity(float chance, float pityChance) {
            this.chance = chance;
            this.pityChance = pityChance;
        }

        public Component getComponent() {
            return Component.translatable("gui.meowdemic.gacha_history.rarity." + this.name().toLowerCase());
        }
    }
}
