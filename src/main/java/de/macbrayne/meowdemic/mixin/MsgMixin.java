package de.macbrayne.meowdemic.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.IncubationAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Optional;

@Mixin(MsgCommand.class)
public class MsgMixin {
    @Inject(method = "sendMessage(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/Collection;Lnet/minecraft/network/chat/PlayerChatMessage;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;sendChatMessage(Lnet/minecraft/network/chat/OutgoingChatMessage;ZLnet/minecraft/network/chat/ChatType$Bound;)V"))
    private static void spreadMsg(CommandSourceStack source, Collection<ServerPlayer> players, PlayerChatMessage message, CallbackInfo ci, @Local(name = "player") ServerPlayer player) {
        Optional<TransmissionEvent> event = TransmissionAttachment.get(source.getPlayer()).getOptional();
        if(source.getPlayer() != null && player != null && event.isPresent()) {
            if (player != source.getPlayer()) {
                IncubationAttachment.get(target).tryIncubate(event.get());
            }
        }
    }
}
