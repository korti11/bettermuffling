package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.item.UpgradeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public final class BetterMufflingItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BetterMuffling.MOD_ID);

    public static final DeferredItem<UpgradeItem> UPGRADE = ITEMS.registerItem("upgrade", UpgradeItem::new);

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(UPGRADE);
        }
    }
}
