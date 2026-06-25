package io.korti.bettermuffling.common.core;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BetterMufflingTileEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, BetterMuffling.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MufflingBlockEntity>> MUFFLING_BLOCK =
            BLOCK_ENTITY_TYPES.register("muffling_block", () ->
                    BlockEntityType.Builder.of(MufflingBlockEntity::new,
                            BetterMufflingBlocks.MUFFLING_BLOCK.get(),
                            BetterMufflingBlocks.MUFFLING_BLOCK_ADVANCED.get())
                    .build(null));
}
