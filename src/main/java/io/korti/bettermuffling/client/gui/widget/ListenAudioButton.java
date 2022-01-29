package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;

public class ListenAudioButton extends BetterButton {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 213;

    public ListenAudioButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "", onPress);
    }

    @Override
    public void renderButton(@Nonnull PoseStack stack, int posX, int posY, float p_renderButton_3_) {
        super.renderButton(stack, posX, posY, p_renderButton_3_);
        RenderSystem.setShaderTexture(0, guiElements);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        this.blit(stack, this.x + xOffset, this.y + yOffset, xTexStart, 16, 16, 16);
    }

    @Override
    protected void renderToolTip(Button button, PoseStack stack, int mouseX, int mouseY) {
        if (xTexStart == 197) {
            this.screen.renderComponentTooltip(stack,
                    Collections.singletonList(new TranslatableComponent("tooltip.muffling_block.listening.on")),
                    mouseX, mouseY);
        } else {
            this.screen.renderComponentTooltip(stack,
                    Collections.singletonList(new TranslatableComponent("tooltip.muffling_block.listening.off")),
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
