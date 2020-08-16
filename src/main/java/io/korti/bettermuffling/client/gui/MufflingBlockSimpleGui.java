package io.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.widget.BetterButton;
import io.korti.bettermuffling.client.gui.widget.RangeSlider;
import io.korti.bettermuffling.client.gui.widget.SoundSlider;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MufflingBlockSimpleGui extends Screen {

    private static final String titleKey = "gui.muffling_block.title";

    protected final TileMuffling tileMuffling;
    protected final ResourceLocation background = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/base_gui.png");
    protected final int xSize;
    protected final int ySize;
    protected int guiTop = 0;
    protected int guiLeft = 0;

    protected MufflingBlockSimpleGui(TileMuffling tileMuffling, int xSize, int ySize) {
        super(new TranslationTextComponent(titleKey));
        this.tileMuffling = tileMuffling;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    protected MufflingBlockSimpleGui(TileMuffling tileMuffling) {
        this(tileMuffling, 300, 170);
    }

    @Override
    protected void func_231160_c_() {
        this.guiTop = (this.field_230709_l_ - this.ySize) / 2;
        this.guiLeft = (this.field_230708_k_ - this.xSize) / 2;
        this.initGui();
    }

    protected void initGui() {
        this.field_230710_m_.clear();

        int buttonNumber = 0;

        RangeSlider rangeSlider = this.func_230480_a_(new RangeSlider(this.guiLeft + 10 + buttonNumber % 2 * 145,
                (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20, tileMuffling.getRange()));
        rangeSlider.setUpdateListener(this.tileMuffling::setRange);
        buttonNumber++;

        String placerKey = getPlacerOnlyButtonMessage();
        this.func_230480_a_(new BetterButton(this.guiLeft + 10 + buttonNumber % 2 * 145,
                (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20,
                I18n.format(placerKey),
                (button) -> {
                    tileMuffling.setPlacerOnly(!tileMuffling.isPlacerOnly());
                    button.func_238482_a_(new TranslationTextComponent(getPlacerOnlyButtonMessage()));
                }));
        buttonNumber++;

        for (SoundCategory category : SoundCategory.values()) {
            if(category != SoundCategory.MASTER && category != SoundCategory.MUSIC) {
                SoundSlider soundSlider = this.func_230480_a_(new SoundSlider(this.guiLeft + 10 + buttonNumber % 2 * 145,
                        (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20,
                        tileMuffling.getSoundLevel(category), category));
                soundSlider.setListener(((soundCategory, volume) -> this.tileMuffling.setSoundLevel(soundCategory, volume.floatValue())));
                buttonNumber++;
            }
        }

        this.func_230480_a_(new BetterButton(this.guiLeft + 50, this.guiTop + 142, 200, 20, I18n.format("gui.done"),
                (button) -> {
                    this.func_231175_as__();
                }));
    }

    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(stack);
        this.renderForeground(stack, mouseX, mouseY, partialTicks);
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);

        for(Widget widget : this.field_230710_m_) {
            if(widget.func_230449_g_()) {
                widget.func_230443_a_(stack, mouseX, mouseY);
            }
        }
    }

    public void renderForeground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        String title = this.field_230704_d_.getString();
        final float x = (float) (this.field_230708_k_ / 2 - this.field_230712_o_.getStringWidth(title) / 2);
        final float y = (float) (this.guiTop + 7.5);
        this.field_230712_o_.func_243248_b(stack, this.field_230704_d_, x, y, 4210752);
    }

    @Override
    public void func_230446_a_(MatrixStack stack) {
        super.func_230446_a_(stack);

        GlStateManager.clearColor(1F, 1F, 1F, 1F);
        this.field_230706_i_.getTextureManager().bindTexture(background);

        int halfHeight = this.ySize / 2;
        int top1 = 0;
        int top2 = 166 - halfHeight;
        int middleWidth = this.xSize - 100;

        // Render left end
        this.func_238474_b_(stack, this.guiLeft, this.guiTop, 0, top1, 50, halfHeight);
        this.func_238474_b_(stack, this.guiLeft, this.guiTop + halfHeight, 0, top2, 50, halfHeight);
        // Render middle part
        this.func_238474_b_(stack, this.guiLeft + 50, this.guiTop, 4, top1, middleWidth, halfHeight);
        this.func_238474_b_(stack, this.guiLeft + 50, this.guiTop + halfHeight, 4, top2, middleWidth, halfHeight);
        // Render right end
        this.func_238474_b_(stack, this.guiLeft + 50 + middleWidth, this.guiTop, 256 - 50, top1, 50, halfHeight);
        this.func_238474_b_(stack, this.guiLeft + 50 + middleWidth, this.guiTop + halfHeight, 256 - 50, top2, 50, halfHeight);

        // Render sound category list
    }

    private String getPlacerOnlyButtonMessage() {
        return tileMuffling.isPlacerOnly() ? "button.muffling_block.placer_only.on" :
                "button.muffling_block.placer_only.off";
    }

    private boolean isAdvancedModeOn() {
        return true;
    }

}
