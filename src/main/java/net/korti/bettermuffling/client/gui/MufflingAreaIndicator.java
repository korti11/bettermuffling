package net.korti.bettermuffling.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.korti.bettermuffling.BetterMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public class MufflingAreaIndicator {

    private final static IndicatorGui indicator = new IndicatorGui();
    private final static Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onOverlayRendering(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            final ClientPlayerEntity clientPlayer = mc.player;
            if(clientPlayer.getPersistentData().getInt("muffling_areas") > 0 &&
                    true) { // TODO: Replace true with config check
                indicator.drawIndicator();
            }
        }
    }

    private static class IndicatorGui extends AbstractGui {

        private final ResourceLocation INDICATOR_ICON =
                new ResourceLocation(BetterMuffling.MOD_ID, "textures/blocks/muffling_block.png");
        private final Minecraft mc = Minecraft.getInstance();

        private void drawIndicator() {
            GlStateManager.color4f(1F, 1F, 1F, 1F);
            this.mc.getTextureManager().bindTexture(INDICATOR_ICON);
            GL11.glPushMatrix();
            GL11.glScalef(0.1F, 0.1F, 0.1F);
            this.blit(25, 25, 0, 0, 256, 256);
            GL11.glPopMatrix();
        }
    }

}
