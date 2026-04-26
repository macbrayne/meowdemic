package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.function.UnaryOperator;

public enum Upgrades implements StringRepresentable {
    TRANSMISSION_RATE(strain -> strain.withTransmissionFactor(strain.transmissionFactor() * 1.1), "transmission_rate"),
    RECOVERY_RATE(strain -> strain.withRecoveryFactor(strain.recoveryFactor() * 0.9), "recovery_rate"),
    IMMUNITY_RATE(strain -> strain.withImmunityFactor(strain.immunityFactor() * 0.9), "immunity_rate"),
    INCUBATION_RATE(strain -> strain.withIncubationFactor(strain.incubationFactor() * 0.9), "incubation_rate"),
    NEW_SYMPTOM_CHAT(strain -> strain.addSymptom(Symptoms.CHAT), "symptom_chat"),
    NEW_SYMPTOM_FOOD(strain -> strain.addSymptom(Symptoms.FOOD), "symptom_food"),
    NEW_SYMPTOM_CAT_EARS(strain -> strain.addSymptom(Symptoms.CAT_EARS), "symptom_cat_ears");

    private final UnaryOperator<Strain> effect;
    private final String id;
    public static final Codec<Upgrades> CODEC = StringRepresentable.fromEnum(Upgrades::values);
    public static final StreamCodec<ByteBuf, Upgrades> STREAM_CODEC = CodecUtils.ofEnum(Upgrades.class);

     Upgrades(UnaryOperator<Strain> effect, String id) {
         this.effect = effect;
         this.id = id;
     }

     public static Upgrades getRandom(RandomSource random) {
         int rnd = random.nextInt(100);

         if(rnd < 10) {
             return List.of(NEW_SYMPTOM_CAT_EARS, NEW_SYMPTOM_FOOD, NEW_SYMPTOM_CHAT).get(random.nextInt(3));
         }
         return List.of(TRANSMISSION_RATE, IMMUNITY_RATE, RECOVERY_RATE, INCUBATION_RATE).get(random.nextInt(4));
     }

     public UnaryOperator<Strain> apply() {
         return effect;
     }

    @Override
    public String getSerializedName() {
        return id;
    }
}
