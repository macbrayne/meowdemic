package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

import java.util.List;

public record Strain(String name, List<Symptoms> symptoms, double transmissionFactor, double recoveryFactor,
                     double immunityFactor) {
    public static final Codec<Strain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Strain::name),
            Symptoms.CODEC.listOf().fieldOf("symptoms").forGetter(Strain::symptoms),
            Codec.DOUBLE.fieldOf("transmissionFactor").forGetter(Strain::transmissionFactor),
            Codec.DOUBLE.fieldOf("recoveryFactor").forGetter(Strain::recoveryFactor),
            Codec.DOUBLE.fieldOf("immunityFactor").forGetter(Strain::immunityFactor)
    ).apply(instance, Strain::new));
    public static final StreamCodec<FriendlyByteBuf, Strain> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Strain::name, Symptoms.STREAM_CODEC.apply(ByteBufCodecs.list()), Strain::symptoms, ByteBufCodecs.DOUBLE, Strain::transmissionFactor, ByteBufCodecs.DOUBLE, Strain::recoveryFactor, ByteBufCodecs.DOUBLE, Strain::immunityFactor, Strain::new);


    public Strain mutate() {
        return new Strain(generateName(name()), symptoms(), transmissionFactor(), recoveryFactor(), immunityFactor());
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
