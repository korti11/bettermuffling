package io.korti.bettermuffling.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class MufflingBlockCloningRecipe extends CustomRecipe {

    public static final MapCodec<MufflingBlockCloningRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient")
                            .forGetter(MufflingBlockCloningRecipe::getItem)
            ).apply(instance, MufflingBlockCloningRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MufflingBlockCloningRecipe> STREAM_CODEC =
            ByteBufCodecs.registry(Registries.ITEM).map(MufflingBlockCloningRecipe::new, MufflingBlockCloningRecipe::getItem);

    private final Item item;

    public MufflingBlockCloningRecipe(Item item) {
        super();
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Identifier getItemId() {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    @Override
    public boolean matches(CraftingInput inv, @Nonnull Level worldIn) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.size(); j++) {
            ItemStack itemStack1 = inv.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.has(DataComponents.CUSTOM_DATA)) {
                    if (!itemStack.isEmpty()) {
                        return false;
                    }
                    itemStack = itemStack1;
                } else {
                    if (itemStack1.getItem() != item) {
                        return false;
                    }
                    i++;
                }
            }
        }

        return !itemStack.isEmpty() && itemStack.getItem() == item && i > 0;
    }

    @Override
    @Nonnull
    public ItemStack assemble(CraftingInput inv) {
        int i = 0;
        ItemStack itemStack = ItemStack.EMPTY;

        for (int j = 0; j < inv.size(); j++) {
            ItemStack itemStack1 = inv.getItem(j);
            if (!itemStack1.isEmpty()) {
                if (itemStack1.has(DataComponents.CUSTOM_DATA)) {
                    if (!itemStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    itemStack = itemStack1;
                } else {
                    if (itemStack1.getItem() != item) {
                        return ItemStack.EMPTY;
                    }
                    i++;
                }
            }
        }

        if (!itemStack.isEmpty() && itemStack.getItem() == item && i >= 1) {
            ItemStack itemStack1 = itemStack.copy();
            itemStack1.setCount(i + 1);
            return itemStack1;
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return BetterMufflingRecipes.MUFFLING_BLOCK_CLONING_RECIPE.get();
    }
}
