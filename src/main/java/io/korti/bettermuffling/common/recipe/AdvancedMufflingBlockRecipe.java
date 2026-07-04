package io.korti.bettermuffling.common.recipe;

import com.mojang.serialization.MapCodec;
import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class AdvancedMufflingBlockRecipe implements CraftingRecipe {

    public static final MapCodec<AdvancedMufflingBlockRecipe> CODEC =
            ShapedRecipe.MAP_CODEC.xmap(AdvancedMufflingBlockRecipe::new, AdvancedMufflingBlockRecipe::inner);

    public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedMufflingBlockRecipe> STREAM_CODEC =
            ShapedRecipe.STREAM_CODEC.map(AdvancedMufflingBlockRecipe::new, AdvancedMufflingBlockRecipe::inner);

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
    public ItemStack assemble(CraftingInput inv) {
        ItemStack itemStack = inner.assemble(inv);
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
    public String group() {
        return inner.group();
    }

    @Override
    public CraftingBookCategory category() {
        return inner.category();
    }

    @Override
    @Nonnull
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return BetterMufflingRecipes.MUFFLING_BLOCK_ADVANCED_RECIPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return inner.placementInfo();
    }

    @Override
    public boolean showNotification() {
        return inner.showNotification();
    }
}
