package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class DeleteEntryButton extends BetterButton {

    private static final ResourceLocation GUI_ELEMENTS = ResourceLocation.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    public DeleteEntryButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.entry.delete", onPress);
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        guiGraphics.blit(GUI_ELEMENTS, this.getX() + xOffset, this.getY() + yOffset, 197, 32, 16, 16);
    }

}
