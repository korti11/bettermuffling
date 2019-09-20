package net.korti.bettermuffling;

import net.korti.bettermuffling.client.ClientProxy;
import net.korti.bettermuffling.common.ServerProxy;
import net.korti.bettermuffling.common.config.BetterMufflingConfig;
import net.korti.bettermuffling.common.network.PacketHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
