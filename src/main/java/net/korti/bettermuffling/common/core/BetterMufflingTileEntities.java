package net.korti.bettermuffling.common.core;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

final public class BetterMufflingTileEntities {

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block")
    public static TileEntityType<?> MUFFLING_BLOCK;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event) {
            final IForgeRegistry<TileEntityType<?>> tileEntityTypeRegistry = event.getRegistry();

            tileEntityTypeRegistry.register(
                    TileEntityType.Builder
                            .create((Supplier<TileEntity>)TileMuffling::new, BetterMufflingBlocks.mufflingBlock)
                            .build(null).setRegistryName(BetterMuffling.MOD_ID + ":muffling_block")
            );
        }
    }

}
