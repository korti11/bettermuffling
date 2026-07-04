package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.recipe.AdvancedMufflingBlockRecipe;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BetterMufflingRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, BetterMuffling.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MufflingBlockCloningRecipe>> MUFFLING_BLOCK_CLONING_RECIPE =
            RECIPE_SERIALIZERS.register("muffling_block_cloning_recipe",
                    () -> new RecipeSerializer<>(MufflingBlockCloningRecipe.CODEC, MufflingBlockCloningRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AdvancedMufflingBlockRecipe>> MUFFLING_BLOCK_ADVANCED_RECIPE =
            RECIPE_SERIALIZERS.register("muffling_block_advanced_recipe",
                    () -> new RecipeSerializer<>(AdvancedMufflingBlockRecipe.CODEC, AdvancedMufflingBlockRecipe.STREAM_CODEC));
}
