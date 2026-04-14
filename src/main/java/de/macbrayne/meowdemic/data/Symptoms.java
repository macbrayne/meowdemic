package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum Symptoms implements StringRepresentable {
    MEOWING("meowing"),
    CAT_EARS("cat_ears"),
    WHISKERS("whiskers"),
    PURRING("purring"),
    CHAT("chat");

    private final String id;
    public static final Codec<Symptoms> CODEC = StringRepresentable.fromEnum(Symptoms::values);
    public static final StreamCodec<ByteBuf, Symptoms> STREAM_CODEC = ofEnum(Symptoms.class);

    Symptoms(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    static <T extends Enum<T>> StreamCodec<ByteBuf, T> ofEnum(Class<? extends T> clazz) {
        T[] values = clazz.getEnumConstants();
        return new StreamCodec<>() {
            @Override
            public T decode(ByteBuf buf) {
                return values[VarInt.read(buf)];
            }

            @Override
            public void encode(ByteBuf buf, T value) {
                VarInt.write(buf, value.ordinal());
            }
        };
    }
}
