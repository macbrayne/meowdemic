package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.attachments.Attachments;
import de.macbrayne.meowdemic.util.LevelCalc;
import net.minecraft.world.entity.LivingEntity;

public class PlayerStatsComponent {
    public static PlayerStatsData get(LivingEntity target) {
        return new PlayerStatsData(target);
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
            return LevelCalc.calcPoints(getEntitiesInfected()) - target.getAttachedOrCreate(Attachments.PlayerStats.POINTS_DELTA);
        }

        public void removePoints(int amount) {
            target.getAttachedOrCreate(Attachments.PlayerStats.POINTS_DELTA);
            target.modifyAttached(Attachments.PlayerStats.POINTS_DELTA, integer -> integer + amount);
        }
    }
}
