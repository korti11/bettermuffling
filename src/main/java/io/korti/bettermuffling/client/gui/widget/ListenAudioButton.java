package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;

public class ListenAudioButton extends BetterButton {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 213;
    private final int yTexStart = 16;

    public ListenAudioButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.IPressable onPress) {
        super(widthIn, heightIn, width, height, "", parent, "", onPress);
    }

    @Override
    public void renderButton(MatrixStack stack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.renderButton(stack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(guiElements);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        this.blit(stack, this.x + xOffset, this.y + yOffset, xTexStart, yTexStart, 16, 16);
    }

    @Override
    protected void renderToolTip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        if(xTexStart == 197) {
            this.screen.func_243308_b(stack,
                    Collections.singletonList(new TranslationTextComponent("tooltip.muffling_block.listening.on")),
                    mouseX, mouseY);
        } else {
            this.screen.func_243308_b(stack,
                    Collections.singletonList(new TranslationTextComponent("tooltip.muffling_block.listening.off")),
                    mouseX, mouseY);
        }
    }

    public void setIsListening(boolean flag) {
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
