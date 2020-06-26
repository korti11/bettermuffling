package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class BaseSlider extends AbstractSlider {

    protected final String titleKey;

    protected BaseSlider(int xIn, int yIn, int widthIn, int heightIn, double valueIn, String titleKey) {
        super(xIn, yIn, widthIn, heightIn + (heightIn % 2), new TranslationTextComponent(titleKey), valueIn);
        this.titleKey = titleKey;
    }

    @Override
    public void func_230431_b_(MatrixStack stack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        minecraft.getTextureManager().bindTexture(field_230687_i_);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.field_230695_q_);
        int i = this.func_230989_a_(this.func_230449_g_());
        GlStateManager.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int halfHeight = this.getHeight() / 2;
        int top1 = 46 + i * 20;
        int top2 = 46 + i * 20 + (20 - halfHeight);

        this.func_238474_b_(stack, this.field_230690_l_, this.field_230691_m_, 0, top1, this.field_230688_j_ / 2, halfHeight);
        this.func_238474_b_(stack, this.field_230690_l_, this.field_230691_m_ + halfHeight, 0, top2, this.field_230688_j_ / 2, halfHeight);
        this.func_238474_b_(stack, this.field_230690_l_ + this.field_230688_j_ / 2, this.field_230691_m_, 200 - this.field_230688_j_ / 2, top1, this.field_230688_j_ / 2, halfHeight);
        this.func_238474_b_(stack, this.field_230690_l_ + this.field_230688_j_ / 2, this.field_230691_m_ + halfHeight, 200 - this.field_230688_j_ / 2, top2, this.field_230688_j_ / 2, halfHeight);

        this.func_230441_a_(stack, minecraft, p_renderButton_1_, p_renderButton_2_);
        int j = getFGColor();

        this.func_238472_a_(stack, fontrenderer, super.func_230458_i_(), this.field_230690_l_ + this.field_230688_j_ / 2, this.field_230691_m_ + (this.field_230689_k_ - 8) / 2, j | MathHelper.ceil(this.field_230695_q_ * 255.0F) << 24);
    }

    @Override
    protected void func_230441_a_(MatrixStack stack, Minecraft minecraft, int p_renderBg_2_, int p_renderBg_3_) {
        minecraft.getTextureManager().bindTexture(field_230687_i_);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.func_230449_g_() ? 2 : 1) * 20;
        int height = this.getHeight() / 2;
        int top1 = 46 + i;
        int top2 = 46 + i + (20 - height);
        // Render the button in quads instead of a pair
        this.func_238474_b_(stack, this.field_230690_l_ + (int)(this.field_230683_b_ * (double)(this.field_230688_j_ - 8)), this.field_230691_m_, 0, top1, 4, height);
        this.func_238474_b_(stack, this.field_230690_l_ + (int)(this.field_230683_b_ * (double)(this.field_230688_j_ - 8)), this.field_230691_m_ + height, 0, top2, 4, height);
        this.func_238474_b_(stack, this.field_230690_l_ + (int)(this.field_230683_b_ * (double)(this.field_230688_j_ - 8)) + 4, this.field_230691_m_, 196, top1, 4, height);
        this.func_238474_b_(stack, this.field_230690_l_ + (int)(this.field_230683_b_ * (double)(this.field_230688_j_ - 8)) + 4, this.field_230691_m_ + height, 196, top2, 4, height);
    }

}
