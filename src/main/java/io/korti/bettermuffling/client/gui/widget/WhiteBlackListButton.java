package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.MufflingBlockAdvancedGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class WhiteBlackListButton extends BetterButton {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 197;
    private final int yTexStart = 0;

    public WhiteBlackListButton(int widthIn, int heightIn, int width, int height, Screen parent, IPressable onPress) {
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

        if(this.isHovered()) {
            this.renderToolTip(I18n.format("tooltip.muffling_block.black.white.list"), p_renderButton_1_, p_renderButton_2_);
        }
    }

    public void setIsWhiteList(boolean flag) {
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
