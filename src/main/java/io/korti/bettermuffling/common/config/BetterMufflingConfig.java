package io.korti.bettermuffling.common.config;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static net.minecraftforge.common.ForgeConfigSpec.*;

@Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterMufflingConfig {

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientSpecs);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpecs);
    }

    public static class Common {
        public final IntValue maxRange;

        public final DoubleValue minVolume;
        public final DoubleValue maxVolume;

        public final IntValue ticksIndicatorHandler;

        Common(Builder builder) {
            builder.comment("Common configuration settings").push("common");

            maxRange = builder
                    .comment("Maximum range for the sound muffling effect.")
                    .translation("config.muffling_block.range")
                    .worldRestart()
                    .defineInRange("maxRange", 16, 2, 64);

            minVolume = builder
                    .comment("Minimum volume for the sound muffling effect.",
                            "Has to be smaller then max volume.")
                    .translation("config.muffling_block.min_volume")
                    .worldRestart()
                    .defineInRange("minVolume", 0.0D, 0.0D, 0.99D);

            maxVolume = builder
                    .comment("Maximum volume for the sound muffling effect.",
                            "Has to be greater then min volume.")
                    .translation("config.muffling_block.min_volume")
                    .worldRestart()
                    .defineInRange("maxVolume", 1.0D, 0.01D, 1.0D);

            ticksIndicatorHandler = builder
                    .comment("After this amount of ticks the muffling block checks if players have left or entered the",
                            "muffling area. The default value should be fine, but if you wish to change this, do it as so.")
                    .translation("config.muffling_block.ticks")
                    .defineInRange("ticksIndicatorHandler", 5, 0, 100);

            builder.pop();
        }
    }

    public static class Client {
        public final BooleanValue indicatorEnable;

        public final IntValue xPos;
        public final IntValue yPos;

        public final IntValue size;

        public final BooleanValue tooltipEnable;

        Client(Builder builder) {
            builder.comment("Client only configuration settings.").push("client");

            indicatorEnable = builder
                    .comment("Show muffling indicator.")
                    .translation("config.muffling_indicator.enable")
                    .define("indicatorEnable", true);

            xPos = builder
                    .comment("The x coordinate of the indicator position.")
                    .translation("config.muffling_indicator.xpos")
                    .defineInRange("xPos", 25, 0, Integer.MAX_VALUE);

            yPos = builder
                    .comment("The y coordinate of the indicator position.")
                    .translation("config.muffling_indicator.ypos")
                    .defineInRange("yPos", 25, 0, Integer.MAX_VALUE);

            size = builder
                    .comment("The size of the indicator.")
                    .translation("config.muffling_indicator.size")
                    .defineInRange("size", 10, 1, 100);

            tooltipEnable = builder.comment("Show sound levels and range in the tool tip of the muffling block.",
                    "This tooltip is only shown if it has data saved on it.")
                    .translation("config.muffling_tooltip.enable")
                    .define("tooltipEnable", true);

            builder.pop();
        }

    }

    static final ForgeConfigSpec clientSpecs;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpecs = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static final ForgeConfigSpec commonSpecs;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpecs = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        BetterMuffling.LOG.debug("Loaded {} config file {}", BetterMuffling.MOD_ID, event.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event) {
        BetterMuffling.LOG.fatal(Logging.CORE, "{} config just got changed on the file system!", BetterMuffling.MOD_ID);
    }

}
