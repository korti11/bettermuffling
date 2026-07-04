package io.korti.bettermuffling.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public abstract class BaseSlider extends AbstractSliderButton {

    private static final Identifier SLIDER_SPRITE = Identifier.withDefaultNamespace("widget/slider");
    private static final Identifier HIGHLIGHTED_SLIDER_SPRITE = Identifier.withDefaultNamespace("widget/slider_highlighted");
    private static final Identifier SLIDER_HANDLE_SPRITE = Identifier.withDefaultNamespace("widget/slider_handle");
    private static final Identifier SLIDER_HANDLE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/slider_handle_highlighted");

    protected final String titleKey;

    protected BaseSlider(int xIn, int yIn, int widthIn, int heightIn, double valueIn, String titleKey) {
        super(xIn, yIn, widthIn, heightIn + (heightIn % 2), Component.translatable(titleKey), valueIn);
        this.titleKey = titleKey;
    }

    @Override
    public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Identifier trackSprite = this.active ? (this.isHoveredOrFocused() ? HIGHLIGHTED_SLIDER_SPRITE : SLIDER_SPRITE) : SLIDER_SPRITE;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, trackSprite, this.getX(), this.getY(), this.width, this.height, ARGB.white(this.alpha));

        Identifier handleSprite = this.isHoveredOrFocused() ? SLIDER_HANDLE_HIGHLIGHTED_SPRITE : SLIDER_HANDLE_SPRITE;
        int handleX = this.getX() + (int) (this.value * (double) (this.width - 8));
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, handleSprite, handleX, this.getY(), 8, this.height, ARGB.white(this.alpha));

        int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;
        int color = (Mth.ceil(this.alpha * 255.0F) << 24) | textColor;
        graphics.centeredText(Minecraft.getInstance().font, this.getMessage(),
                this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
    }
}
