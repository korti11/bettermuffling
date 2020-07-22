package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;

public class BetterButton extends Button {

    private final String toolTipKey;
    private final Button.ITooltip tooltip;

    protected final Screen screen;

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, IPressable onPress) {
        this(widthIn, heightIn, width, height + (height % 2), textKey, null, "", onPress);
    }

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, Screen screen, String toolTipKey, IPressable onPress) {
        super(widthIn, heightIn, width, height + (height % 2), new TranslationTextComponent(textKey), onPress);
        this.screen = screen;
        this.toolTipKey = toolTipKey;
        this.tooltip = this::renderToolTip;
    }

    @Override
    public void func_230443_a_(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_) {
        this.tooltip.onTooltip(this, p_230443_1_, p_230443_2_, p_230443_3_);
    }

    protected void renderToolTip(Button button, MatrixStack stack, int mouseX, int mouseY) {
        if(!toolTipKey.isEmpty()) {
            this.screen.func_238654_b_(stack, Collections.singletonList(new TranslationTextComponent(toolTipKey)),
                    mouseX, mouseY);
        }
    }

    @Override
    public void func_230431_b_(MatrixStack stack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.fontRenderer;
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

        this.func_230441_a_(stack, minecraft, p_230431_2_, p_230431_3_);
        int j = this.getFGColor();

        this.func_238472_a_(stack, fontRenderer, super.func_230458_i_(), this.field_230690_l_ + this.field_230688_j_ / 2, this.field_230691_m_ + (this.field_230689_k_ - 8) / 2, j | MathHelper.ceil(this.field_230695_q_ * 255.0F) << 24);
    }
}
