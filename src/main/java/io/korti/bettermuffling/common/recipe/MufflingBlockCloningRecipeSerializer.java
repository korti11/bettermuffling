package io.korti.bettermuffling.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class MufflingBlockCloningRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MufflingBlockCloningRecipe> {

    private final BiFunction<ResourceLocation, Item, MufflingBlockCloningRecipe> constructor;

    public MufflingBlockCloningRecipeSerializer(BiFunction<ResourceLocation, Item, MufflingBlockCloningRecipe> constructor) {
        this.constructor = constructor;
    }

    @Override
    public MufflingBlockCloningRecipe read(ResourceLocation recipeId, JsonObject json) {
        String itemId = json.getAsJsonPrimitive("ingredient").getAsString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        return constructor.apply(recipeId, item);
    }

    @Nullable
    @Override
    public MufflingBlockCloningRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        ResourceLocation itemId = buffer.readResourceLocation();
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        return constructor.apply(recipeId, item);
    }

    @Override
    public void write(PacketBuffer buffer, MufflingBlockCloningRecipe recipe) {
        buffer.writeResourceLocation(recipe.getItemId());
    }
}
