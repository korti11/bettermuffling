package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;

public class BetterButton extends Button {

    protected final Screen screen;

    public BetterButton(int widthIn, int heightIn, int width, int height, String text, IPressable onPress) {
        this(widthIn, heightIn, width, height + (height % 2), text, null, onPress);
    }

    public BetterButton(int widthIn, int heightIn, int width, int height, String text, Screen screen, IPressable onPress) {
        super(widthIn, heightIn, width, height + (height % 2), text, onPress);
        this.screen = screen;
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

    protected void renderToolTip(String msg, int mouseX, int mouseY) {
        this.screen.renderTooltip(msg, mouseX, mouseY);
    }
}
