package io.korti.bettermuffling.client.gui.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BetterButton extends Button {

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
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractDefaultSprite(graphics);
        this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
    }
}
