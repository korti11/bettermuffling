package io.korti.bettermuffling.common.recipe;

import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nonnull;

public class AdvancedMufflingBlockRecipe extends ShapedRecipe {

    public AdvancedMufflingBlockRecipe(ShapedRecipe r) {
        super(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingContainer inv) {
        ItemStack itemStack = super.assemble(inv);
        ItemStack muffleBlock = inv.getItem(4);
        if (!itemStack.isEmpty() && muffleBlock.getItem() == BetterMufflingBlocks.mufflingBlockItem) {
            if (!muffleBlock.hasTag()) {
                return itemStack;
            }

            itemStack.setTag(muffleBlock.getTag());
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return BetterMufflingRecipes.advancedMufflingBlockRecipeSerializer;
    }
}
