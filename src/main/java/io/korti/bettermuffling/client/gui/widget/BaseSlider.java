package io.korti.bettermuffling.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public abstract class BaseSlider extends AbstractSliderButton {

    private static final ResourceLocation SLIDER_SPRITE = ResourceLocation.withDefaultNamespace("widget/slider");
    private static final ResourceLocation HIGHLIGHTED_SLIDER_SPRITE = ResourceLocation.withDefaultNamespace("widget/slider_highlighted");
    private static final ResourceLocation SLIDER_HANDLE_SPRITE = ResourceLocation.withDefaultNamespace("widget/slider_handle");
    private static final ResourceLocation SLIDER_HANDLE_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("widget/slider_handle_highlighted");

    protected final String titleKey;

    protected BaseSlider(int xIn, int yIn, int widthIn, int heightIn, double valueIn, String titleKey) {
        super(xIn, yIn, widthIn, heightIn + (heightIn % 2), Component.translatable(titleKey), valueIn);
        this.titleKey = titleKey;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font fontrenderer = Minecraft.getInstance().font;

        ResourceLocation trackSprite = this.active ? (this.isHoveredOrFocused() ? HIGHLIGHTED_SLIDER_SPRITE : SLIDER_SPRITE) : SLIDER_SPRITE;
        guiGraphics.blitSprite(trackSprite, this.getX(), this.getY(), this.width, this.height);

        ResourceLocation handleSprite = this.isHoveredOrFocused() ? SLIDER_HANDLE_HIGHLIGHTED_SPRITE : SLIDER_HANDLE_SPRITE;
        int handleX = this.getX() + (int) (this.value * (double) (this.width - 8));
        guiGraphics.blitSprite(handleSprite, handleX, this.getY(), 8, this.height);

        int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;
        int color = textColor | Mth.ceil(this.alpha * 255.0F) << 24;
        guiGraphics.drawCenteredString(fontrenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
    }
}
