package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.block.AdvancedMufflingBlock;
import io.korti.bettermuffling.common.block.MufflingBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

final public class BetterMufflingBlocks {

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block")
    public static MufflingBlock mufflingBlock;

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block")
    public static Item mufflingBlockItem;

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block_advanced")
    public static AdvancedMufflingBlock advancedMufflingBlock;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerBlock(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> blockRegistry = event.getRegistry();

            blockRegistry.register(new MufflingBlock().setRegistryName(BetterMuffling.MOD_ID, "muffling_block"));
            blockRegistry.register(new AdvancedMufflingBlock().setRegistryName(BetterMuffling.MOD_ID, "muffling_block_advanced"));
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> itemRegistry = event.getRegistry();
            final Item.Properties itemBuilder = new Item.Properties().group(ItemGroup.MISC);

            itemRegistry.register(new BlockItem(mufflingBlock, itemBuilder)
                    .setRegistryName(mufflingBlock.getRegistryName()));
            itemRegistry.register(new BlockItem(advancedMufflingBlock, itemBuilder)
                    .setRegistryName(advancedMufflingBlock.getRegistryName()));
        }
    }

}
