package io.korti.bettermuffling.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MufflingBlockCloningRecipeSerializer implements RecipeSerializer<MufflingBlockCloningRecipe> {

    private static final MapCodec<MufflingBlockCloningRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC)
                            .forGetter(MufflingBlockCloningRecipe::category),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient")
                            .forGetter(MufflingBlockCloningRecipe::getItem)
            ).apply(instance, MufflingBlockCloningRecipe::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, MufflingBlockCloningRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    CraftingBookCategory.STREAM_CODEC, MufflingBlockCloningRecipe::category,
                    ByteBufCodecs.registry(Registries.ITEM), MufflingBlockCloningRecipe::getItem,
                    MufflingBlockCloningRecipe::new
            );

    @Override
    public MapCodec<MufflingBlockCloningRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MufflingBlockCloningRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
