package io.korti.bettermuffling.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class BetterButton extends Button {

    private static final ResourceLocation BUTTON_SPRITE = ResourceLocation.withDefaultNamespace("widget/button");
    private static final ResourceLocation HIGHLIGHTED_BUTTON_SPRITE = ResourceLocation.withDefaultNamespace("widget/button_highlighted");
    private static final ResourceLocation DISABLED_BUTTON_SPRITE = ResourceLocation.withDefaultNamespace("widget/button_disabled");

    protected final Screen screen;

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, OnPress onPress) {
        this(widthIn, heightIn, width, height, textKey, null, "", onPress);
    }

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, Screen screen, String toolTipKey, OnPress onPress) {
        super(widthIn, heightIn, width, height + (height % 2), Component.literal(textKey), onPress, Button.DEFAULT_NARRATION);
        this.screen = screen;
        if (!toolTipKey.isEmpty()) {
            this.setTooltip(Tooltip.create(Component.translatable(toolTipKey)));
        }
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font fontRenderer = Minecraft.getInstance().font;

        ResourceLocation sprite = !this.active ? DISABLED_BUTTON_SPRITE : this.isHoveredOrFocused() ? HIGHLIGHTED_BUTTON_SPRITE : BUTTON_SPRITE;
        guiGraphics.blitSprite(sprite, this.getX(), this.getY(), this.width, this.height);

        int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;
        int color = textColor | Mth.ceil(this.alpha * 255.0F) << 24;
        guiGraphics.drawCenteredString(fontRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
    }
}
