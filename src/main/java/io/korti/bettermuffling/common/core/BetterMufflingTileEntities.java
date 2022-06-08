package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

final public class BetterMufflingTileEntities {

    @ObjectHolder(registryName = "block_entity_type", value = BetterMuffling.MOD_ID + ":muffling_block")
    public static BlockEntityType<?> MUFFLING_BLOCK;

    @Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerTileEntityType(final RegisterEvent event) {
            event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
                helper.register(new ResourceLocation(BetterMuffling.MOD_ID, "muffling_block"),
                        BlockEntityType.Builder.of(MufflingBlockEntity::new, BetterMufflingBlocks.mufflingBlock, BetterMufflingBlocks.advancedMufflingBlock).build(null));
            });
        }
    }

}
