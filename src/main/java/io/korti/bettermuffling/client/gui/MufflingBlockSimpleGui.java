package io.korti.bettermuffling.client.gui;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.widget.BetterButton;
import io.korti.bettermuffling.client.gui.widget.RangeSlider;
import io.korti.bettermuffling.client.gui.widget.SoundSlider;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public class MufflingBlockSimpleGui extends Screen {

    private static final String titleKey = "gui.muffling_block.title";

    protected final MufflingBlockEntity tileMuffling;
    protected final Identifier background = Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/base_gui.png");
    protected final int xSize;
    protected final int ySize;
    protected int guiTop = 0;
    protected int guiLeft = 0;

    protected MufflingBlockSimpleGui(MufflingBlockEntity tileMuffling, int xSize, int ySize) {
        super(Component.translatable(titleKey));
        this.tileMuffling = tileMuffling;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    protected MufflingBlockSimpleGui(MufflingBlockEntity tileMuffling) {
        this(tileMuffling, 300, 170);
    }

    @Override
    protected void init() {
        this.guiTop = (this.height - this.ySize) / 2;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.initGui();
    }

    protected void initGui() {
        this.clearWidgets();

        int buttonNumber = 0;

        RangeSlider rangeSlider = this.addRenderableWidget(new RangeSlider(this.guiLeft + 10,
                (this.guiTop + 22), 135, 20, tileMuffling.getRange()));
        rangeSlider.setUpdateListener(this.tileMuffling::setRange);
        buttonNumber++;

        String placerKey = getPlacerOnlyButtonMessage();
        this.addRenderableWidget(new BetterButton(this.guiLeft + 10 + buttonNumber % 2 * 145,
                (this.guiTop + 22), 135, 20,
                I18n.get(placerKey),
                (button) -> {
                    tileMuffling.setPlacerOnly(!tileMuffling.isPlacerOnly());
                    button.setMessage(Component.translatable(getPlacerOnlyButtonMessage()));
                }));
        buttonNumber++;

        for (SoundSource category : SoundSource.values()) {
            if (MufflingBlockEntity.IGNORED_CATEGORIES.contains(category)) {
                continue;
            }

            SoundSlider soundSlider = this.addRenderableWidget(new SoundSlider(this.guiLeft + 10 + buttonNumber % 2 * 145,
                    (this.guiTop + 22 + 24 * (buttonNumber >> 1)), 135, 20,
                    tileMuffling.getSoundLevel(category), category));
            soundSlider.setListener(((soundCategory, volume) -> this.tileMuffling.setSoundLevel(soundCategory, volume.floatValue())));
            buttonNumber++;
        }

        this.addRenderableWidget(new BetterButton(this.guiLeft + 50, this.guiTop + 142, 200, 20, I18n.get("gui.done"),
                (button) -> this.onClose()));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.renderForeground(graphics, mouseX, mouseY, a);
    }

    public void renderForeground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        String title = this.title.getString();
        final int x = this.width / 2 - this.font.width(title) / 2;
        final int y = this.guiTop + 7;
        graphics.text(this.font, this.title, x, y, 0xFF404040, false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        int halfHeight = this.ySize / 2;
        int top1 = 0;
        int top2 = 166 - halfHeight;
        int middleWidth = this.xSize - 100;

        // Render left end
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, this.guiLeft, this.guiTop, 0.0f, (float) top1, 50, halfHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, this.guiLeft, this.guiTop + halfHeight, 0.0f, (float) top2, 50, halfHeight, 256, 256);
        // Render middle part
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, this.guiLeft + 50, this.guiTop, 4.0f, (float) top1, middleWidth, halfHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, this.guiLeft + 50, this.guiTop + halfHeight, 4.0f, (float) top2, middleWidth, halfHeight, 256, 256);
        // Render right end
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, this.guiLeft + 50 + middleWidth, this.guiTop, (float) (256 - 50), (float) top1, 50, halfHeight, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, this.guiLeft + 50 + middleWidth, this.guiTop + halfHeight, (float) (256 - 50), (float) top2, 50, halfHeight, 256, 256);
    }

    private String getPlacerOnlyButtonMessage() {
        return tileMuffling.isPlacerOnly() ? "button.muffling_block.placer_only.on" :
                "button.muffling_block.placer_only.off";
    }
}
