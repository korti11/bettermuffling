package net.korti.bettermuffling.client.gui;

import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MufflingBlockGui extends GuiScreen {

    private TileMuffling tileMuffling;
    private String title = "Muffling Options";

    public MufflingBlockGui(TileMuffling tileMuffling) {
        this.tileMuffling = tileMuffling;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.title = I18n.format("gui.muffling_block.title");

        int i = 0;
        this.buttonList.add(new RangeButton(42, this.width / 2 - 155 + i % 2 * 160,
                (this.height / 6 - 12 + 24 * (i >> 1)) + 35, tileMuffling.getRange()));
        i = i + 2;

        for (SoundCategory soundcategory : SoundCategory.values())
        {
            if (soundcategory != SoundCategory.MASTER && soundcategory != SoundCategory.MUSIC)
            {
                this.buttonList.add(new SoundButton(soundcategory.ordinal(), this.width / 2 - 155 + i % 2 * 160,
                        (this.height / 6 - 12 + 24 * (i >> 1)) + 35, soundcategory, tileMuffling.getSoundLevel(soundcategory)));
                ++i;
            }
        }
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 143, I18n.format("gui.done")));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, this.height / 6 + 10, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.enabled) {
            switch (button.id) {
                case 200: {
                    this.mc.displayGuiScreen(null);

                    if (this.mc.currentScreen == null)
                    {
                        this.mc.setIngameFocus();
                    }
                }
            }
        }
    }

    private void updateRange(int range) {
        this.tileMuffling.updateRange(range);
    }

    private void updateSoundLevel(SoundCategory category, float volume) {
        this.tileMuffling.updateSoundLevel(category, volume);
    }

    @SideOnly(Side.CLIENT)
    class RangeButton extends GuiButton {
        private final String labelName;
        public float range;
        public boolean pressed;

        public RangeButton(int buttonId, int x, int y, int range)
        {
            super(buttonId, x, y, 150, 20, "");
            this.labelName = I18n.format("button.range.name");
            this.range = (float)(range - 2) / 14F;
            this.displayString = this.labelName + ": " + this.getDisplayString();
        }

        private String getDisplayString() {
            return String.valueOf((int)(range * 14F) + 2);
        }

        /**
         * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering
         * over this button.
         */
        protected int getHoverState(boolean mouseOver)
        {
            return 0;
        }

        /**
         * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
         */
        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                if (this.pressed)
                {
                    this.range = (float)(mouseX - (this.x + 4)) / (this.width - 8);
                    this.range = MathHelper.clamp(this.range, 0.0F, 1.0F);
                    MufflingBlockGui.this.updateRange((int)((range * 14F) + 2));
                    this.displayString = this.labelName + ": " + this.getDisplayString();
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.x + (int)(this.range * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.x + (int)(this.range * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
            }
        }

        /**
         * Returns true if the mouse has been pressed on this control. Equivalent of
         * MouseListener.mousePressed(MouseEvent e).
         */
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
        {
            if (super.mousePressed(mc, mouseX, mouseY))
            {
                this.range = (float)(mouseX - (this.x + 4)) / (this.width - 8);
                this.range = MathHelper.clamp(this.range, 0.0F, 1.0F);
                MufflingBlockGui.this.updateRange((int)((range * 14F) + 2));
                this.displayString = this.labelName + ": " + this.getDisplayString();
                this.pressed = true;
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
         */
        public void mouseReleased(int mouseX, int mouseY)
        {
            this.pressed = false;
        }
    }

    @SideOnly(Side.CLIENT)
    class SoundButton extends GuiButton {
        private final SoundCategory category;
        private final String categoryName;
        public float volume = 1.0F;
        public boolean pressed;

        public SoundButton(int buttonId, int x, int y, SoundCategory category, float volume)
        {
            super(buttonId, x, y, 150, 20, "");
            this.category = category;
            this.categoryName = I18n.format("soundCategory." + category.getName());
            this.volume = volume;
            this.displayString = this.categoryName + ": " + this.getDisplayString();
        }

        private String getDisplayString() {
            return (int)(this.volume * 100.0F) + "%";
        }

        /**
         * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering
         * over this button.
         */
        protected int getHoverState(boolean mouseOver)
        {
            return 0;
        }

        /**
         * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
         */
        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {
                if (this.pressed)
                {
                    this.volume = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                    this.volume = MathHelper.clamp(this.volume, 0.0F, 1.0F);
                    MufflingBlockGui.this.updateSoundLevel(category, volume);
                    this.displayString = this.categoryName + ": " + this.getDisplayString();
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.x + (int)(this.volume * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.x + (int)(this.volume * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
            }
        }

        /**
         * Returns true if the mouse has been pressed on this control. Equivalent of
         * MouseListener.mousePressed(MouseEvent e).
         */
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
        {
            if (super.mousePressed(mc, mouseX, mouseY))
            {
                this.volume = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.volume = MathHelper.clamp(this.volume, 0.0F, 1.0F);
                MufflingBlockGui.this.updateSoundLevel(category, volume);
                this.displayString = this.categoryName + ": " + this.getDisplayString();
                this.pressed = true;
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
         */
        public void mouseReleased(int mouseX, int mouseY)
        {
            this.pressed = false;
        }
    }
}
