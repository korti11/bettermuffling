package io.korti.bettermuffling.common.recipe;

import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MufflingBlockCloningRecipe extends SpecialRecipe {

    private final Item item;

    public MufflingBlockCloningRecipe(ResourceLocation idIn, Item item) {
        super(idIn);
        this.item = item;
    }

    public ResourceLocation getItemId() {
        return item.getRegistryName();
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); j++) {
            ItemStack itemStack1 = inv.getStackInSlot(j);
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
    public ItemStack getCraftingResult(CraftingInventory inv) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); j++) {
            ItemStack itemStack1 = inv.getStackInSlot(j);
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
    public boolean canFit(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return BetterMufflingRecipes.mufflingBlockCloningRecipeSerializer;
    }
}
