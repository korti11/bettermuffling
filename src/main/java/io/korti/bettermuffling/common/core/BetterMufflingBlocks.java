package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.block.AdvancedMufflingBlock;
import io.korti.bettermuffling.common.block.MufflingBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
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

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block_advanced")
    public static Item advancedMufflingBlockItem;

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
            final Item.Properties itemBuilder = new Item.Properties().tab(CreativeModeTab.TAB_MISC);

            itemRegistry.register(new BlockItem(mufflingBlock, itemBuilder)
                    .setRegistryName(mufflingBlock.getRegistryName()));
            itemRegistry.register(new BlockItem(advancedMufflingBlock, itemBuilder)
                    .setRegistryName(advancedMufflingBlock.getRegistryName()));
        }
    }

}
