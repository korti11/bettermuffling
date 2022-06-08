package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import java.util.Collections;

public class BetterButton extends Button {

    private final String toolTipKey;
    private final Button.OnTooltip tooltip;

    protected final Screen screen;

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, OnPress onPress) {
        this(widthIn, heightIn, width, height + (height % 2), textKey, null, "", onPress);
    }

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, Screen screen, String toolTipKey, OnPress onPress) {
        super(widthIn, heightIn, width, height + (height % 2), Component.translatable(textKey), onPress);
        this.screen = screen;
        this.toolTipKey = toolTipKey;
        this.tooltip = this::renderToolTip;
    }

    @Override
    public void renderToolTip(@Nonnull PoseStack stack, int posX, int posY) {
        this.tooltip.onTooltip(this, stack, posX, posY);
    }

    protected void renderToolTip(Button button, PoseStack stack, int mouseX, int mouseY) {
        if (!toolTipKey.isEmpty()) {
            this.screen.renderComponentTooltip(stack, Collections.singletonList(Component.translatable(toolTipKey)),
                    mouseX, mouseY);
        }
    }

    @Override
    public void renderButton(@Nonnull PoseStack stack, int posX, int posY, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        int i = this.getYImage(this.isHovered);
        GlStateManager._enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int halfHeight = this.height / 2;
        int top1 = 46 + i * 20;
        int top2 = 46 + i * 20 + (20 - halfHeight);

        this.blit(stack, this.x, this.y, 0, top1, this.width / 2, halfHeight);
        this.blit(stack, this.x, this.y + halfHeight, 0, top2, this.width / 2, halfHeight);
        this.blit(stack, this.x + this.width / 2, this.y, 200 - this.width / 2, top1, this.width / 2, halfHeight);
        this.blit(stack, this.x + this.width / 2, this.y + halfHeight, 200 - this.width / 2, top2, this.width / 2, halfHeight);

        this.renderBg(stack, minecraft, posX, posY);
        int j = this.getFGColor();

        drawCenteredString(stack, fontRenderer, super.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
