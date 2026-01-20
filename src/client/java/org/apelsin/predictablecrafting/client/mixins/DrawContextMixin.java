package org.apelsin.predictablecrafting.client.mixins;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "drawStackCount", at = @At("HEAD"), cancellable = true)
    private void customStackCount(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String stackCountText, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (stack.getCount() != 1 || stackCountText != null) {
            String string = stackCountText == null ? String.valueOf(stack.getCount()) : stackCountText;
            DrawContext context = (DrawContext)(Object)this;

            context.drawText(textRenderer, string, x + 19 - 2 - textRenderer.getWidth(string), y + 6 + 3, -1, true);


            if (minecraftClient.isShiftPressed() && stack.getCount() > 64) {
                Matrix3x2fStack matrices = context.getMatrices();
                matrices.pushMatrix();

                float scale = 0.8f;
                matrices.scale(scale, scale);
                String stacksString = String.valueOf(stack.getCount() / 64);

                int scaledX = (int)((x + 16) / scale) - textRenderer.getWidth(stacksString);
                int scaledY = (int)((y + 1) / scale);

                context.drawText(textRenderer, stacksString, scaledX, scaledY, 0xD0FFFFFF, true);

                matrices.popMatrix();
            }

        }

        ci.cancel();
    }
}
