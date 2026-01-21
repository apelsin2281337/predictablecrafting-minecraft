package org.apelsin.predictablecrafting.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import org.apelsin.predictablecrafting.config.ModConfig;

public class ModMenuImplementation implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.create(ModConfig.HANDLER, (defaults, config, builder) -> builder
                .title(Text.literal("Predictable Crafting Mod Settings"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Predictable Crafting Mod Settings"))

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enable Mod"))
                                .description(OptionDescription.of(Text.literal("Enable or disable the mod completely")))
                                .binding(defaults.isModEnabled, () -> config.isModEnabled, val -> config.isModEnabled = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show Actual Amount of Items"))
                                .description(OptionDescription.of(Text.literal("Display the actual amount of items")))
                                .binding(defaults.isTrueAmountEnabled, () -> config.isTrueAmountEnabled, val -> config.isTrueAmountEnabled = val)
                                .controller(TickBoxControllerBuilder::create)
                                .available(config.isModEnabled)
                                .build())

                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show Amount of Stacks"))
                                .description(OptionDescription.of(Text.literal("Display the number of stacks")))
                                .binding(defaults.isAmountOfStacksShown, () -> config.isAmountOfStacksShown, val -> config.isAmountOfStacksShown = val)
                                .controller(TickBoxControllerBuilder::create)
                                .available(config.isModEnabled && config.isTrueAmountEnabled)
                                .build())

                        .option(Option.<Integer>createBuilder()
                                .name(Text.literal("Stacks Opacity"))
                                .description(OptionDescription.of(Text.literal("Set the opacity of stack display (0-255)")))
                                .binding(defaults.stacksOpacity, () -> config.stacksOpacity, val -> config.stacksOpacity = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 255)
                                        .step(1))
                                .available(config.isModEnabled)
                                .build())

                        .build())
        ).generateScreen(parentScreen);
    }
}