package org.apelsin.predictablecrafting;

import net.fabricmc.api.ModInitializer;
import org.apelsin.predictablecrafting.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredictableCrafting implements ModInitializer {
    public static final String MOD_ID = "predictablecrafting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        ModConfig.HANDLER.load();

    }
}
