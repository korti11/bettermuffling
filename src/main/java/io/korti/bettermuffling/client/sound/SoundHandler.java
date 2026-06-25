package io.korti.bettermuffling.client.sound;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = BetterMuffling.MOD_ID)
public final class SoundHandler {

    private static final RandomSource RANDOM_SOURCE = RandomSource.create();

    @SubscribeEvent
    public static void onSoundPlaying(final PlaySoundEvent event) {
        final SoundInstance sound = event.getSound();
        if (sound == null) return;
        final SoundSource category = sound.getSource();
        final BlockPos soundPos = BlockPos.containing(sound.getX(), sound.getY(), sound.getZ());
        MufflingCache.getCache().forEach((entry) -> {
            final BlockPos pos = entry.getKey();
            final short range = entry.getValue().getRange();
            if (MathHelper.isInRange(soundPos, pos, range)) {
                final MufflingBlockEntity tileMuffling = (MufflingBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos);
                if (tileMuffling != null && event.getEngine() != null) {
                    final String soundName = sound.getLocation().toString();
                    if (tileMuffling.muffleSound(category, soundName)) {
                        sound.resolve(event.getEngine().soundManager);
                        final float soundLevel = tileMuffling.getSoundLevel(category);
                        final SoundInstance newSound = new SimpleSoundInstance(sound.getLocation(), category,
                                sound.getVolume() * soundLevel, sound.getPitch(), RANDOM_SOURCE, sound.isLooping(),
                                sound.getDelay(), sound.getAttenuation(), sound.getX(), sound.getY(), sound.getZ(),
                                false
                        );
                        event.setSound(newSound);
                    }
                    if (tileMuffling.isListening()) {
                        tileMuffling.addSoundName(category, soundName);
                    }
                }
            }
        });
    }
}
