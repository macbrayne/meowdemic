package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.world.attachments.Attachments;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class PlayerStatsAttachment {
    public static PlayerStatsData get(LivingEntity target) {
        return new PlayerStatsData(target);
    }

    public static int calcPoints(int infected) {
        if (infected < 5) return 0;
        if (infected < 40) return Mth.log2(2 * infected / 5) / Mth.log2(2);
        return infected / 40 + 3;
    }

    public record PlayerStatsData(LivingEntity target) {
        public int getEntitiesInfected() {
            return target.getAttachedOrCreate(Attachments.PlayerStats.ENTITIES_INFECTED);
        }

        public void addEntitiesInfected(int amount) {
            target.getAttachedOrCreate(Attachments.PlayerStats.ENTITIES_INFECTED);
            target.modifyAttached(Attachments.PlayerStats.ENTITIES_INFECTED, integer -> integer + amount);
        }

        public int getTimesCured() {
            return target.getAttachedOrCreate(Attachments.PlayerStats.TIMES_CURED);
        }

        public void increaseTimesCured() {
            target.getAttachedOrCreate(Attachments.PlayerStats.TIMES_CURED);
            target.modifyAttached(Attachments.PlayerStats.TIMES_CURED, integer -> integer + 1);
        }

        public int getPoints() {
            return calcPoints(getEntitiesInfected()) - target.getAttachedOrCreate(Attachments.PlayerStats.POINTS_DELTA);
        }

        public void removePoints(int amount) {
            target.getAttachedOrCreate(Attachments.PlayerStats.POINTS_DELTA);
            target.modifyAttached(Attachments.PlayerStats.POINTS_DELTA, integer -> integer + amount);
        }
    }
}
