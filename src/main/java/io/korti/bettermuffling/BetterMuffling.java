package io.korti.bettermuffling;

import io.korti.bettermuffling.client.ClientProxy;
import io.korti.bettermuffling.common.ServerProxy;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.network.PacketHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterMuffling.MOD_ID)
public class BetterMuffling {

    public static final String MOD_ID = "bettermuffling";
    public static final Logger LOG = LogManager.getLogger();

    public static BetterMuffling instance;
    public static ServerProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public BetterMuffling() {
        BetterMuffling.instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        BetterMufflingConfig.register();
    }

    private void preInit(final FMLCommonSetupEvent event) {
        proxy.preInit();
        PacketHandler.register();
    }
}
