package io.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.widget.BaseSlider;
import io.korti.bettermuffling.client.gui.widget.BetterButton;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MufflingBlockGui extends Screen {

    private static final String titleKey = "gui.muffling_block.title";

    private final TileMuffling tileMuffling;
    private final ResourceLocation background = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/base_gui.png");
    private final int xSize = 300;
    private final int ySize = 170;
    private int guiTop = 0;
    private int guiLeft = 0;

    protected MufflingBlockGui(TileMuffling tileMuffling) {
        super(new TranslationTextComponent(titleKey));
        this.tileMuffling = tileMuffling;
    }

    @Override
    protected void init() {
        this.guiTop = (this.height - this.ySize) / 2;
        this.guiLeft = (this.width - this.xSize) / 2;

        this.buttons.clear();

        int buttonNumber = 0;

        this.addButton(new RangeSlider(this.guiLeft + 10 + buttonNumber % 2 * 145,
                (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20, tileMuffling.getRange()));
        buttonNumber++;

        String placerKey = getPlacerOnlyButtonMessage();
        this.addButton(new BetterButton(this.guiLeft + 10 + buttonNumber % 2 * 145,
                (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20,
                I18n.format(placerKey),
                (button) -> {
                    tileMuffling.setPlacerOnly(!tileMuffling.isPlacerOnly());
                    button.setMessage(I18n.format(getPlacerOnlyButtonMessage()));
                }));
        buttonNumber++;

        for (SoundCategory category : SoundCategory.values()) {
            if(category != SoundCategory.MASTER && category != SoundCategory.MUSIC) {
                this.addButton(new SoundSlider(this.guiLeft + 10 + buttonNumber % 2 * 145,
                        (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20,
                        tileMuffling.getSoundLevel(category), category));
                buttonNumber++;
            }
        }

        this.addButton(new BetterButton(this.guiLeft + 50, this.guiTop + 142, 200, 20, I18n.format("gui.done"),
                (button) -> {
                    this.minecraft.displayGuiScreen(null);
                    this.minecraft.mouseHelper.grabMouse();
                }));
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.renderBackground();
        this.drawBaseBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        String title = this.title.getFormattedText();
        this.font.drawString(title, (float) (this.guiLeft + this.xSize / 2 - this.font.getStringWidth(title) / 2), (float) (this.guiTop + 7.5), 4210752);
    }

    private void drawBaseBackground() {
        GlStateManager.clearColor(1F, 1F, 1F, 1F);
        this.minecraft.getTextureManager().bindTexture(background);

        int halfHeight = this.ySize / 2;
        int top1 = 0;
        int top2 = 166 - halfHeight;

        // Render left end
        this.blit(this.guiLeft, this.guiTop, 0, top1, 50, halfHeight);
        this.blit(this.guiLeft, this.guiTop + halfHeight, 0, top2, 50, halfHeight);
        // Render middle part
        this.blit(this.guiLeft + 50, this.guiTop, 28, top1, 200, halfHeight);
        this.blit(this.guiLeft + 50, this.guiTop + halfHeight, 28, top2, 200, halfHeight);
        // Render right end
        this.blit(this.guiLeft + 50 + 200, this.guiTop, 256 - 50, top1, 50, halfHeight);
        this.blit(this.guiLeft + 50 + 200, this.guiTop + halfHeight, 256 - 50, top2, 50, halfHeight);
    }

    private String getPlacerOnlyButtonMessage() {
        return tileMuffling.isPlacerOnly() ? "button.muffling_block.placer_only.on" :
                "button.muffling_block.placer_only.off";
    }

    private class RangeSlider extends BaseSlider {
        private final short min = 2;
        private final short max = (short) (BetterMufflingConfig.COMMON.maxRange.get() - min);

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
            tileMuffling.setRange(calcRange());
        }
    }

    private class SoundSlider extends BaseSlider {
        private final double min = BetterMufflingConfig.COMMON.minVolume.get();
        private final double max = BetterMufflingConfig.COMMON.maxVolume.get() - min;

        private final SoundCategory category;

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
            tileMuffling.setSoundLevel(this.category, (float) calcVolume());
        }
    }

}
