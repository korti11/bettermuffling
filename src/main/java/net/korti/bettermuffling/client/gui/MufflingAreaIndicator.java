package net.korti.bettermuffling.client.gui;

import net.korti.bettermuffling.common.config.ModConfig;
import net.korti.bettermuffling.common.constant.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class MufflingAreaIndicator {

    private final static IndicatorGui indicatorGui = new IndicatorGui();
    private final static Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onOverlayRendering(final RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            final EntityPlayer player = mc.player;
            if (player.getEntityData()
                    .getCompoundTag("muffling_indicator").getBoolean("render") &&
                    ModConfig.mufflerIndicator.enable) {
                indicatorGui.drawIndicator();
            }
        }
    }

    private static class IndicatorGui extends Gui {

        private final ResourceLocation INDICATOR_ICON =
                new ResourceLocation(ModInfo.MOD_ID, "textures/blocks/muffling_block.png");

        private final Minecraft mc = Minecraft.getMinecraft();

        private void drawIndicator() {
            GlStateManager.color(1F, 1F, 1F, 1F);
            this.mc.getTextureManager().bindTexture(INDICATOR_ICON);
            GL11.glPushMatrix();
            GL11.glScalef(0.1F, 0.1F, 0.1F);
            this.drawTexturedModalRect(ModConfig.mufflerIndicator.x, ModConfig.mufflerIndicator.y, 0, 0, 256, 256);
            GL11.glPopMatrix();
        }

    }
}
