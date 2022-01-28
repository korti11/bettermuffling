package io.korti.bettermuffling.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class AdvancedMufflingBlockRecipeSerializer extends ShapedRecipe.Serializer {

    @Override
    public ShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ShapedRecipe recipe = super.fromJson(recipeId, json);
        return new AdvancedMufflingBlockRecipe(recipe);
    }

    @Override
    public ShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ShapedRecipe recipe = super.fromNetwork(recipeId, buffer);
        if (recipe == null) {
            return null;
        }
        return new AdvancedMufflingBlockRecipe(recipe);
    }
}
