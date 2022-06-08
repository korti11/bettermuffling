package io.korti.bettermuffling.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class MufflingBlockCloningRecipeSerializer implements RecipeSerializer<MufflingBlockCloningRecipe> {

    private final BiFunction<ResourceLocation, Item, MufflingBlockCloningRecipe> constructor;

    public MufflingBlockCloningRecipeSerializer(BiFunction<ResourceLocation, Item, MufflingBlockCloningRecipe> constructor) {
        this.constructor = constructor;
    }

    @Override
    @Nonnull
    public MufflingBlockCloningRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {
        String itemId = json.getAsJsonPrimitive("ingredient").getAsString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        return constructor.apply(recipeId, item);
    }

    @Nullable
    @Override
    public MufflingBlockCloningRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ResourceLocation itemId = buffer.readResourceLocation();
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        return constructor.apply(recipeId, item);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, MufflingBlockCloningRecipe recipe) {
        buffer.writeResourceLocation(recipe.getItemId());
    }
}
