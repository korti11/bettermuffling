package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class DeleteEntryButton extends BetterButton {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    public DeleteEntryButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.entry.delete", onPress);
    }

    @Override
    public void renderButton(@Nonnull PoseStack stack, int posX, int posY, float p_renderButton_3_) {
        super.renderButton(stack, posX, posY, p_renderButton_3_);
        RenderSystem.setShaderTexture(0, guiElements);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        this.blit(stack, this.x + xOffset, this.y + yOffset, 197, 32, 16, 16);
    }

}
