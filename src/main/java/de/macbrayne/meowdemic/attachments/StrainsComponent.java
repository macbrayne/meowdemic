package de.macbrayne.meowdemic.attachments;

import com.mojang.serialization.Codec;
import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Strain;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import java.util.Map;

public class StrainsComponent {
    private static final Codec<Map<String, Strain>> MAP_CODEC = Codec.unboundedMap(Codec.STRING, Strain.CODEC);
    public static final AttachmentType<Map<String, Strain>> TYPE = AttachmentRegistry.create(Meowdemic.id("strains"), builder -> builder
            .initializer(Map::of)
            .persistent(StrainsComponent.MAP_CODEC));

    public static StrainsData get(AttachmentTarget target) {
        return new StrainsData(target);
    }

    public record StrainsData(AttachmentTarget target) {
        public Strain getStrain(String id) {
            return target.getAttachedOrCreate(TYPE).get(id);
        }

        public void addStrain(String id, Strain strain) {
            target.modifyAttached(TYPE, map -> {
                if(map == null) {
                    map = new java.util.HashMap<>();
                }
                map.put(id, strain);
                return map;
            });
        }

        public void removeStrain(String id) {
            target.modifyAttached(TYPE, map -> {
                if(map == null) {
                    return Map.of();
                }
                map.remove(id);
                return map;
            });
        }
    }
}
