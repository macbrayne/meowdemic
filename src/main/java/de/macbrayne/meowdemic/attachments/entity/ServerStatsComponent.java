package de.macbrayne.meowdemic.attachments.entity;

import de.macbrayne.meowdemic.attachments.Attachments;
import net.minecraft.server.level.ServerLevel;

public class ServerStatsComponent {
    static boolean confirmReset;
    public static ServerStatsData get(ServerLevel target) {
        return new ServerStatsData(target);
    }

    public record ServerStatsData(ServerLevel target) {
        public int getCurrentlyInfected() {
            return target.getAttachedOrCreate(Attachments.ServerStats.CURRENTLY_INFECTED);
        }

        public void removeCurrentlyInfected() {
            target.getAttachedOrCreate(Attachments.ServerStats.CURRENTLY_INFECTED);
            target.modifyAttached(Attachments.ServerStats.CURRENTLY_INFECTED, integer -> integer - 1);
        }

        public void addCurrentlyInfected(int amount) {
            target.getAttachedOrCreate(Attachments.ServerStats.CURRENTLY_INFECTED);
            target.modifyAttached(Attachments.ServerStats.CURRENTLY_INFECTED, integer -> integer + amount);
            addTotalInfected(amount);
        }

        public int getTotalInfected() {
            return target.getAttachedOrCreate(Attachments.ServerStats.TOTAL_INFECTED);
        }

        private void addTotalInfected(int amount) {
            target.getAttachedOrCreate(Attachments.ServerStats.TOTAL_INFECTED);
            target.modifyAttached(Attachments.ServerStats.TOTAL_INFECTED, integer -> integer + amount);
        }

        public int getSpeciesBarriersCrossed() {
            return target.getAttachedOrCreate(Attachments.ServerStats.SPECIES_BARRIERS_CROSSED);
        }

        public void addSpeciesBarriersCrossed(int amount) {
            target.getAttachedOrCreate(Attachments.ServerStats.SPECIES_BARRIERS_CROSSED);
            target.modifyAttached(Attachments.ServerStats.SPECIES_BARRIERS_CROSSED, integer -> integer + amount);
        }

        public int getStrainsCreated() {
            return target.getAttachedOrCreate(Attachments.ServerStats.STRAINS_MUTATED);
        }

        public void addStrainsCreated() {
            target.getAttachedOrCreate(Attachments.ServerStats.STRAINS_MUTATED);
            target.modifyAttached(Attachments.ServerStats.STRAINS_MUTATED, integer -> integer + 1);
        }

        public boolean reset() {
            if(!confirmReset) {
                confirmReset = true;
                return false;
            }
            target.removeAttached(Attachments.ServerStats.CURRENTLY_INFECTED);
            target.removeAttached(Attachments.ServerStats.TOTAL_INFECTED);
            target.removeAttached(Attachments.ServerStats.SPECIES_BARRIERS_CROSSED);
            target.removeAttached(Attachments.ServerStats.STRAINS_MUTATED);
            confirmReset = false;
            return true;
        }
    }
}
