package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.BiConsumer;

public class SoundSlider extends BaseSlider {
    private final double min = BetterMufflingConfig.COMMON.minVolume.get();
    private final double max = BetterMufflingConfig.COMMON.maxVolume.get() - min;
    private final SoundCategory category;

    private BiConsumer<SoundCategory, Double> listener;

    public SoundSlider(int x, int y, int width, int height, double value, SoundCategory category) {
        super(x, y, width, height, 0, "soundCategory." + category.getName());
        this.sliderValue = (value - min) / max;
        this.category = category;
        this.func_230979_b_();
    }

    private double calcVolume() {
        return (this.sliderValue * max) + min;
    }

    public void setListener(BiConsumer<SoundCategory, Double> listener) {
        this.listener = listener;
    }

    @Override
    protected void func_230979_b_() {
        String volumeMessage = this.sliderValue == 0 ? I18n.format("options.off") : (int)(calcVolume() * 100) + "%";
        this.setMessage(new StringTextComponent(I18n.format(this.titleKey) + ": " + volumeMessage));
    }

    @Override
    protected void func_230972_a_() {
        if (listener == null) {
            BetterMuffling.LOG.error("Listener not set.");
            return;
        }
        listener.accept(this.category, calcVolume());
    }
}
