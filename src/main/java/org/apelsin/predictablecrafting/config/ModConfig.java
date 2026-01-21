package org.apelsin.predictablecrafting.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apelsin.predictablecrafting.PredictableCrafting;

@Environment(EnvType.CLIENT)
public class ModConfig {
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.of(PredictableCrafting.MOD_ID, "predictablecrafting"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("predictablecrafting.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public boolean isModEnabled = true;

    @SerialEntry
    public boolean isTrueAmountEnabled = true;

    @SerialEntry
    public boolean isAmountOfStacksShown = true;

    @SerialEntry
    public int stacksOpacity = 208;

    public static ModConfig get() {
        return HANDLER.instance();
    }

}
