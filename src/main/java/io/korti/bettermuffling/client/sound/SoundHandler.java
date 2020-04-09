package io.korti.bettermuffling.client.sound;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.ClientProxy;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import io.korti.bettermuffling.common.util.MathHelper;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BetterMuffling.MOD_ID)
public final class SoundHandler {

    @SubscribeEvent
    public static void onSoundPlaying(final PlaySoundEvent event) {
        final ISound sound = event.getSound();
        final SoundCategory category = sound.getCategory();
        final BlockPos soundPos = new BlockPos(sound.getX(), sound.getY(), sound.getZ());
        MufflingCache.getCache().forEach((entry) -> {
            final BlockPos pos = entry.getKey();
            final short range = entry.getValue().getRange();
            if(MathHelper.isInRange(soundPos, pos, range)){
                sound.createAccessor(event.getManager().sndHandler); // Why? Idk :D
                final TileMuffling tileMuffling = (TileMuffling) ClientProxy.getWorld().getTileEntity(pos);
                if(tileMuffling == null) return;
                final float soundLevel = tileMuffling.getSoundLevel(category);
                final ISound newSound = new SimpleSound(sound.getSoundLocation(), category,
                        sound.getVolume() * soundLevel, sound.getPitch(), sound.canRepeat(), sound.getRepeatDelay(),
                        sound.getAttenuationType(), sound.getX(), sound.getY(), sound.getZ(), false
                );
                event.setResultSound(newSound);
            }
        });
    }
}
