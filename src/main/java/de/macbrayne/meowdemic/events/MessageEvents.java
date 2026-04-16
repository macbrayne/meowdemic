package de.macbrayne.meowdemic.events;


import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.data.Symptoms;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.node.TextNode;
import net.minecraft.world.entity.LivingEntity;

public class MessageEvents {
    public static TextNode register(TextNode textNode, PlaceholderContext placeholderContext) {
        if (placeholderContext.entity() instanceof LivingEntity entity) {
            if (TransmissionComponent.get(entity).hasSymptom(Symptoms.CHAT)) {
                return TextNode.asSingle(textNode, TextNode.of(getVariant(entity)));
            }
        }
        return textNode;
    }

    private static String getVariant(LivingEntity entity) {
        int random = entity.getRandom().nextInt(4);
        String append = switch (random) {
            case 1 -> "mew";
            case 2 -> "nya";
            case 3 -> "meowww";
            default -> "meow";
        };
        if (entity.getRandom().nextBoolean()) {
            append += " :3";
        }
        return append;
    }
}
