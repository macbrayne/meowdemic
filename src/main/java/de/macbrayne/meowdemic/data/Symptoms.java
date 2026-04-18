package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public enum Symptoms implements StringRepresentable {
    MEOW_AND_PURR("meow_and_purr"),
    CAT_EARS("cat_ears"),
    FOOD("food"),
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

    public static List<Symptoms> all() {
        return List.of(values());
    }
}
