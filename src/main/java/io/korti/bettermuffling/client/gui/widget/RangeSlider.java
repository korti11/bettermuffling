package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.resources.I18n;

import java.util.function.Consumer;

public class RangeSlider extends BaseSlider {
    private final short min = 2;
    private final short max = (short) (BetterMufflingConfig.COMMON.maxRange.get() - min);
    private Consumer<Short> listener;

    public RangeSlider(int x, int y, int width, int height, double value) {
        super(x, y, width, height + (height % 2), 0);
        this.value = (value - min) / max;
        this.updateMessage();
    }

    private short calcRange() {
        return (short)((this.value * (double)max) + (double)min);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(I18n.format("button.muffling_block.range") + ": " + calcRange());
    }

    @Override
    protected void applyValue() {
        if (listener == null) {
            BetterMuffling.LOG.error("Listener not set.");
            return;
        }
        this.listener.accept(calcRange());
    }

    public void setUpdateListener(Consumer<Short> listener) {
        this.listener = listener;
    }
}
