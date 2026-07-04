package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class DeleteEntryButton extends BetterButton {

    private static final Identifier GUI_ELEMENTS = Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    public DeleteEntryButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.entry.delete", onPress);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractContents(graphics, mouseX, mouseY, a);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, this.getX() + xOffset, this.getY() + yOffset,
                197.0f, 32.0f, 16, 16, 256, 256);
    }
}
