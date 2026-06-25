package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.block.AdvancedMufflingBlock;
import io.korti.bettermuffling.common.block.MufflingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public final class BetterMufflingBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BetterMuffling.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BetterMuffling.MOD_ID);

    public static final DeferredBlock<MufflingBlock> MUFFLING_BLOCK =
            BLOCKS.register("muffling_block", MufflingBlock::new);
    public static final DeferredBlock<AdvancedMufflingBlock> MUFFLING_BLOCK_ADVANCED =
            BLOCKS.register("muffling_block_advanced", AdvancedMufflingBlock::new);

    public static final DeferredItem<BlockItem> MUFFLING_BLOCK_ITEM =
            ITEMS.register("muffling_block", () -> new BlockItem(MUFFLING_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MUFFLING_BLOCK_ADVANCED_ITEM =
            ITEMS.register("muffling_block_advanced", () -> new BlockItem(MUFFLING_BLOCK_ADVANCED.get(), new Item.Properties()));

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MUFFLING_BLOCK_ITEM);
            event.accept(MUFFLING_BLOCK_ADVANCED_ITEM);
        }
    }
}
