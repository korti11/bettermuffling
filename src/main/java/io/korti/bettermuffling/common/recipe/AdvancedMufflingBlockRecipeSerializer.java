package io.korti.bettermuffling.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class AdvancedMufflingBlockRecipeSerializer implements RecipeSerializer<AdvancedMufflingBlockRecipe> {

    private static final MapCodec<AdvancedMufflingBlockRecipe> CODEC =
            new ShapedRecipe.Serializer().codec().xmap(AdvancedMufflingBlockRecipe::new, AdvancedMufflingBlockRecipe::inner);

    private static final StreamCodec<RegistryFriendlyByteBuf, AdvancedMufflingBlockRecipe> STREAM_CODEC =
            new ShapedRecipe.Serializer().streamCodec().map(AdvancedMufflingBlockRecipe::new, AdvancedMufflingBlockRecipe::inner);

    @Override
    public MapCodec<AdvancedMufflingBlockRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AdvancedMufflingBlockRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
