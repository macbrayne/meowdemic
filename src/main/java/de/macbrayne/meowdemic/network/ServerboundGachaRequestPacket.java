package de.macbrayne.meowdemic.network;

import de.macbrayne.meowdemic.Meowdemic;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ServerboundGachaRequestPacket() implements CustomPacketPayload {
    public static final Identifier GACHA_REQUEST_PAYLOAD_ID = Meowdemic.id( "gacha_request");
    public static final CustomPacketPayload.Type<ServerboundGachaRequestPacket> TYPE = new CustomPacketPayload.Type<>(GACHA_REQUEST_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundGachaRequestPacket> CODEC = StreamCodec.unit(new ServerboundGachaRequestPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
