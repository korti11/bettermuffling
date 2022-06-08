package io.korti.bettermuffling.common.recipe;

import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class MufflingBlockCloningRecipe extends CustomRecipe {

    private final Item item;

    public MufflingBlockCloningRecipe(ResourceLocation idIn, Item item) {
        super(idIn);
        this.item = item;
    }

    public ResourceLocation getItemId() {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    @Override
    public boolean matches(CraftingContainer inv, @Nonnull Level worldIn) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getContainerSize(); j++) {
            ItemStack itemStack1 = inv.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.hasTag()) {
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
    public ItemStack assemble(CraftingContainer inv) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getContainerSize(); j++) {
            ItemStack itemStack1 = inv.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.hasTag()) {
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
        return BetterMufflingRecipes.mufflingBlockCloningRecipeSerializer;
    }
}
