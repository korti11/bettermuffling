package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;

public abstract class BaseSlider extends AbstractSlider {

    protected BaseSlider(int xIn, int yIn, int widthIn, int heightIn, double valueIn) {
        super(xIn, yIn, widthIn, heightIn + (heightIn % 2), valueIn);
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        GlStateManager.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int halfHeight = this.getHeight() / 2;
        int top1 = 46 + i * 20;
        int top2 = 46 + i * 20 + (20 - halfHeight);

        this.blit(this.x, this.y, 0, top1, this.width / 2, halfHeight);
        this.blit(this.x, this.y + halfHeight, 0, top2, this.width / 2, halfHeight);
        this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, top1, this.width / 2, halfHeight);
        this.blit(this.x + this.width / 2, this.y + halfHeight, 200 - this.width / 2, top2, this.width / 2, halfHeight);

        this.renderBg(minecraft, p_renderButton_1_, p_renderButton_2_);
        int j = getFGColor();

        this.drawCenteredString(fontrenderer, super.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    protected void renderBg(Minecraft p_renderBg_1_, int p_renderBg_2_, int p_renderBg_3_) {
        p_renderBg_1_.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHovered() ? 2 : 1) * 20;
        int height = this.getHeight() / 2;
        int top1 = 46 + i;
        int top2 = 46 + i + (20 - height);
        // Render the button in quads instead of a pair
        this.blit(this.x + (int)(this.value * (double)(this.width - 8)), this.y, 0, top1, 4, height);
        this.blit(this.x + (int)(this.value * (double)(this.width - 8)), this.y + height, 0, top2, 4, height);
        this.blit(this.x + (int)(this.value * (double)(this.width - 8)) + 4, this.y, 196, top1, 4, height);
        this.blit(this.x + (int)(this.value * (double)(this.width - 8)) + 4, this.y + height, 196, top2, 4, height);
    }

}
