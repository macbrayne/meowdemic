package de.macbrayne.meowdemic.util;


import net.minecraft.util.Mth;

public class LevelCalc {
    public static int calcPoints(int infected) {
        if (infected < 5) return 0;
        if (infected < 40) return Mth.log2(2 * infected / 5) / Mth.log2(2);
        return infected / 40 + 3;
    }
}
