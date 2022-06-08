package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.item.UpgradeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class BetterMufflingItems {

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerItems(final RegisterEvent event) {
            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "upgrade"), new UpgradeItem());
            });
        }
    }

}
