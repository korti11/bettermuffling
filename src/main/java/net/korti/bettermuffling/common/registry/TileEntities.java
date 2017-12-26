package net.korti.bettermuffling.common.registry;

import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntities {

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileMuffling.class, "tile_muffling");
    }

}
