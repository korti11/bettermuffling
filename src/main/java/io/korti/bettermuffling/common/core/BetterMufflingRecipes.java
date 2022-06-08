package io.korti.bettermuffling.common.core;


import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.recipe.AdvancedMufflingBlockRecipeSerializer;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipe;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

final public class BetterMufflingRecipes {

    @ObjectHolder(registryName = "recipe_serializer", value = BetterMuffling.MOD_ID + ":muffling_block_cloning_recipe")
    public static MufflingBlockCloningRecipeSerializer mufflingBlockCloningRecipeSerializer;

    @ObjectHolder(registryName = "recipe_serializer", value = BetterMuffling.MOD_ID + ":muffling_block_advanced_recipe")
    public static AdvancedMufflingBlockRecipeSerializer advancedMufflingBlockRecipeSerializer;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerRecipeSerializer(final RegisterEvent event) {
            event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block_cloning_recipe"), new MufflingBlockCloningRecipeSerializer(MufflingBlockCloningRecipe::new));
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block_advanced_recipe"), new AdvancedMufflingBlockRecipeSerializer());
            });
        }

    }

}
