package io.korti.bettermuffling.client.gui;

import io.korti.bettermuffling.client.ClientProxy;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiHandler {

    public static void openMufflingGui(BlockPos pos) {
        final BlockEntity blockEntity = ClientProxy.getWorld().getBlockEntity(pos);
        if (blockEntity instanceof MufflingBlockEntity mufflingBlockEntity) {
            Screen screen;
            if (mufflingBlockEntity.isAdvancedMode()) {
                screen = new MufflingBlockAdvancedGui(mufflingBlockEntity);
            } else {
                screen = new MufflingBlockSimpleGui(mufflingBlockEntity);
            }
            Minecraft.getInstance().setScreen(screen);
        }
    }

}
