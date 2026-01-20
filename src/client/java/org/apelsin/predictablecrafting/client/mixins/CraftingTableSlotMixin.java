package org.apelsin.predictablecrafting.client.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import org.apelsin.predictablecrafting.mixins.CraftingResultSlotAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Environment(EnvType.CLIENT)
@Mixin(Slot.class)
public class CraftingTableSlotMixin {

    @Inject(
            method = "getStack",
            at = @At("RETURN"),
            cancellable = true
    )
    private void ShowMaximumCraftableAmount(CallbackInfoReturnable<ItemStack> cir){

        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        if (!minecraftClient.isOnThread()) return;
        if (!((Object)this instanceof CraftingResultSlot)) {
            return;
        }

        CraftingResultSlot craftingSlot = (CraftingResultSlot)(Object)this;

        try {
            if (!minecraftClient.isShiftPressed()) {
                return;
            }
        }
        catch (Exception e) {
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

        ItemStack modifiedResult = initialResult.copy();
        modifiedResult.setCount((initialResult.getCount() * minAmountOfItems));

        cir.setReturnValue(modifiedResult);
    }
}