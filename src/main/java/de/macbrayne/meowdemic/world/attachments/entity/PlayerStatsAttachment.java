package de.macbrayne.meowdemic.world.attachments.entity;

import de.macbrayne.meowdemic.data.PullHistoryEvent;
import de.macbrayne.meowdemic.network.ClientBoundToastRequestPacket;
import de.macbrayne.meowdemic.world.attachments.Attachments;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayDeque;

public class PlayerStatsAttachment {
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
            if ((getPoints() % 160 == 0 || amount > 5) && target instanceof ServerPlayer serverPlayer) {
                ClientBoundToastRequestPacket payload = new ClientBoundToastRequestPacket();
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }

        public int getTimesCured() {
            return target.getAttachedOrCreate(Attachments.PlayerStats.TIMES_CURED);
        }

        public void increaseTimesCured() {
            target.getAttachedOrCreate(Attachments.PlayerStats.TIMES_CURED);
            target.modifyAttached(Attachments.PlayerStats.TIMES_CURED, integer -> integer + 1);
        }

        public int getPoints() {
            return (getEntitiesInfected() - target.getAttachedOrCreate(Attachments.PlayerStats.POINTS_DELTA) * 5) * 32;
        }

        public int numberOfPullsAffordable() {
            return getPoints() / 160;
        }

        public void removePoints(int amount) {
            target.getAttachedOrCreate(Attachments.PlayerStats.POINTS_DELTA);
            target.modifyAttached(Attachments.PlayerStats.POINTS_DELTA, integer -> integer + amount);
        }

        public int getGachaPity() {
            return target.getAttachedOrCreate(Attachments.PlayerStats.GACHA_PITY);
        }

        public void increaseGachaPity() {
            target.getAttachedOrCreate(Attachments.PlayerStats.GACHA_PITY);
            target.modifyAttached(Attachments.PlayerStats.GACHA_PITY, integer -> integer + 1);
        }

        public void resetGachaPity() {
            target.setAttached(Attachments.PlayerStats.GACHA_PITY, 0);
        }

        public ArrayDeque<PullHistoryEvent> getPullHistory() {
            return target.getAttachedOrCreate(Attachments.PlayerStats.PULL_HISTORY);
        }

        public void addPullHistoryEvent(PullHistoryEvent event) {
            target.getAttachedOrCreate(Attachments.PlayerStats.PULL_HISTORY);
            target.modifyAttached(Attachments.PlayerStats.PULL_HISTORY, list -> {
                ArrayDeque<PullHistoryEvent> result = new ArrayDeque<>(list);
                if(result.size() >= 10) result.removeFirst();
                result.add(event);
                return result;
            });
        }
    }
}
