package net.korti.bettermuffling.client.event;

import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Map;

public class SoundLevelUpdateEvent extends Event {

    private final Map<SoundCategory, Float> soundLevels;

    public SoundLevelUpdateEvent(Map<SoundCategory, Float> soundLevels) {
        this.soundLevels = soundLevels;
    }

    private float getSoundLevel(SoundCategory category) {
        return this.soundLevels.getOrDefault(category, 1F);
    }
}
