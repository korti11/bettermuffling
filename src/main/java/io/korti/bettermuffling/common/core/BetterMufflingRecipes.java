package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.recipe.AdvancedMufflingBlockRecipeSerializer;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipe;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BetterMufflingRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, BetterMuffling.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, MufflingBlockCloningRecipeSerializer> MUFFLING_BLOCK_CLONING_RECIPE =
            RECIPE_SERIALIZERS.register("muffling_block_cloning_recipe", MufflingBlockCloningRecipeSerializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, AdvancedMufflingBlockRecipeSerializer> MUFFLING_BLOCK_ADVANCED_RECIPE =
            RECIPE_SERIALIZERS.register("muffling_block_advanced_recipe", AdvancedMufflingBlockRecipeSerializer::new);
}
