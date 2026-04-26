package de.macbrayne.meowdemic.network;

import de.macbrayne.meowdemic.Meowdemic;
import de.macbrayne.meowdemic.data.Upgrades;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ClientboundGachaResponsePacket(Upgrades upgrade) implements CustomPacketPayload {
    public static final Identifier GACHA_RESPONSE_PAYLOAD_ID = Meowdemic.id( "gacha_response");
    public static final CustomPacketPayload.Type<ClientboundGachaResponsePacket> TYPE = new CustomPacketPayload.Type<>(GACHA_RESPONSE_PAYLOAD_ID);
    public static final StreamCodec<ByteBuf, ClientboundGachaResponsePacket> CODEC = Upgrades.STREAM_CODEC.map(ClientboundGachaResponsePacket::new, ClientboundGachaResponsePacket::upgrade);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
