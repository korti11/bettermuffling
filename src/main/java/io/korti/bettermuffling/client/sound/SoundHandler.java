package io.korti.bettermuffling.client.sound;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.ClientProxy;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import io.korti.bettermuffling.common.util.MathHelper;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BetterMuffling.MOD_ID)
public final class SoundHandler {

    @SubscribeEvent
    public static void onSoundPlaying(final PlaySoundEvent event) {
        final SoundInstance sound = event.getSound();
        final SoundSource category = sound.getSource();
        final BlockPos soundPos = new BlockPos(sound.getX(), sound.getY(), sound.getZ());
        MufflingCache.getCache().forEach((entry) -> {
            final BlockPos pos = entry.getKey();
            final short range = entry.getValue().getRange();
            if(MathHelper.isInRange(soundPos, pos, range)){
                final TileMuffling tileMuffling = (TileMuffling) ClientProxy.getWorld().getBlockEntity(pos);
                if(tileMuffling != null && event.getEngine() != null) {
                    if(tileMuffling.muffleSound(category, event.getName())) {
                        sound.resolve(event.getEngine().soundManager); // Why? Idk :D
                        final float soundLevel = tileMuffling.getSoundLevel(category);
                        final SoundInstance newSound = new SimpleSoundInstance(sound.getLocation(), category,
                                sound.getVolume() * soundLevel, sound.getPitch(), sound.isLooping(), sound.getDelay(),
                                sound.getAttenuation(), sound.getX(), sound.getY(), sound.getZ(), false
                        );
                        event.setSound(newSound);
                    }
                    if (tileMuffling.isListening()) {
                        tileMuffling.addSoundName(category, event.getName());
                    }
                }
            }
        });
    }
}
