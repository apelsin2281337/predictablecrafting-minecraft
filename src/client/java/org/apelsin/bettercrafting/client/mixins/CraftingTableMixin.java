package org.apelsin.bettercrafting.client.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import org.apelsin.bettercrafting.mixins.CraftingResultSlotAccessor;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Slot.class)
public class CraftingTableMixin {
    @Final
    @Shadow
    public Inventory inventory;

    @Shadow
    public int getIndex() {
        return 0;
    }

    @Inject(
            method = "getStack",
            at = @At("RETURN"),
            cancellable = true
    )
    private void ShowMaximumCraftableAmount(CallbackInfoReturnable<ItemStack> cir){
        if (!MinecraftClient.getInstance().isOnThread()) return;
        if (!((Object)this instanceof CraftingResultSlot)) {
            return;
        }

        CraftingResultSlot craftingSlot = (CraftingResultSlot)(Object)this;

        try {
            long windowHandle = net.minecraft.client.MinecraftClient.getInstance().getWindow().getHandle();
            boolean shiftPressed = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS ||
                    GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;

            if (!shiftPressed) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        ItemStack initialResult = cir.getReturnValue();

        if (initialResult.isEmpty()) {
            return;
        }

        RecipeInputInventory input = ((CraftingResultSlotAccessor)craftingSlot).getInput();

        int minAmountOfItems = Integer.MAX_VALUE;
        for (ItemStack stack : input) {
            if (!stack.isEmpty()) {
                minAmountOfItems = Math.min(stack.getCount(), minAmountOfItems);
            }
        }


        if (minAmountOfItems == Integer.MAX_VALUE || minAmountOfItems <= 1) {
            return;
        }

        long totalCraftableLong = (long) initialResult.getCount() * minAmountOfItems;
        int totalCraftable = totalCraftableLong > Integer.MAX_VALUE ?
                Integer.MAX_VALUE : (int) totalCraftableLong;

        ItemStack modifiedResult = initialResult.copy();
        modifiedResult.setCount((initialResult.getCount() * minAmountOfItems));

        cir.setReturnValue(modifiedResult);
    }
}