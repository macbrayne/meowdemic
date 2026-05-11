package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.macbrayne.meowdemic.datagen.MeowdemicEntityTypeTagProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public record Strain(String name, HashSet<Symptoms> symptoms, HashSet<EntityType<?>> targets, double incubationFactor, double transmissionFactor,
                     double recoveryFactor, double immunityFactor) {
    public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Strain::name),
            Symptoms.CODEC.listOf().fieldOf("symptoms").xmap(HashSet::new, ArrayList::new).forGetter(Strain::symptoms),
            EntityType.CODEC.listOf().fieldOf("targets").xmap(HashSet::new, ArrayList::new).forGetter(Strain::targets),
            Codec.DOUBLE.fieldOf("incubationFactor").forGetter(Strain::incubationFactor),
            Codec.DOUBLE.fieldOf("transmissionFactor").forGetter(Strain::transmissionFactor),
            Codec.DOUBLE.fieldOf("recoveryFactor").forGetter(Strain::recoveryFactor),
            Codec.DOUBLE.fieldOf("immunityFactor").forGetter(Strain::immunityFactor)
    ).apply(instance, Strain::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Strain::name,
            Symptoms.STREAM_CODEC.apply(ByteBufCodecs.list()).map(HashSet::new, ArrayList::new), Strain::symptoms,
            EntityType.STREAM_CODEC.apply(ByteBufCodecs.list()).map(HashSet::new, ArrayList::new), Strain::targets,
            ByteBufCodecs.DOUBLE, Strain::incubationFactor, ByteBufCodecs.DOUBLE, Strain::transmissionFactor,
            ByteBufCodecs.DOUBLE, Strain::recoveryFactor, ByteBufCodecs.DOUBLE, Strain::immunityFactor, Strain::new);

    public Strain(HashSet<Symptoms> symptoms, HashSet<EntityType<?>> targets, double incubationFactor, double transmissionFactor, double recoveryFactor, double immunityFactor) {
        this(generateName(), symptoms, targets, incubationFactor, transmissionFactor, recoveryFactor, immunityFactor);
    }

    public static Strain random(RandomSource randomSource) {
        return new Strain(generateName(), new HashSet<>(List.of(Symptoms.MEOW_AND_PURR)), defaultEntitySet(), randomSource.nextGaussian() * 0.25 + 1,
                randomSource.nextGaussian() * 0.25 + 0.5, randomSource.nextGaussian() * 0.25 + 1,
                randomSource.nextGaussian() * 0.25 + 1);
    }

    public static HashSet<EntityType<?>> defaultEntitySet() {
        return BuiltInRegistries.ENTITY_TYPE.get(MeowdemicEntityTypeTagProvider.DISEASE_SPREADS_TO)
                    .stream().flatMap(holders -> holders.stream().map(Holder::value)).collect(Collectors.toCollection(HashSet::new));
    }

    public Strain addSymptom(Symptoms newSymptom) {
        if (symptoms().contains(newSymptom)) {
            return this;
        }
        HashSet<Symptoms> newSymptoms = new HashSet<>(symptoms());
        newSymptoms.add(newSymptom);
        return new Strain(modifyName(name()), newSymptoms, targets(), incubationFactor(), transmissionFactor(), recoveryFactor(), immunityFactor());
    }

    public Strain addTarget(EntityType<?> newTarget) {
        if (targets().contains(newTarget)) {
            return this;
        }
        HashSet<EntityType<?>> newTargets = new HashSet<>(targets());
        newTargets.add(newTarget);
        return new Strain(modifyName(name()), symptoms(), newTargets, incubationFactor(), transmissionFactor(), recoveryFactor(), immunityFactor());
    }

    public Strain withIncubationFactor(double newIncubationFactor) {
        return new Strain(modifyName(name()), symptoms(), targets(), newIncubationFactor, transmissionFactor(), recoveryFactor(), immunityFactor());
    }

    public Strain withTransmissionFactor(double newTransmissionFactor) {
        return new Strain(modifyName(name()), symptoms(), targets(), incubationFactor(), newTransmissionFactor, recoveryFactor(), immunityFactor());
    }

    public Strain withRecoveryFactor(double newRecoveryFactor) {
        return new Strain(modifyName(name()), symptoms(), targets(), incubationFactor(), transmissionFactor(), newRecoveryFactor, immunityFactor());
    }

    public Strain withImmunityFactor(double newImmunityFactor) {
        return new Strain(modifyName(name()), symptoms(), targets(), incubationFactor(), transmissionFactor(), recoveryFactor(), newImmunityFactor);
    }

    private static String modifyName(String previousName) {
        RandomSource random = RandomSource.create();
        String[] parts = splitName(previousName);
        String[] newParts = generateNameParts();
        float rand = random.nextFloat();
        if (rand < 0.1f) {
            parts[0] = newParts[0];
        }
        if (rand < 0.3f) {
            parts[1] = newParts[1];
        }
        if (rand < 0.8f) {
            parts[2] = newParts[2];
        }
        return parts[0] + "." + parts[1] + " " + parts[2];
    }

    public static String generateName() {
        String[] parts = generateNameParts();
        return parts[0] + "." + parts[1] + " " + parts[2];
    }

    private static String[] generateNameParts() {
        RandomSource random = RandomSource.create();
        float rand = random.nextFloat();
        String[] parts = new String[3];
        char rndCar = (char) ('α' + random.nextInt(24));
        parts[0] = rndCar + "";
        int rndNum = random.nextInt(99) + 1;
        parts[1] = rndNum + "";
        String rndEntity = BuiltInRegistries.ENTITY_TYPE.getRandom(random).get().value().getDescription().getString();
        parts[2] = rndEntity;
        return parts;
    }

    private static String[] splitName(String name) {
        String[] part1and2 = name.split("\\.");
        String[] part2and3 = part1and2[1].split(" ", 2);
        return new String[]{part1and2[0], part2and3[0], part2and3[1]};
    }
}
