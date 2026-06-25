package io.korti.bettermuffling.common.config;

import io.korti.bettermuffling.BetterMuffling;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class BetterMufflingConfig {

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, clientSpecs);
        container.registerConfig(ModConfig.Type.COMMON, commonSpecs);
    }

    public static class Common {
        public final IntValue maxRange;
        public final DoubleValue minVolume;
        public final DoubleValue maxVolume;

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

            builder.pop();
        }
    }

    public static class Client {
        public final BooleanValue tooltipEnable;

        Client(Builder builder) {
            builder.comment("Client only configuration settings.").push("client");

            tooltipEnable = builder.comment("Show sound levels and range in the tool tip of the muffling block.",
                            "This tooltip is only shown if it has data saved on it.")
                    .translation("config.muffling_tooltip.enable")
                    .define("tooltipEnable", true);

            builder.pop();
        }
    }

    static final ModConfigSpec clientSpecs;
    public static final Client CLIENT;

    static {
        final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
        clientSpecs = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static final ModConfigSpec commonSpecs;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        commonSpecs = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
