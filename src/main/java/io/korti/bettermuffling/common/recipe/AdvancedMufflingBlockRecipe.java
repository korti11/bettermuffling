package io.korti.bettermuffling.common.recipe;

import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class AdvancedMufflingBlockRecipe implements CraftingRecipe {

    private final ShapedRecipe inner;

    public AdvancedMufflingBlockRecipe(ShapedRecipe inner) {
        this.inner = inner;
    }

    public ShapedRecipe inner() {
        return inner;
    }

    @Override
    public boolean matches(CraftingInput inv, @Nonnull Level level) {
        return inner.matches(inv, level);
    }

    @Override
    @Nonnull
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack itemStack = inner.assemble(inv, registries);
        ItemStack muffleBlock = inv.getItem(4);
        if (!itemStack.isEmpty() && muffleBlock.getItem() == BetterMufflingBlocks.MUFFLING_BLOCK_ITEM.get()) {
            var sourceData = muffleBlock.get(DataComponents.CUSTOM_DATA);
            if (sourceData == null) {
                return itemStack;
            }
            itemStack.set(DataComponents.CUSTOM_DATA, sourceData);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return inner.canCraftInDimensions(width, height);
    }

    @Override
    @Nonnull
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return inner.getResultItem(registries);
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return BetterMufflingRecipes.MUFFLING_BLOCK_ADVANCED_RECIPE.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return inner.getType();
    }

    @Override
    public CraftingBookCategory category() {
        return inner.category();
    }

    @Override
    public boolean showNotification() {
        return inner.showNotification();
    }
}
