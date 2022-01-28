package io.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BetterMuffling.MOD_ID)
public class MufflingAreaIndicator {

    private final static IndicatorGui indicator = new IndicatorGui();
    private final static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onOverlayRendering(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            final LocalPlayer clientPlayer = mc.player;
            if(clientPlayer.getPersistentData().getInt("muffling_areas") > 0 &&
                    BetterMufflingConfig.CLIENT.indicatorEnable.get()) {
                indicator.drawIndicator(event.getMatrixStack());
            }
        }
    }

    private static class IndicatorGui extends GuiComponent {

        private final ResourceLocation INDICATOR_ICON =
                new ResourceLocation(BetterMuffling.MOD_ID, "textures/blocks/muffling_block.png");
        private final Minecraft mc = Minecraft.getInstance();

        private void drawIndicator(PoseStack stack) {
            final float size = (float)BetterMufflingConfig.CLIENT.size.get() / 100F;
            final int xPos = BetterMufflingConfig.CLIENT.xPos.get();
            final int yPos = BetterMufflingConfig.CLIENT.yPos.get();

            this.mc.getTextureManager().bindForSetup(INDICATOR_ICON);
            GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glScalef(size, size, size);
            this.blit(stack, xPos, yPos, 0, 0, 256, 256);
            GL11.glPopMatrix();
        }
    }

}
