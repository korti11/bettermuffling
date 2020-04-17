package io.korti.bettermuffling.common.core;


import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.recipe.AdvancedMufflingBlockRecipeSerializer;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipe;
import io.korti.bettermuffling.common.recipe.MufflingBlockCloningRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

final public class BetterMufflingRecipes {

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block_cloning_recipe")
    public static MufflingBlockCloningRecipeSerializer mufflingBlockCloningRecipeSerializer;

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block_advanced_recipe")
    public static AdvancedMufflingBlockRecipeSerializer advancedMufflingBlockRecipeSerializer;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerRecipeSerializer(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            final IForgeRegistry<IRecipeSerializer<?>> recipeRegistry = event.getRegistry();

            recipeRegistry.register(new MufflingBlockCloningRecipeSerializer(MufflingBlockCloningRecipe::new)
                    .setRegistryName(BetterMuffling.MOD_ID, "muffling_block_cloning_recipe"));
            recipeRegistry.register(new AdvancedMufflingBlockRecipeSerializer()
                    .setRegistryName(BetterMuffling.MOD_ID, "muffling_block_advanced_recipe"));
        }

    }

}
