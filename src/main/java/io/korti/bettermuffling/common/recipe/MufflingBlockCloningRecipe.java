package io.korti.bettermuffling.common.recipe;

import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.core.component.DataComponents;

import javax.annotation.Nonnull;

public class MufflingBlockCloningRecipe extends CustomRecipe {

    private final Item item;

    public MufflingBlockCloningRecipe(CraftingBookCategory category, Item item) {
        super(category);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public ResourceLocation getItemId() {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    @Override
    public boolean matches(CraftingInput inv, @Nonnull Level worldIn) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.size(); j++) {
            ItemStack itemStack1 = inv.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.has(DataComponents.CUSTOM_DATA)) {
                    if (!itemStack.isEmpty()) {
                        return false;
                    }
                    itemStack = itemStack1;
                } else {
                    if (itemStack1.getItem() != item) {
                        return false;
                    }
                    i++;
                }
            }
        }

        return !itemStack.isEmpty() && i > 0;
    }

    @Override
    @Nonnull
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.size(); j++) {
            ItemStack itemStack1 = inv.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.has(DataComponents.CUSTOM_DATA)) {
                    if (!itemStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    itemStack = itemStack1;
                } else {
                    if (itemStack1.getItem() != item) {
                        return ItemStack.EMPTY;
                    }
                    i++;
                }
            }
        }

        if (!itemStack.isEmpty() && i >= 1) {
            ItemStack itemStack1 = itemStack.copy();
            itemStack1.setCount(i + 1);
            return itemStack1;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return BetterMufflingRecipes.MUFFLING_BLOCK_CLONING_RECIPE.get();
    }
}
