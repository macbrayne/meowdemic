package de.macbrayne.meowdemic.client.gui;

import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.Symptoms;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.function.UnaryOperator;

public enum Upgrades {
    TRANSMISSION_RATE(strain -> strain.withTransmissionFactor(strain.transmissionFactor() * 1.1)),
    RECOVERY_RATE(strain -> strain.withRecoveryFactor(strain.recoveryFactor() * 0.9)),
    IMMUNITY_RATE(strain -> strain.withImmunityFactor(strain.immunityFactor() * 0.9)),
    INCUBATION_RATE(strain -> strain.withIncubationFactor(strain.incubationFactor() * 0.9)),
    NEW_SYMPTOM_CHAT(strain -> strain.addSymptom(Symptoms.CHAT)),
    NEW_SYMPTOM_FOOD(strain -> strain.addSymptom(Symptoms.FOOD)),
    NEW_SYMPTOM_CAT_EARS(strain -> strain.addSymptom(Symptoms.CAT_EARS));

    private final UnaryOperator<Strain> effect;

     Upgrades(UnaryOperator<Strain> effect) {
         this.effect = effect;
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
}
