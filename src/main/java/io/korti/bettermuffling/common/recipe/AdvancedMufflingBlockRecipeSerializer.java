package io.korti.bettermuffling.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nonnull;

public class AdvancedMufflingBlockRecipeSerializer extends ShapedRecipe.Serializer {

    @Override
    @Nonnull
    public ShapedRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
        ShapedRecipe recipe = super.fromJson(recipeId, json);
        return new AdvancedMufflingBlockRecipe(recipe);
    }

    @Override
    public ShapedRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
        ShapedRecipe recipe = super.fromNetwork(recipeId, buffer);
        if (recipe == null) {
            return null;
        }
        return new AdvancedMufflingBlockRecipe(recipe);
    }
}
