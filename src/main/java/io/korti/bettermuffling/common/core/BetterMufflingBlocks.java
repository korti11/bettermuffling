package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.block.AdvancedMufflingBlock;
import io.korti.bettermuffling.common.block.MufflingBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

final public class BetterMufflingBlocks {

    @ObjectHolder(registryName = "block", value = BetterMuffling.MOD_ID + ":muffling_block")
    public static MufflingBlock mufflingBlock;

    @ObjectHolder(registryName = "item", value = BetterMuffling.MOD_ID + ":muffling_block")
    public static Item mufflingBlockItem;

    @ObjectHolder(registryName = "block", value = BetterMuffling.MOD_ID + ":muffling_block_advanced")
    public static AdvancedMufflingBlock advancedMufflingBlock;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerBlock(final RegisterEvent event) {
            event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block"), new MufflingBlock());
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block_advanced"), new AdvancedMufflingBlock());
            });

            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                final Item.Properties itemBuilder = new Item.Properties().tab(CreativeModeTab.TAB_MISC);

                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block"), new BlockItem(mufflingBlock, itemBuilder));
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block_advanced"), new BlockItem(advancedMufflingBlock, itemBuilder));
            });
        }
    }

}
