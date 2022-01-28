package io.korti.bettermuffling.client.gui;

import io.korti.bettermuffling.client.ClientProxy;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiHandler {

    public static void openMufflingGui(BlockPos pos) {
        final BlockEntity te = ClientProxy.getWorld().getBlockEntity(pos);
        if(te instanceof TileMuffling) {
            TileMuffling tile = (TileMuffling) te;
            Screen screen;
            if(tile.isAdvancedMode()) {
                screen = new MufflingBlockAdvancedGui(tile);
            } else {
                screen = new MufflingBlockSimpleGui(tile);
            }
            Minecraft.getInstance().setScreen(screen);
        }
    }

}
