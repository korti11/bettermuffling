package net.korti.bettermuffling.common.tileentity;

import net.korti.bettermuffling.common.util.TileCache;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TileMuffling extends TileEntity {

    private final Map<SoundCategory, Float> soundLevels = new HashMap<>();
    private int range = 6;

    public TileMuffling() {
        init();
    }

    private void init() {
        final Set<String> categoryNames = SoundCategory.getSoundCategoryNames();
        categoryNames.remove(SoundCategory.MASTER.getName());
        categoryNames.remove(SoundCategory.MUSIC.getName());

        for (String categoryName : categoryNames) {
            final SoundCategory category = SoundCategory.getByName(categoryName);
            soundLevels.put(category, 0.1F);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSoundPlaying(PlaySoundEvent event) {
        final ISound iSound = event.getSound();
        final SoundCategory category = iSound.getCategory();
        if(isInRange(iSound) && soundLevels.containsKey(category)) {
            iSound.createAccessor(event.getManager().sndHandler);
            final float soundLevel = soundLevels.get(category);
            final ISound newSound = new PositionedSoundRecord(
                    iSound.getSoundLocation(), category, iSound.getVolume() * soundLevel, iSound.getPitch() * soundLevel,
                    iSound.canRepeat(), iSound.getRepeatDelay(), iSound.getAttenuationType(),
                    iSound.getXPosF(), iSound.getYPosF(), iSound.getZPosF()
            );
            event.setResultSound(newSound);
        }
    }

    @SideOnly(Side.CLIENT)
    private boolean isInRange(ISound sound) {
        final double distance = Math.sqrt(
                getDistanceSq((double) sound.getXPosF(), (double) sound.getYPosF(), (double) sound.getZPosF())
        );
        return distance <= range;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onLoad() {
        super.onLoad();
        if(getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.register(this);
            TileCache.addTileEntity(this);
        }
    }
}
