package de.macbrayne.meowdemic.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import de.macbrayne.meowdemic.Meowdemic;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ConfigHelper {
    private static final Logger LOGGER = Meowdemic.LOGGER;

    public static <T> Optional<T> attemptLoad(Path path, Codec<T> codec) {
        Gson gson = new GsonBuilder().create();
        if (Files.exists(path)) {
            try {
                var gsonReader = Files.newBufferedReader(path);
                JsonElement element = gson.fromJson(gsonReader, JsonElement.class);
                var result = codec.decode(JsonOps.INSTANCE, element).resultOrPartial(LOGGER::error);
                if (result.isPresent()) {
                    return Optional.of(result.get().getFirst());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    public static <T> void save(Path path, T object, Codec<T> codec) {
        LOGGER.info("Writing to file {}", path.toAbsolutePath());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (var gsonWriter = Files.newBufferedWriter(path)) {
            var result = codec.encodeStart(JsonOps.INSTANCE, object).resultOrPartial(LOGGER::error);
            gson.toJson(result.orElseThrow(), gsonWriter);
        } catch (IOException e) {
            LOGGER.error("Failed to write to file {}", path.toAbsolutePath(), e);
        }
    }

    public static void ensureConfigExists() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(Meowdemic.MOD_ID + ".json");
        if (!Files.exists(configPath)) {
            save(configPath, Config.defaultConfig(), Config.CODEC);
        }
    }
}