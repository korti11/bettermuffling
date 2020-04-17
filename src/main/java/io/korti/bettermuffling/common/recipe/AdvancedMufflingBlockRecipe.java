package io.korti.bettermuffling.common.recipe;

import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;

public class AdvancedMufflingBlockRecipe extends ShapedRecipe {

    public AdvancedMufflingBlockRecipe(ShapedRecipe r) {
        super(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getRecipeOutput());
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack itemStack = super.getCraftingResult(inv);
        ItemStack muffleBlock = inv.getStackInSlot(4);
        if (!itemStack.isEmpty() && muffleBlock.getItem() == BetterMufflingBlocks.mufflingBlockItem) {
            if(!muffleBlock.hasTag()) {
                return itemStack;
            }

            itemStack.setTag(muffleBlock.getTag());
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BetterMufflingRecipes.advancedMufflingBlockRecipeSerializer;
    }
}
