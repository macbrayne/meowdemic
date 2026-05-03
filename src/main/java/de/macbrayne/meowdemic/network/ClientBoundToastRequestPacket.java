package de.macbrayne.meowdemic.network;

import de.macbrayne.meowdemic.Meowdemic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ClientBoundToastRequestPacket() implements CustomPacketPayload {
    public static final Identifier TOAST_REQUEST_PAYLOAD_ID = Meowdemic.id( "toast_request");
    public static final CustomPacketPayload.Type<ClientBoundToastRequestPacket> TYPE = new CustomPacketPayload.Type<>(TOAST_REQUEST_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundToastRequestPacket> CODEC = StreamCodec.unit(new ClientBoundToastRequestPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
