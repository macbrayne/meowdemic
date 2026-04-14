package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record Strain(String name, List<Symptoms> symptoms, double transmissionFactor, double recoveryFactor, double immunityFactor) {
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

    String generateName(String previousName) {
        return previousName;
    }
}
