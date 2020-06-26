package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
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
        super(widthIn, heightIn, width, height, "", parent, "tooltip.muffling_block.entry.delete", onPress);
    }

    @Override
    public void func_230431_b_(MatrixStack stack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        super.func_230431_b_(stack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(guiElements);

        int xOffset = (this.field_230688_j_ - 16) / 2;
        int yOffset = (this.field_230689_k_ - 16) / 2;
        this.func_238474_b_(stack, this.field_230690_l_ + xOffset, this.field_230691_m_ + yOffset, xTexStart, yTexStart, 16, 16);
    }

}
