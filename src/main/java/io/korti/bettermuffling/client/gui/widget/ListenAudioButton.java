package io.korti.bettermuffling.client.gui.widget;

import io.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ListenAudioButton extends BetterButton {

    private static final ResourceLocation GUI_ELEMENTS = ResourceLocation.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private int xTexStart = 213;

    public ListenAudioButton(int widthIn, int heightIn, int width, int height, Screen parent, Button.OnPress onPress) {
        super(widthIn, heightIn, width, height, "", parent, "", onPress);
        this.setTooltip(Tooltip.create(Component.translatable("tooltip.muffling_block.listening.off")));
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        int xOffset = (this.width - 16) / 2;
        int yOffset = (this.height - 16) / 2;
        guiGraphics.blit(GUI_ELEMENTS, this.getX() + xOffset, this.getY() + yOffset, xTexStart, 16, 16, 16);
    }

    public void setIsListening(boolean flag) {
        this.xTexStart = flag ? 197 : 213;
        updateTooltip();
    }

    @Override
    public void onPress() {
        super.onPress();
        changeTexture();
    }

    private void changeTexture() {
        this.xTexStart = xTexStart == 197 ? 213 : 197;
        updateTooltip();
    }

    private void updateTooltip() {
        String key = xTexStart == 197 ? "tooltip.muffling_block.listening.on" : "tooltip.muffling_block.listening.off";
        this.setTooltip(Tooltip.create(Component.translatable(key)));
    }

}
