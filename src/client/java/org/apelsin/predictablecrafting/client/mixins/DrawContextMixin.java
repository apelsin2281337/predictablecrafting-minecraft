package org.apelsin.predictablecrafting.client.mixins;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.apelsin.predictablecrafting.config.ModConfig;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Unique
    private ModConfig modConfig = ModConfig.get();

    @Inject(method = "drawStackCount", at = @At("TAIL"), cancellable = true)
    private void customStackCount(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String stackCountText, CallbackInfo ci) {
        if (!modConfig.isModEnabled) return;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (stack.getCount() != 1 || stackCountText != null) {
            DrawContext context = (DrawContext)(Object)this;

            if (minecraftClient.isShiftPressed() && stack.getCount() > 64 && modConfig.isAmountOfStacksShown) {
                Matrix3x2fStack matrices = context.getMatrices();
                matrices.pushMatrix();

                float scale = 0.8f;
                matrices.scale(scale, scale);
                String stacksString = String.valueOf(stack.getCount() / 64);

                int scaledX = (int)((x + 16) / scale) - textRenderer.getWidth(stacksString);
                int scaledY = (int)((y + 1) / scale);

                int colorCode = (modConfig.stacksOpacity << 24) | (0x00FFFFFF);

                context.drawText(textRenderer, stacksString, scaledX, scaledY, colorCode, true);

                matrices.popMatrix();
            }

        }

        ci.cancel();
    }
}
