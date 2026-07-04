package io.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.widget.*;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.*;

public class MufflingBlockAdvancedGui extends MufflingBlockSimpleGui {

    private final Identifier guiElements = Identifier.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private ScrollList soundNamesList = null;
    private Button selectedSoundCategoryButton = null;
    private SoundSlider activeSoundSlider = null;

    private IncludeExcludeButton activeIncludeExcludeButton = null;

    protected MufflingBlockAdvancedGui(MufflingBlockEntity tileMuffling) {
        super(tileMuffling, 348, 222);
    }

    @Override
    protected void initGui() {
        this.soundNamesList = new ScrollList(this.guiLeft + 130, this.guiTop + 69, 181, 121);
        this.addWidget(this.soundNamesList);

        int buttonCount = 0;
        for (final SoundSource category : SoundSource.values()) {
            if (MufflingBlockEntity.IGNORED_CATEGORIES.contains(category)) {
                continue;
            }
            SoundSlider soundSlider = this.addRenderableWidget(new SoundSlider(this.guiLeft + 130, this.guiTop + 31, 180, 20,
                    tileMuffling.getSoundLevel(category).doubleValue(), category));
            soundSlider.visible = false;
            soundSlider.setListener((c, volume) -> tileMuffling.setSoundLevel(c, volume.floatValue()));

            IncludeExcludeButton includeExcludeButton = this.addRenderableWidget(new IncludeExcludeButton(this.guiLeft + 315,
                    this.guiTop + 69, 20, 20, this, (b) -> tileMuffling.setIncludeModeForCategory(category, !tileMuffling.getIncludeModeForCategory(category))));
            includeExcludeButton.setIsInclude(tileMuffling.getIncludeModeForCategory(category));
            includeExcludeButton.visible = false;

            BetterButton button = this.addRenderableWidget(new BetterButton(this.guiLeft + 11,
                    this.guiTop + 31 + (20 * buttonCount), 110, 20, I18n.get("soundCategory." + category.getName()),
                    (b) -> {
                        activeSoundSlider.visible = false;
                        activeSoundSlider = soundSlider;
                        soundSlider.visible = true;

                        activeIncludeExcludeButton.visible = false;
                        activeIncludeExcludeButton = includeExcludeButton;
                        includeExcludeButton.visible = true;

                        selectedSoundCategoryButton.active = true;
                        selectedSoundCategoryButton = b;
                        b.active = false;

                        this.tileMuffling.setSelectedCategory(category);
                        this.soundNamesList.selectSoundCategory(category);
                    }));

            if (category == tileMuffling.getSelectedCategory()) {
                this.activeSoundSlider = soundSlider;
                soundSlider.visible = true;
                this.activeIncludeExcludeButton = includeExcludeButton;
                includeExcludeButton.visible = true;
                this.selectedSoundCategoryButton = button;
                button.active = false;
                this.soundNamesList.selectSoundCategory(category);
            }
            buttonCount++;
        }

        RangeSlider rangeSlider = this.addRenderableWidget(new RangeSlider(this.guiLeft + 11, this.guiTop + 195, 158, 20,
                this.tileMuffling.getRange()));
        rangeSlider.setUpdateListener(this.tileMuffling::setRange);

        this.addRenderableWidget(new BetterButton(this.guiLeft + 11 + 158 + 8, this.guiTop + 195, 158, 20,
                I18n.get("gui.done"), (b) -> this.onClose()));

        LockIconButton lockIconButton = this.addRenderableWidget(new LockIconButton(this.guiLeft + 315, this.guiTop + 31,
                (b) -> {
                    LockIconButton lb = (LockIconButton) b;
                    lb.setLocked(!lb.isLocked());
                    tileMuffling.setPlacerOnly(((LockIconButton) b).isLocked());
                }));
        lockIconButton.setLocked(tileMuffling.isPlacerOnly());

        ListenAudioButton listenAudioButton = this.addRenderableWidget(new ListenAudioButton(this.guiLeft + 315, this.guiTop + 94,
                20, 20, this, (b) -> tileMuffling.setListening(!tileMuffling.isListening())));
        listenAudioButton.setIsListening(tileMuffling.isListening());

        this.addRenderableWidget(new DeleteEntryButton(this.guiLeft + 315, this.guiTop + 119,
                20, 20, this, (b) -> this.soundNamesList.removeSelectedEntry()));
    }

    @Override
    public void removed() {
        super.removed();
        this.tileMuffling.syncToServer();
    }

    @Override
    public void renderForeground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.renderForeground(graphics, mouseX, mouseY, a);
        graphics.text(this.font, Component.translatable("label.muffling_block.sound.category"), this.guiLeft + 11, this.guiTop + 21, 0xFF404040, false);
        graphics.text(this.font, Component.translatable("label.muffling_block.volume"), this.guiLeft + 130, this.guiTop + 21, 0xFF404040, false);
        graphics.text(this.font, Component.translatable("label.muffling_block.sound.names"), this.guiLeft + 130, this.guiTop + 60, 0xFF404040, false);
        this.soundNamesList.renderForeground(graphics);
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        this.soundNamesList.renderBackground(graphics);
    }

    private class ScrollList implements GuiEventListener, NarratableEntry {

        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final Map<SoundSource, Pair<SortedSet<String>, List<String>>> soundCategoryNameMap = new HashMap<>();

        private float scrollValue = 0f;
        private final int scrollPosX;
        private final int scrollPosY;
        private final int listWidth;
        private final Set<String> selectedEntries = new HashSet<>();

        private SortedSet<String> soundNameSet;
        private List<String> soundNames;

        private boolean scrollBarClicked = false;

        public ScrollList(int xIn, int yIn, int widthIn, int heightIn) {
            this.x = xIn;
            this.y = yIn;
            this.width = widthIn;
            this.height = heightIn;

            this.listWidth = this.width - 8;
            this.scrollPosX = this.x + this.listWidth + 1;
            this.scrollPosY = this.y + 1;
            this.init();
        }

        protected void init() {
            for (final SoundSource category : SoundSource.values()) {
                if (MufflingBlockEntity.IGNORED_CATEGORIES.contains(category)) {
                    continue;
                }
                soundCategoryNameMap.put(category,
                        Pair.of(MufflingBlockAdvancedGui.this.tileMuffling.getNameSet(category), new LinkedList<>()));
                this.selectSoundCategory(category);
            }
            this.selectSoundCategory(SoundSource.RECORDS);
        }

        private void selectSoundCategory(SoundSource category) {
            Pair<SortedSet<String>, List<String>> pair = soundCategoryNameMap.get(category);
            this.soundNameSet = pair.getLeft();
            this.soundNames = pair.getRight();
            this.updateSoundNames();
        }

        private void drawString(GuiGraphicsExtractor graphics, String msg, int x, int y, int color) {
            graphics.text(MufflingBlockAdvancedGui.this.font, msg, x, y, color, false);
        }

        private void updateSoundNames() {
            this.soundNames.clear();
            this.soundNames.addAll(this.soundNameSet);

            if (this.soundNameSet.size() < 12) {
                this.scrollValue = 0f;
            } else if (((this.soundNameSet.size() - 12) * scrollValue) > this.soundNameSet.size()) {
                this.scrollValue = 1f;
            }
            this.selectedEntries.clear();
        }

        private void removeSelectedEntry() {
            this.soundNameSet.removeAll(this.selectedEntries);
            this.updateSoundNames();
        }

        private int getScrollYOffset() {
            return (int) ((this.height - 1 - 27) * this.scrollValue);
        }

        public void renderForeground(GuiGraphicsExtractor graphics) {
            int scrollBarUV = isScrollBarEnabled() ? 185 : 191;
            int yOffset = getScrollYOffset();
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements,
                    this.scrollPosX, this.scrollPosY + yOffset, (float) scrollBarUV, 0.0f, 6, 27, 256, 256);

            int startValue = Math.max((int) ((this.soundNameSet.size() - 12) * scrollValue), 0);
            int maxValue = Math.min(startValue + 12, this.soundNameSet.size());
            int elementOffset = 0;
            for (int i = startValue; i < maxValue && i < this.soundNameSet.size() && i < this.soundNames.size(); i++) {
                String current = soundNames.get(i);
                if (this.selectedEntries.contains(current)) {
                    drawString(graphics, current, this.x + 2, this.y + 2 + (elementOffset * 10), 0xFFFFFFA0);
                } else {
                    drawString(graphics, current, this.x + 2, this.y + 2 + (elementOffset * 10), 0xFF404040);
                }
                elementOffset++;
            }
        }

        public void renderBackground(GuiGraphicsExtractor graphics) {
            int listWidth = this.listWidth - 1;
            int halfHeight = Math.round(this.height / 2f);
            int top1 = 0;
            int top2 = 142 - halfHeight;

            // Render left end
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements, this.x, this.y, 0.0f, (float) top1, 1, halfHeight, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements, this.x, this.y + halfHeight, 0.0f, (float) top2, 1, halfHeight + 1, 256, 256);
            // Render middle
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements, this.x + 1, this.y, 1.0f, (float) top1, listWidth, halfHeight, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements, this.x + 1, this.y + halfHeight, 1.0f, (float) top2, listWidth, halfHeight + 1, 256, 256);
            // Render right end
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements, this.scrollPosX - 1, this.y, 177.0f, (float) top1, 8, halfHeight, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, MufflingBlockAdvancedGui.this.guiElements, this.scrollPosX - 1, this.y + halfHeight, 177.0f, (float) top2, 8, halfHeight + 1, 256, 256);
        }

        private boolean isScrollBarEnabled() {
            return this.soundNameSet.size() > 12;
        }

        private boolean isOverScrollBar(double mouseX, double mouseY) {
            int yOffset = getScrollYOffset();
            return mouseX >= this.scrollPosX && mouseX <= this.scrollPosX + 6
                    && mouseY >= this.scrollPosY + yOffset && mouseY <= this.scrollPosY + yOffset + 27;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= this.x && mouseX <= (this.x + this.width)
                    && mouseY >= this.y && mouseY <= (this.y + this.height);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            double mouseX = event.x();
            double mouseY = event.y();
            if (isMouseOver(mouseX, mouseY)) {
                if (this.isOverScrollBar(mouseX, mouseY)) {
                    this.scrollBarClicked = true;
                    return true;
                }
                int index = (int) ((mouseY - (double) this.y) / 10.0) +
                        (int) ((this.soundNameSet.size() - 12) * scrollValue);
                if (index < this.soundNames.size()) {
                    String selectedEntry = this.soundNames.get(index);
                    com.mojang.blaze3d.platform.Window window = Minecraft.getInstance().getWindow();
                    if (!InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) &&
                        !InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                        this.selectedEntries.clear();
                        this.selectedEntries.add(selectedEntry);
                    } else {
                        if (this.selectedEntries.contains(selectedEntry)) {
                            this.selectedEntries.remove(selectedEntry);
                        } else {
                            this.selectedEntries.add(selectedEntry);
                        }
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseReleased(MouseButtonEvent event) {
            this.scrollBarClicked = false;
            return true;
        }

        @Override
        public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
            double mouseY = event.y();
            if (!this.scrollBarClicked || !this.isScrollBarEnabled()) {
                return false;
            }
            float i = this.y + 1 + 13.5f;
            float j = i + this.height - 28f;
            this.scrollValue = -(float) ((mouseY - i) / (i - j));
            BetterMuffling.LOG.debug("MouseY: " + mouseY + ", ScrollValue: " + this.scrollValue);
            this.scrollValue = Mth.clamp(this.scrollValue, 0f, 1f);
            return true;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
            if (!this.isScrollBarEnabled()) {
                return false;
            }
            int itemSize = this.soundNameSet.size() - 12;
            this.scrollValue = (float) (this.scrollValue - (scrollY / itemSize));
            this.scrollValue = Mth.clamp(this.scrollValue, 0f, 1f);
            return true;
        }

        private boolean focused = false;

        @Override
        public boolean isFocused() { return focused; }

        @Override
        public void setFocused(boolean focused) { this.focused = focused; }

        @Override
        @Nonnull
        public NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@Nonnull NarrationElementOutput narrationElementOutput) {
        }
    }

    private class LockIconButton extends net.minecraft.client.gui.components.LockIconButton {

        public LockIconButton(int x, int y, OnPress onPress) {
            super(x, y, onPress);
            this.setTooltip(Tooltip.create(Component.translatable("button.muffling_block.player_only")));
        }
    }
}
