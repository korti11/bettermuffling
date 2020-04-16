package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class DeleteEntryButton extends BetterButton {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 197;
    private final int yTexStart = 32;

    public DeleteEntryButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.IPressable onPress) {
        super(widthIn, heightIn, width, height, "", parent, onPress);
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.renderButton(p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(guiElements);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        this.blit(this.x + xOffset, this.y + yOffset, xTexStart, yTexStart, 16, 16);
    }

    public void renderToolTip(int mouseX, int mouseY) {
        if(this.isHovered()) {
            this.renderToolTip(I18n.format("tooltip.muffling_block.entry.delete"), mouseX, mouseY);
        }
    }

}
