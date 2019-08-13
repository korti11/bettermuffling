package net.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.gui.widget.SoundSlider;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MufflingBlockGui extends Screen {

    private static final String titleKey = "gui.muffling_block.title";

    private final TileMuffling tileMuffling;

    protected MufflingBlockGui(TileMuffling tileMuffling) {
        super(new TranslationTextComponent(titleKey));
        this.tileMuffling = tileMuffling;
    }

    @Override
    protected void init() {
        this.buttons.clear();

        int buttonNumber = 0;

        this.addButton(new RangeSlider(this.width / 2 - 155 + buttonNumber % 2 * 160,
                (this.height / 6 - 12 + 24 * (buttonNumber >> 1)) + 35, 150, 20, tileMuffling.getRange()));
        buttonNumber++;

        String placerKey = getPlacerOnlyButtonMessage();
        this.addButton(new Button(this.width / 2 - 155 + buttonNumber % 2 * 160,
                (this.height / 6 - 12 + 24 * (buttonNumber >> 1)) + 35, 150, 20,
                I18n.format(placerKey),
                (button) -> {
                    tileMuffling.setPlacerOnly(!tileMuffling.isPlacerOnly());
                    button.setMessage(I18n.format(getPlacerOnlyButtonMessage()));
                }));
        buttonNumber++;

        for (SoundCategory category : SoundCategory.values()) {
            if(category != SoundCategory.MASTER && category != SoundCategory.MUSIC) {
                this.addButton(new SoundSlider(this.width / 2 - 155 + buttonNumber % 2 * 160,
                        (this.height / 6 - 12 + 24 * (buttonNumber >> 1)) + 35, 150, 20,
                        tileMuffling.getSoundLevel(category), category));
                buttonNumber++;
            }
        }

        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 143, 200, 20, I18n.format("gui.done"),
                (button) -> {
                    this.minecraft.displayGuiScreen(null);
                    this.minecraft.mouseHelper.grabMouse();
                }));
    }

    private String getPlacerOnlyButtonMessage() {
        return tileMuffling.isPlacerOnly() ? "button.muffling_block.placer_only.on" :
                "button.muffling_block.placer_only.off";
    }

    private class RangeSlider extends AbstractSlider {
        private static final short min = 2;
        private static final short max = 16 - min;    // TODO: Get value form config.

        public RangeSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, (value - min) / max);
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
            tileMuffling.setRange(calcRange());
        }
    }

    private class SoundSlider extends AbstractSlider {
        private static final short min = 0;     // TODO: Get value from config.
        private static final short max = 1;     // TODO: Get value from config.

        private final SoundCategory category;

        public SoundSlider(int x, int y, int width, int height, double value, SoundCategory category) {
            super(x, y, width, height, (value - min) / max);
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
            tileMuffling.setSoundLevel(this.category, (float) calcVolume());
        }
    }

}
