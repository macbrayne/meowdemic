package de.macbrayne.meowdemic.events;


import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.node.TextNode;
import net.minecraft.world.entity.LivingEntity;

public class MessageEvents {
    public static TextNode register(TextNode textNode, PlaceholderContext placeholderContext) {
        if (placeholderContext.entity() instanceof LivingEntity entity) {
            if (TransmissionAttachment.get(entity).hasSymptom(Symptoms.CHAT) && entity.getRandom().nextFloat() < TransmissionAttachment.get(entity).getOptional().get().strain().transmissionFactor()) {
                return TextNode.asSingle(textNode, TextNode.of(getVariant(entity)));
            }
        }
        return textNode;
    }

    private static String getVariant(LivingEntity entity) {
        int random = entity.getRandom().nextInt(5);
        String append = switch (random) {
            case 1 -> " mew";
            case 2 -> " nya";
            case 3 -> " meowww";
            case 4 -> " meoww!";
            case 5 -> " nyaaa!";
            default -> " meow";
        };
        if (entity.getRandom().nextBoolean()) {
            append += " :3";
        }
        return append;
    }
}
