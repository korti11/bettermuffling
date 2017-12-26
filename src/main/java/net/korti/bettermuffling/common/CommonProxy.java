package net.korti.bettermuffling.common;

import net.korti.bettermuffling.common.registry.TileEntities;

public class CommonProxy {

    public void preInit() {
        TileEntities.registerTileEntities();
    }

}
