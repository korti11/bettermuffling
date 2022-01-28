package io.korti.bettermuffling.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Collections;

import net.minecraft.client.gui.components.Button.OnPress;

public class BetterButton extends Button {

    private final String toolTipKey;
    private final Button.OnTooltip tooltip;

    protected final Screen screen;

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, OnPress onPress) {
        this(widthIn, heightIn, width, height + (height % 2), textKey, null, "", onPress);
    }

    public BetterButton(int widthIn, int heightIn, int width, int height, String textKey, Screen screen, String toolTipKey, OnPress onPress) {
        super(widthIn, heightIn, width, height + (height % 2), new TranslatableComponent(textKey), onPress);
        this.screen = screen;
        this.toolTipKey = toolTipKey;
        this.tooltip = this::renderToolTip;
    }
    
    @Override
    public void renderToolTip(PoseStack p_230443_1_, int p_230443_2_, int p_230443_3_) {
        this.tooltip.onTooltip(this, p_230443_1_, p_230443_2_, p_230443_3_);
    }

    protected void renderToolTip(Button button, PoseStack stack, int mouseX, int mouseY) {
        if(!toolTipKey.isEmpty()) {
            this.screen.renderComponentTooltip(stack, Collections.singletonList(new TranslatableComponent(toolTipKey)),
                    mouseX, mouseY);
        }
    }

    @Override
    public void renderButton(PoseStack stack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        minecraft.getTextureManager().bindForSetup(WIDGETS_LOCATION);
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, this.alpha);
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

        this.renderBg(stack, minecraft, p_230431_2_, p_230431_3_);
        int j = this.getFGColor();

        drawCenteredString(stack, fontRenderer, super.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
    }
}
