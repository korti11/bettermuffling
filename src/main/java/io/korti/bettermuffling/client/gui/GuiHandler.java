package io.korti.bettermuffling.client.gui;

import io.korti.bettermuffling.client.ClientProxy;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiHandler {

    public static void openMufflingGui(BlockPos pos) {
        final TileEntity te = ClientProxy.getWorld().getTileEntity(pos);
        if(te instanceof TileMuffling) {
            Minecraft.getInstance().displayGuiScreen(new MufflingBlockSimpleGui((TileMuffling) te));
        }
    }

}
