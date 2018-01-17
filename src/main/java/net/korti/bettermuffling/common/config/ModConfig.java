package net.korti.bettermuffling.common.config;

import net.korti.bettermuffling.common.constant.ModInfo;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModInfo.MOD_ID)
@Config.LangKey("gui.config.title")
public class ModConfig {

    @Config.Comment("Maximum range for the sound muffling effect.")
    @Config.RangeInt(min = 2, max = 64)
    @Config.Name("Max Range")
    @Config.RequiresWorldRestart
    public static int maxRange = 16;

    @Config.Comment("Minimum volume for the sound muffling effect.")
    @Config.RangeDouble(min = 0, max = 0.99)
    @Config.Name("Min Volume")
    @Config.RequiresWorldRestart
    public static double minVolume = 0;

    @Config.Comment("Maximum volume for the sound muffling effect.")
    @Config.RangeDouble(min = 0.01, max = 1)
    @Config.Name("Max Volume")
    @Config.RequiresWorldRestart
    public static double maxVolume = 1;

    @Config.Name("Muffler Indicator")
    public static final MufflerIndicator mufflerIndicator = new MufflerIndicator();

    @Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ModInfo.MOD_ID)) {
                ConfigManager.sync(ModInfo.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }

    public static class MufflerIndicator {
        @Config.Comment("Show muffler indicator.")
        @Config.Name("Enable")
        public boolean enable = true;

        @Config.Comment("The x coordinate of the indicator position.")
        @Config.Name("Position X")
        public int x = 25;

        @Config.Comment("The y coordinate of the indicator position.")
        @Config.Name("Position Y")
        public int y = 25;
    }
}
