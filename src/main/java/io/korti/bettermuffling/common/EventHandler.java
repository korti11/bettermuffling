package io.korti.bettermuffling.common;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterMuffling.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onBreaking(final PlayerEvent.BreakSpeed event) {
        final Player player = event.getPlayer();
        final Level world = player.getCommandSenderWorld();
        final BlockPos pos = event.getPos();
        final BlockEntity te = world.getBlockEntity(pos);

        if (te instanceof MufflingBlockEntity) {
            event.setCanceled(!((MufflingBlockEntity) te).canAccess(player));
        }
    }

}
