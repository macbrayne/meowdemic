package de.macbrayne.meowdemic.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum Symptoms implements StringRepresentable {
    MEOWING("meowing"),
    CAT_EARS("cat_ears"),
    WHISKERS("whiskers"),
    PURRING("purring"),
    CHAT("chat");

    private final String id;
    public static final Codec<Symptoms> CODEC = StringRepresentable.fromEnum(Symptoms::values);

    Symptoms(String id) {
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return id;
    }
}
