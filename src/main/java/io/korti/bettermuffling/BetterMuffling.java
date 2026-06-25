package io.korti.bettermuffling;

import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.core.BetterMufflingItems;
import io.korti.bettermuffling.common.core.BetterMufflingRecipes;
import io.korti.bettermuffling.common.core.BetterMufflingTileEntities;
import io.korti.bettermuffling.common.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterMuffling.MOD_ID)
public class BetterMuffling {

    public static final String MOD_ID = "bettermuffling";
    public static final Logger LOG = LogManager.getLogger();

    public BetterMuffling(IEventBus modEventBus, ModContainer modContainer) {
        BetterMufflingConfig.register(modContainer);
        BetterMufflingBlocks.BLOCKS.register(modEventBus);
        BetterMufflingBlocks.ITEMS.register(modEventBus);
        BetterMufflingItems.ITEMS.register(modEventBus);
        BetterMufflingTileEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        BetterMufflingRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(PacketHandler::register);
    }
}
