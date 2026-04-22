package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.HashSet;

public record Strain(String name, HashSet<Symptoms> symptoms, double incubationFactor, double transmissionFactor, double recoveryFactor,
                     double immunityFactor) {
    public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Strain::name),
            Symptoms.CODEC.listOf().fieldOf("symptoms").xmap(HashSet::new, ArrayList::new).forGetter(strain -> strain.symptoms()),
            Codec.DOUBLE.fieldOf("incubationFactor").forGetter(Strain::incubationFactor),
            Codec.DOUBLE.fieldOf("transmissionFactor").forGetter(Strain::transmissionFactor),
            Codec.DOUBLE.fieldOf("recoveryFactor").forGetter(Strain::recoveryFactor),
            Codec.DOUBLE.fieldOf("immunityFactor").forGetter(Strain::immunityFactor)
    ).apply(instance, Strain::new));
    public static final StreamCodec<FriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Strain::name, Symptoms.STREAM_CODEC.apply(ByteBufCodecs.list()).map(HashSet::new, ArrayList::new), Strain::symptoms, ByteBufCodecs.DOUBLE, Strain::incubationFactor, ByteBufCodecs.DOUBLE, Strain::transmissionFactor, ByteBufCodecs.DOUBLE, Strain::recoveryFactor, ByteBufCodecs.DOUBLE, Strain::immunityFactor, Strain::new);


    public Strain addSymptom(Symptoms newSymptom) {
        if (symptoms().contains(newSymptom)) {
            return this;
        }
        HashSet<Symptoms> newSymptoms = new HashSet<>(symptoms());
        newSymptoms.add(newSymptom);
        return new Strain(generateName(name()), newSymptoms, incubationFactor(), transmissionFactor(), recoveryFactor(), immunityFactor());
    }

    public Strain withIncubationFactor(double newIncubationFactor) {
        return new Strain(generateName(name()), symptoms(), newIncubationFactor, transmissionFactor(), recoveryFactor(), immunityFactor());
    }

    public Strain withTransmissionFactor(double newTransmissionFactor) {
        return new Strain(generateName(name()), symptoms(), incubationFactor(), newTransmissionFactor, recoveryFactor(), immunityFactor());
    }

    public Strain withRecoveryFactor(double newRecoveryFactor) {
        return new Strain(generateName(name()), symptoms(), incubationFactor(), transmissionFactor(), newRecoveryFactor, immunityFactor());
    }

    public Strain withImmunityFactor(double newImmunityFactor) {
        return new Strain(generateName(name()), symptoms(), incubationFactor(), transmissionFactor(), recoveryFactor(), newImmunityFactor);
    }

    private static String generateName(String previousName) {
        RandomSource random = RandomSource.create();
        String[] parts = splitName(previousName);
        float rand = random.nextFloat();
        if (rand < 0.1f) {
            char rndCar = (char) ('α' + random.nextInt(24));
            parts[0] = rndCar + "";
        }
        if (rand < 0.3f) {
            int rndNum = random.nextInt(99) + 1;
            parts[1] = rndNum + "";
        }
        if (rand < 0.8f) {
            String rndEntity = BuiltInRegistries.ENTITY_TYPE.getRandom(random).get().value().getDescription().getString();
            System.out.println(rndEntity);
            parts[2] = rndEntity;
        }
        return parts[0] + "." + parts[1] + " " + parts[2];
    }

    private static String[] splitName(String name) {
        String[] part1and2 = name.split("\\.");
        String[] part2and3 = part1and2[1].split(" ");
        return new String[]{part1and2[0], part2and3[0], part2and3[1]};
    }
}
