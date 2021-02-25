package io.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.gui.widget.*;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class MufflingBlockAdvancedGui extends MufflingBlockSimpleGui {

    private final ResourceLocation guiElements = new ResourceLocation(BetterMuffling.MOD_ID, "textures/gui/gui_elements.png");

    private ScrollList soundNamesList = null;
    private Button selectedSoundCategoryButton = null;
    private SoundCategory selectedSoundCategory = null;
    private SoundSlider activeSoundSlider = null;

    private WhiteBlackListButton activeWhiteBlackListButton = null;
    private LockIconButton lockIconButton = null;
    private ListenAudioButton listenAudioButton = null;
    private DeleteEntryButton deleteEntryButton = null;

    protected MufflingBlockAdvancedGui(TileMuffling tileMuffling) {
        super(tileMuffling, 348, 222);
    }

    @Override
    protected void initGui() {
        //super.initGui();

        this.soundNamesList = new ScrollList(this.guiLeft + 130, this.guiTop + 69, 181, 121);
        this.children.add(this.soundNamesList);

        int buttonCount = 0;
        for (final SoundCategory category : SoundCategory.values()) {
            if (category == SoundCategory.MASTER || category == SoundCategory.MUSIC) {
                continue;
            }
            SoundSlider soundSlider = this.addButton(new SoundSlider(this.guiLeft + 130, this.guiTop + 31, 180, 20,
                    tileMuffling.getSoundLevel(category).doubleValue(), category));
            soundSlider.visible = false;
            soundSlider.setListener((c, volume) -> tileMuffling.setSoundLevel(c, volume.floatValue()));

            WhiteBlackListButton whiteBlackListButton = this.addButton(new WhiteBlackListButton(this.guiLeft + 315,
                    this.guiTop + 69, 20, 20, this, (b) -> {
                tileMuffling.setWhiteListForCategory(category, !tileMuffling.getWhiteListForCategory(category));
            }));
            whiteBlackListButton.setIsWhiteList(tileMuffling.getWhiteListForCategory(category));
            whiteBlackListButton.visible = false;

            BetterButton button = this.addButton(new BetterButton(this.guiLeft + 11,
                    this.guiTop + 31 + (20 * buttonCount), 110, 20, I18n.format("soundCategory." + category.getName()),
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

                        selectedSoundCategory = category;
                        this.tileMuffling.setSelectedCategory(category);
                        this.soundNamesList.selectSoundCategory(category);
                        this.soundNamesList.resetScrollPosition();
                    }));

            if (category == tileMuffling.getSelectedCategory()) {
                this.activeSoundSlider = soundSlider;
                soundSlider.visible = true;
                this.activeWhiteBlackListButton = whiteBlackListButton;
                whiteBlackListButton.visible = true;
                this.selectedSoundCategoryButton = button;
                button.active = false;
                this.soundNamesList.selectSoundCategory(category);
                this.selectedSoundCategory = category;
            }
            buttonCount++;
        }

        RangeSlider rangeSlider = this.addButton(new RangeSlider(this.guiLeft + 11, this.guiTop + 195, 158, 20,
                this.tileMuffling.getRange()));
        rangeSlider.setUpdateListener(this.tileMuffling::setRange);

        this.addButton(new BetterButton(this.guiLeft + 11 + 158 + 8, this.guiTop + 195, 158, 20,
                I18n.format("gui.done"), (b) -> this.closeScreen()));

        this.lockIconButton = this.addButton(new LockIconButton(this.guiLeft + 315, this.guiTop + 31,
                (b) -> {
                    LockIconButton lb = (LockIconButton) b;
                    lb.setLocked(!lb.isLocked());
                    tileMuffling.setPlacerOnly(((LockIconButton) b).isLocked());
                }));
        this.lockIconButton.setLocked(tileMuffling.isPlacerOnly());

        this.listenAudioButton = this.addButton(new ListenAudioButton(this.guiLeft + 315, this.guiTop + 94,
                20, 20, this, (b) -> {
            tileMuffling.setListening(!tileMuffling.isListening());
        }));
        this.listenAudioButton.setIsListening(tileMuffling.isListening());

        this.deleteEntryButton = this.addButton(new DeleteEntryButton(this.guiLeft + 315, this.guiTop + 119,
                20, 20, this, (b) -> {
            this.soundNamesList.removeSelectedEntry();
        }));

    }

    @Override
    public void onClose() {
        super.onClose();
        this.tileMuffling.syncToServer();
    }

    @Override
    public void renderForeground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderForeground(stack, mouseX, mouseY, partialTicks);
        this.font.func_243248_b(stack, new TranslationTextComponent("label.muffling_block.sound.category"), (float) (this.guiLeft + 11), (float) (this.guiTop + 21), 4210752);
        this.font.func_243248_b(stack, new TranslationTextComponent("label.muffling_block.volume"), (float) (this.guiLeft + 130), (float) (this.guiTop + 21), 4210752);
        this.font.func_243248_b(stack, new TranslationTextComponent("label.muffling_block.sound.names"), (float) (this.guiLeft + 130), (float) (this.guiTop + 60), 4210752);
        this.soundNamesList.renderForeground(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(MatrixStack stack) {
        super.renderBackground(stack);
        this.soundNamesList.renderBackground(stack);
    }

    private class ScrollList implements IGuiEventListener {

        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final Map<SoundCategory, Pair<SortedSet<String>, List<String>>> soundCategoryNameMap = new HashMap<>();

        private float scrollValue = 0f;
        private int scrollPosX;
        private int scrollPosY;

        private SortedSet<String> soundNameSet;
        private List<String> soundNames;

        private int listWidth;
        private boolean scrollBarClicked = false;
        private Set<String> selectedEntries = new HashSet<>();

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
            for (final SoundCategory category : SoundCategory.values()) {
                if (category == SoundCategory.MASTER || category == SoundCategory.MUSIC) {
                    continue;
                }
                soundCategoryNameMap.put(category,
                        Pair.of(MufflingBlockAdvancedGui.this.tileMuffling.getNameSet(category), new LinkedList<>()));
                this.selectSoundCategory(category);
                this.updateSoundNames();
            }
            this.selectSoundCategory(SoundCategory.RECORDS);
        }

        private Screen getParent() {
            return MufflingBlockAdvancedGui.this;
        }

        public void resetScrollPosition() {
            this.scrollValue = 0;
        }

        private void selectSoundCategory(SoundCategory category) {
            Pair<SortedSet<String>, List<String>> pair = soundCategoryNameMap.get(category);
            this.soundNameSet = pair.getLeft();
            this.soundNames = pair.getRight();
            this.selectedEntries.clear();
        }

        private void drawString(MatrixStack stack, String msg, float x, float y, int color) {
            MufflingBlockAdvancedGui.this.font.drawString(stack, msg, x, y, color);
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

        public void renderForeground(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            GlStateManager.clearColor(1f, 1f, 1f, 1f);
            minecraft.getTextureManager().bindTexture(guiElements);
            int scrollBarUV = isScrollBarEnabled() ? 185 : 191;

            // Render scroll bar
            int yOffset = getScrollYOffset();
            getParent().blit(stack, this.scrollPosX, this.scrollPosY + yOffset, scrollBarUV, 0, 6, 27);

            int startValue = (int) ((this.soundNameSet.size() - 12) * scrollValue);
            int maxValue = Math.min(startValue + 12, this.soundNameSet.size());
            int elementOffset = 0;
            for (int i = startValue; i < maxValue && i >= 0 && i < this.soundNameSet.size(); i++) {
                String current = soundNames.get(i);
                if (this.selectedEntries.contains(current)) {
                    drawString(stack, current, this.x + 2, this.y + 2 + (elementOffset * 10), 16777120);
                } else {
                    drawString(stack, current, this.x + 2, this.y + 2 + (elementOffset * 10), 4210752);
                }
                elementOffset++;
            }
        }

        public void renderBackground(MatrixStack stack) {
            GlStateManager.clearColor(1f, 1f, 1f, 1f);
            minecraft.getTextureManager().bindTexture(guiElements);
            int listWidth = this.listWidth - 1;
            int halfHeight = Math.round(this.height / 2f);
            int top1 = 0;
            int top2 = 142 - halfHeight;

            // Render left end
            getParent().blit(stack, this.x, this.y, 0, top1, 1, halfHeight);
            getParent().blit(stack, this.x, this.y + halfHeight, 0, top2, 1, halfHeight + 1);

            // Render middle
            getParent().blit(stack, this.x + 1, this.y, 1, top1, listWidth, halfHeight);
            getParent().blit(stack, this.x + 1, this.y + halfHeight, 1, top2, listWidth, halfHeight + 1);

            // Render right end
            getParent().blit(stack, this.scrollPosX - 1, this.y, 177, top1, 8, halfHeight);
            getParent().blit(stack, this.scrollPosX - 1, this.y + halfHeight, 177, top2, 8, halfHeight + 1);
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
                    if (!BetterMuffling.proxy.isShiftKeyDown()) {
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
            this.scrollValue = MathHelper.clamp(this.scrollValue, 0f, 1f);
            return true;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double yOffset) {
            if (!this.isScrollBarEnabled()) {
                return false;
            }
            int itemSize = this.soundNameSet.size() - 12;
            this.scrollValue = (float) (this.scrollValue - (yOffset / itemSize));
            this.scrollValue = MathHelper.clamp(this.scrollValue, 0f, 1f);
            return true;
        }
    }

    private class LockIconButton extends net.minecraft.client.gui.widget.button.LockIconButton {

        public LockIconButton(int p_i51133_1_, int p_i51133_2_, IPressable p_i51133_3_) {
            super(p_i51133_1_, p_i51133_2_, p_i51133_3_);
        }

        @Override
        public void renderToolTip(MatrixStack stack, int mouseX, int mouseY) {
            MufflingBlockAdvancedGui.this.func_243308_b(stack,
                    Collections.singletonList(new TranslationTextComponent("button.muffling_block.player_only")),
                    mouseX, mouseY);
        }
    }
}
