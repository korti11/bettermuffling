package net.korti.bettermuffling.common.core;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.block.MufflingBlock;
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

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerBlock(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> blockRegistry = event.getRegistry();

            blockRegistry.register(new MufflingBlock());
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> itemRegistry = event.getRegistry();
            final Item.Properties itemBuilder = new Item.Properties().group(ItemGroup.MISC);

            itemRegistry.register(new BlockItem(mufflingBlock, itemBuilder)
                    .setRegistryName(mufflingBlock.getRegistryName()));
        }
    }

}
