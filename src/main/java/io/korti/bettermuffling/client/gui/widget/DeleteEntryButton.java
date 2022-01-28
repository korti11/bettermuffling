package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;

public class DeleteEntryButton extends BetterButton {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 197;
    private final int yTexStart = 32;

    public DeleteEntryButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.entry.delete", onPress);
    }

    @Override
    public void renderButton(PoseStack stack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.renderButton(stack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindForSetup(guiElements);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        this.blit(stack, this.x + xOffset, this.y + yOffset, xTexStart, yTexStart, 16, 16);
    }

}
