package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class RangeSlider extends BaseSlider {
    private final short min = 2;
    private final short max = (short) (BetterMufflingConfig.COMMON.maxRange.get() - min);
    private Consumer<Short> listener;

    public RangeSlider(int x, int y, int width, int height, double value) {
        super(x, y, width, height + (height % 2), 0, "button.muffling_block.range");
        this.field_230683_b_ = (value - min) / max;
        this.func_230979_b_();
    }

    private short calcRange() {
        return (short)((this.field_230683_b_ * (double)max) + (double)min);
    }

    public void setUpdateListener(Consumer<Short> listener) {
        this.listener = listener;
    }

    @Override
    protected void func_230979_b_() {
        this.func_238482_a_(new StringTextComponent(I18n.format(this.titleKey) + ": " + calcRange()));
    }

    @Override
    protected void func_230972_a_() {
        if (listener == null) {
            BetterMuffling.LOG.error("Listener not set.");
            return;
        }
        this.listener.accept(calcRange());
    }
}
