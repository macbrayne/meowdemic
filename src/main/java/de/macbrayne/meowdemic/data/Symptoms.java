package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public enum Symptoms implements StringRepresentable {
    MEOW_AND_PURR("meow_and_purr"),
    CAT_EARS("cat_ears"),
    FOOD("food"),
    CHAT("chat");

    private final String id;
    public static final Codec<Symptoms> CODEC = StringRepresentable.fromEnum(Symptoms::values);
    public static final StreamCodec<ByteBuf, Symptoms> STREAM_CODEC = CodecUtils.ofEnum(Symptoms.class);
    public static final List<String> IDS = Stream.of(values()).map(Symptoms::getSerializedName).toList();

    Symptoms(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    public static HashSet<Symptoms> all() {
        return new HashSet<>(List.of(values()));
    }
}
