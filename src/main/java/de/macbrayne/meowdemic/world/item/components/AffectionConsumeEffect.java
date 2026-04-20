package de.macbrayne.meowdemic.world.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.macbrayne.meowdemic.data.Strain;
import de.macbrayne.meowdemic.data.TransmissionEvent;
import de.macbrayne.meowdemic.world.attachments.entity.ImmunityAttachment;
import de.macbrayne.meowdemic.world.attachments.entity.TransmissionAttachment;
import de.macbrayne.meowdemic.world.item.MeowdemicItems;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public record AffectionConsumeEffect(Optional<UUID> source, Strain strain, boolean vaccinate, float modifier) implements ConsumeEffect {
    public static final MapCodec<AffectionConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            UUIDUtil.CODEC.optionalFieldOf("source").forGetter(AffectionConsumeEffect::source),
            Strain.CODEC.fieldOf("strain").forGetter(AffectionConsumeEffect::strain),
            Codec.BOOL.fieldOf("vaccinate").forGetter(AffectionConsumeEffect::vaccinate),
            Codec.FLOAT.fieldOf("modifier").forGetter(AffectionConsumeEffect::modifier)
    ).apply(instance, AffectionConsumeEffect::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AffectionConsumeEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), AffectionConsumeEffect::source,
            Strain.STREAM_CODEC, AffectionConsumeEffect::strain,
            ByteBufCodecs.BOOL, AffectionConsumeEffect::vaccinate,
            ByteBufCodecs.FLOAT, AffectionConsumeEffect::modifier, AffectionConsumeEffect::new);

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return MeowdemicItems.AFFECT;
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        final LivingEntity target = user;
        if(vaccinate) {
            return ImmunityAttachment.get(user).setIfNone(strain(), 2f);
        }
        return TransmissionAttachment.get(user).tryInfecting(new TransmissionEvent(source, target.getUUID(), strain()));
    }

    public static AffectionConsumeEffect vaccinate(Strain strain) {
        return new AffectionConsumeEffect(Optional.empty(), strain, true, 2f);
    }

    public static AffectionConsumeEffect infect(Optional<UUID> source, Strain strain) {
        return new AffectionConsumeEffect(source, strain, false, 0.85f);
    }
}
