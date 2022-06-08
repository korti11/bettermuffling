package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;

import java.util.function.BiConsumer;

public class SoundSlider extends BaseSlider {
    private final double min = BetterMufflingConfig.COMMON.minVolume.get();
    private final double max = BetterMufflingConfig.COMMON.maxVolume.get() - min;
    private final SoundSource category;

    private BiConsumer<SoundSource, Double> listener;

    public SoundSlider(int x, int y, int width, int height, double value, SoundSource category) {
        super(x, y, width, height, 0, "soundCategory." + category.getName());
        this.value = (value - min) / max;
        this.category = category;
        this.updateMessage();
    }

    private double calcVolume() {
        return (this.value * max) + min;
    }

    public void setListener(BiConsumer<SoundSource, Double> listener) {
        this.listener = listener;
    }

    @Override
    protected void updateMessage() {
        String volumeMessage = this.value == 0 ? I18n.get("options.off") : (int) (calcVolume() * 100) + "%";
        this.setMessage(Component.literal(I18n.get(this.titleKey) + ": " + volumeMessage));
    }

    @Override
    protected void applyValue() {
        if (listener == null) {
            BetterMuffling.LOG.error("Listener not set.");
            return;
        }
        listener.accept(this.category, calcVolume());
    }
}
