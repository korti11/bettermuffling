package net.korti.bettermuffling;

import net.korti.bettermuffling.client.ClientProxy;
import net.korti.bettermuffling.common.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BetterMuffling.MOD_ID)
public class BetterMuffling {

    public static final String MOD_ID = "bettermuffling";

    public static BetterMuffling instance;
    public static ServerProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public BetterMuffling() {
        BetterMuffling.instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
    }

    private void preInit(final FMLCommonSetupEvent event) {
        proxy.preInit();
    }
}
