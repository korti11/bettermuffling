package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class IncludeExcludeButton extends BetterButton {

    private static final ResourceLocation GUI_ELEMENTS = ResourceLocation.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 197;

    public IncludeExcludeButton(int widthIn, int heightIn, int width, int height, Screen parent, OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.include.exclude", onPress);
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        guiGraphics.blit(GUI_ELEMENTS, this.getX() + xOffset, this.getY() + yOffset, xTexStart, 0, 16, 16);
    }

    public void setIsInclude(boolean flag) {
        this.xTexStart = flag ? 197 : 213;
    }

    @Override
    public void onPress() {
        super.onPress();
        changeTexture();
    }

    private void changeTexture() {
        this.xTexStart = xTexStart == 197 ? 213 : 197;
    }
}
