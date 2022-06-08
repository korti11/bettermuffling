package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public abstract class BaseSlider extends AbstractSliderButton {

    protected final String titleKey;

    protected BaseSlider(int xIn, int yIn, int widthIn, int heightIn, double valueIn, String titleKey) {
        super(xIn, yIn, widthIn, heightIn + (heightIn % 2), Component.translatable(titleKey), valueIn);
        this.titleKey = titleKey;
    }

    @Override
    public void renderButton(@Nonnull PoseStack stack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
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

        this.renderBg(stack, minecraft, p_renderButton_1_, p_renderButton_2_);
        int j = getFGColor();

        drawCenteredString(stack, fontrenderer, super.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack stack, Minecraft minecraft, int p_renderBg_2_, int p_renderBg_3_) {
        minecraft.getTextureManager().bindForSetup(WIDGETS_LOCATION);
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = (this.isHovered ? 2 : 1) * 20;
        int height = this.height / 2;
        int top1 = 46 + i;
        int top2 = 46 + i + (20 - height);
        // Render the button in quads instead of a pair
        this.blit(stack, this.x + (int) (this.value * (double) (this.width - 8)), this.y, 0, top1, 4, height);
        this.blit(stack, this.x + (int) (this.value * (double) (this.width - 8)), this.y + height, 0, top2, 4, height);
        this.blit(stack, this.x + (int) (this.value * (double) (this.width - 8)) + 4, this.y, 196, top1, 4, height);
        this.blit(stack, this.x + (int) (this.value * (double) (this.width - 8)) + 4, this.y + height, 196, top2, 4, height);
    }

}
