package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.block.AdvancedMufflingBlock;
import io.korti.bettermuffling.common.block.MufflingBlock;
import io.korti.bettermuffling.common.item.MufflingBlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.SoundType;
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
            BLOCKS.registerBlock("muffling_block", MufflingBlock::new, props -> props.sound(SoundType.WOOL).noLootTable());
    public static final DeferredBlock<AdvancedMufflingBlock> MUFFLING_BLOCK_ADVANCED =
            BLOCKS.registerBlock("muffling_block_advanced", AdvancedMufflingBlock::new, props -> props.sound(SoundType.WOOL).noLootTable());

    public static final DeferredItem<MufflingBlockItem> MUFFLING_BLOCK_ITEM =
            ITEMS.registerItem("muffling_block", props -> new MufflingBlockItem(MUFFLING_BLOCK.get(), props));
    public static final DeferredItem<MufflingBlockItem> MUFFLING_BLOCK_ADVANCED_ITEM =
            ITEMS.registerItem("muffling_block_advanced", props -> new MufflingBlockItem(MUFFLING_BLOCK_ADVANCED.get(), props));

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MUFFLING_BLOCK_ITEM);
            event.accept(MUFFLING_BLOCK_ADVANCED_ITEM);
        }
    }
}
