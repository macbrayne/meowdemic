package de.macbrayne.meowdemic.events;


import de.macbrayne.meowdemic.attachments.entity.TransmissionComponent;
import de.macbrayne.meowdemic.data.Symptoms;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.node.TextNode;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class MessageEvents {
    public static TextNode register(TextNode textNode, PlaceholderContext placeholderContext) {
        if(placeholderContext.entity() instanceof LivingEntity entity) {
            if(TransmissionComponent.get(entity).hasSymptom(Symptoms.CHAT)) {
                return TextNode.asSingle(textNode, TextNode.of(" meow :3"));
            }
        }
        return textNode;
    }
}
