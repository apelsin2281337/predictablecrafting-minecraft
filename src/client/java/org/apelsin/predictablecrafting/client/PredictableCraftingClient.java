package org.apelsin.predictablecrafting.client;

import net.fabricmc.api.ClientModInitializer;
import org.apelsin.predictablecrafting.PredictableCrafting;


public class PredictableCraftingClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        PredictableCrafting.LOGGER.info("PredictableCrafting initialized!");
    }
}
