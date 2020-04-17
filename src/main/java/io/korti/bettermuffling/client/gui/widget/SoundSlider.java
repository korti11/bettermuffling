package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SoundSlider extends BaseSlider {
    private final double min = BetterMufflingConfig.COMMON.minVolume.get();
    private final double max = BetterMufflingConfig.COMMON.maxVolume.get() - min;
    private final SoundCategory category;

    private BiConsumer<SoundCategory, Double> listener;

    public SoundSlider(int x, int y, int width, int height, double value, SoundCategory category) {
        super(x, y, width, height, 0);
        this.value = (value - min) / max;
        this.category = category;
        this.updateMessage();
    }

    private double calcVolume() {
        return (this.value * max) + min;
    }

    @Override
    protected void updateMessage() {
        String volumeMessage = this.value == 0 ? I18n.format("options.off") : (int)(calcVolume() * 100) + "%";
        this.setMessage(I18n.format("soundCategory." + this.category.getName()) + ": " + volumeMessage);
    }

    @Override
    protected void applyValue() {
        if (listener == null) {
            BetterMuffling.LOG.error("Listener not set.");
            return;
        }
        listener.accept(this.category, calcVolume());
    }

    public void setListener(BiConsumer<SoundCategory, Double> listener) {
        this.listener = listener;
    }
}
