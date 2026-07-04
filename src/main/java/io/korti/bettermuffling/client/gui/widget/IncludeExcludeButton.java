package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class IncludeExcludeButton extends BetterButton {

    private static final Identifier GUI_ELEMENTS = Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 197;

    public IncludeExcludeButton(int widthIn, int heightIn, int width, int height, Screen parent, OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.include.exclude", onPress);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractContents(graphics, mouseX, mouseY, a);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_ELEMENTS, this.getX() + xOffset, this.getY() + yOffset,
                (float) xTexStart, 0.0f, 16, 16, 256, 256);
    }

    public void setIsInclude(boolean flag) {
        this.xTexStart = flag ? 197 : 213;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        super.onPress(input);
        changeTexture();
    }

    private void changeTexture() {
        this.xTexStart = xTexStart == 197 ? 213 : 197;
    }
}
