package io.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.widget.*;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.*;

public class MufflingBlockAdvancedGui extends MufflingBlockSimpleGui {

    private final ResourceLocation guiElements = ResourceLocation.fromNamespaceAndPath(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private ScrollList soundNamesList = null;
    private Button selectedSoundCategoryButton = null;
    private SoundSlider activeSoundSlider = null;

    private WhiteBlackListButton activeWhiteBlackListButton = null;

    protected MufflingBlockAdvancedGui(MufflingBlockEntity tileMuffling) {
        super(tileMuffling, 348, 222);
    }

    @Override
    protected void initGui() {
        this.soundNamesList = new ScrollList(this.guiLeft + 130, this.guiTop + 69, 181, 121);
        this.addWidget(this.soundNamesList);

        int buttonCount = 0;
        for (final SoundSource category : SoundSource.values()) {
            if (category == SoundSource.MASTER || category == SoundSource.MUSIC) {
                continue;
            }
            SoundSlider soundSlider = this.addRenderableWidget(new SoundSlider(this.guiLeft + 130, this.guiTop + 31, 180, 20,
                    tileMuffling.getSoundLevel(category).doubleValue(), category));
            soundSlider.visible = false;
            soundSlider.setListener((c, volume) -> tileMuffling.setSoundLevel(c, volume.floatValue()));

            WhiteBlackListButton whiteBlackListButton = this.addRenderableWidget(new WhiteBlackListButton(this.guiLeft + 315,
                    this.guiTop + 69, 20, 20, this, (b) -> tileMuffling.setWhiteListForCategory(category, !tileMuffling.getWhiteListForCategory(category))));
            whiteBlackListButton.setIsWhiteList(tileMuffling.getWhiteListForCategory(category));
            whiteBlackListButton.visible = false;

            BetterButton button = this.addRenderableWidget(new BetterButton(this.guiLeft + 11,
                    this.guiTop + 31 + (20 * buttonCount), 110, 20, I18n.get("soundCategory." + category.getName()),
                    (b) -> {
                        activeSoundSlider.visible = false;
                        activeSoundSlider = soundSlider;
                        soundSlider.visible = true;

                        activeWhiteBlackListButton.visible = false;
                        activeWhiteBlackListButton = whiteBlackListButton;
                        whiteBlackListButton.visible = true;

                        selectedSoundCategoryButton.active = true;
                        selectedSoundCategoryButton = b;
                        b.active = false;

                        this.tileMuffling.setSelectedCategory(category);
                        this.soundNamesList.selectSoundCategory(category);
                    }));

            if (category == tileMuffling.getSelectedCategory()) {
                this.activeSoundSlider = soundSlider;
                soundSlider.visible = true;
                this.activeWhiteBlackListButton = whiteBlackListButton;
                whiteBlackListButton.visible = true;
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
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderForeground(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawString(this.font, Component.translatable("label.muffling_block.sound.category"), this.guiLeft + 11, this.guiTop + 21, 4210752, false);
        guiGraphics.drawString(this.font, Component.translatable("label.muffling_block.volume"), this.guiLeft + 130, this.guiTop + 21, 4210752, false);
        guiGraphics.drawString(this.font, Component.translatable("label.muffling_block.sound.names"), this.guiLeft + 130, this.guiTop + 60, 4210752, false);
        this.soundNamesList.renderForeground(guiGraphics);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        this.soundNamesList.renderBackground(guiGraphics);
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
                if (category == SoundSource.MASTER || category == SoundSource.MUSIC) {
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

        private void drawString(GuiGraphics guiGraphics, String msg, int x, int y, int color) {
            guiGraphics.drawString(MufflingBlockAdvancedGui.this.font, msg, x, y, color, false);
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

        public void renderForeground(GuiGraphics guiGraphics) {
            int scrollBarUV = isScrollBarEnabled() ? 185 : 191;
            int yOffset = getScrollYOffset();
            guiGraphics.blit(guiElements, this.scrollPosX, this.scrollPosY + yOffset, scrollBarUV, 0, 6, 27);

            int startValue = Math.max((int) ((this.soundNameSet.size() - 12) * scrollValue), 0);
            int maxValue = Math.min(startValue + 12, this.soundNameSet.size());
            int elementOffset = 0;
            for (int i = startValue; i < maxValue && i < this.soundNameSet.size() && i < this.soundNames.size(); i++) {
                String current = soundNames.get(i);
                if (this.selectedEntries.contains(current)) {
                    drawString(guiGraphics, current, this.x + 2, this.y + 2 + (elementOffset * 10), 16777120);
                } else {
                    drawString(guiGraphics, current, this.x + 2, this.y + 2 + (elementOffset * 10), 4210752);
                }
                elementOffset++;
            }
        }

        public void renderBackground(GuiGraphics guiGraphics) {
            int listWidth = this.listWidth - 1;
            int halfHeight = Math.round(this.height / 2f);
            int top1 = 0;
            int top2 = 142 - halfHeight;

            // Render left end
            guiGraphics.blit(guiElements, this.x, this.y, 0, top1, 1, halfHeight);
            guiGraphics.blit(guiElements, this.x, this.y + halfHeight, 0, top2, 1, halfHeight + 1);
            // Render middle
            guiGraphics.blit(guiElements, this.x + 1, this.y, 1, top1, listWidth, halfHeight);
            guiGraphics.blit(guiElements, this.x + 1, this.y + halfHeight, 1, top2, listWidth, halfHeight + 1);
            // Render right end
            guiGraphics.blit(guiElements, this.scrollPosX - 1, this.y, 177, top1, 8, halfHeight);
            guiGraphics.blit(guiElements, this.scrollPosX - 1, this.y + halfHeight, 177, top2, 8, halfHeight + 1);
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
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY)) {
                if (this.isOverScrollBar(mouseX, mouseY)) {
                    this.scrollBarClicked = true;
                    return true;
                }
                int index = (int) ((mouseY - (double) this.y) / 10.0) +
                        (int) ((this.soundNameSet.size() - 12) * scrollValue);
                if (index < this.soundNames.size()) {
                    String selectedEntry = this.soundNames.get(index);
                    long windowHandle = Minecraft.getInstance().getWindow().getWindow();
                    if (!InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) &&
                        !InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT)) {
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
        public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
            this.scrollBarClicked = false;
            return true;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double xOffset, double yOffset) {
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
