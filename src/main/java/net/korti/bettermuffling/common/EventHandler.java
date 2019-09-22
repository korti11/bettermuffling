package net.korti.bettermuffling.common;

import net.korti.bettermuffling.BetterMuffling;
import net.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onBreaking(final PlayerEvent.BreakSpeed event) {
        final PlayerEntity player = event.getPlayer();
        final World world = player.getEntityWorld();
        final BlockPos pos = event.getPos();
        final TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileMuffling) {
            event.setCanceled(!((TileMuffling) te).canAccess(player));
        }
    }

}
