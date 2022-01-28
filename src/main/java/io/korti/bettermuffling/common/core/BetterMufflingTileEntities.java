package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

final public class BetterMufflingTileEntities {

    @ObjectHolder(BetterMuffling.MOD_ID + ":muffling_block")
    public static BlockEntityType<?> MUFFLING_BLOCK;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerTileEntityType(final RegistryEvent.Register<BlockEntityType<?>> event) {
            final IForgeRegistry<BlockEntityType<?>> tileEntityTypeRegistry = event.getRegistry();

            tileEntityTypeRegistry.register(
                    BlockEntityType.Builder
                            .of(MufflingBlockEntity::new, BetterMufflingBlocks.mufflingBlock,
                                    BetterMufflingBlocks.advancedMufflingBlock)
                            .build(null).setRegistryName(BetterMuffling.MOD_ID + ":muffling_block")
            );
        }
    }

}
