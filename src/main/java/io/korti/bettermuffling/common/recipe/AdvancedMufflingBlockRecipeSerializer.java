package io.korti.bettermuffling.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class AdvancedMufflingBlockRecipeSerializer extends ShapedRecipe.Serializer {

    @Override
    public ShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
        ShapedRecipe recipe = super.read(recipeId, json);
        return new AdvancedMufflingBlockRecipe(recipe);
    }

    @Override
    public ShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        ShapedRecipe recipe = super.read(recipeId, buffer);
        if (recipe == null) {
            return null;
        }
        return new AdvancedMufflingBlockRecipe(recipe);
    }
}
