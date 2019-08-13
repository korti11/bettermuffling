package net.korti.bettermuffling.common.tileentity;

import net.korti.bettermuffling.client.util.MufflingCache;
import net.korti.bettermuffling.common.core.BetterMufflingTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;

import java.util.*;

public final class TileMuffling extends TileEntity {

    private final Map<SoundCategory, Float> soundLevels = new HashMap<>();
    private short range = 6;
    private UUID placer;
    private boolean placerOnly = false;

    public TileMuffling() {
        super(BetterMufflingTileEntities.MUFFLING_BLOCK);
        this.init();
    }

    private void init() {
        final Set<SoundCategory> categories = new HashSet<>(Arrays.asList(SoundCategory.values()));
        categories.remove(SoundCategory.MASTER);
        categories.remove(SoundCategory.MUSIC);

        categories.forEach(category -> this.soundLevels.put(category, 0f)); // TODO: Use min value from config.
    }

    public Float getSoundLevel(SoundCategory category) {
        return this.soundLevels.get(category);
    }

    public short getRange() {
        return this.range;
    }

    public boolean isPlacerOnly() {
        return this.placerOnly;
    }

    public boolean canAccess(PlayerEntity player) {
        return !this.placerOnly || this.placer.equals(player.getUniqueID());
    }

    public void setPlacer(final UUID placer) {
        this.placer = placer;
    }

    public void setRange(final short range) {
        this.range = range;
    }

    public void setSoundLevel(final SoundCategory category, final float volume) {
        this.soundLevels.replace(category, volume);
    }

    public void setPlacerOnly(final boolean placerOnly) {
        this.placerOnly = placerOnly;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.writeSoundLevels(compound);
        compound.putShort("range", this.range);
        compound.putUniqueId("placer", this.placer);
        compound.putBoolean("placerOnly", this.placerOnly);
        return super.write(compound);
    }

    private void writeSoundLevels(CompoundNBT compound) {
        this.soundLevels.forEach((category, level) -> compound.putFloat(category.getName(), level));
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.readSoundLevels(compound);
        this.range = compound.getShort("range");
        this.placer = compound.getUniqueId("placer");
        this.placerOnly = compound.getBoolean("placerOnly");
        // TODO: Validate with config.
    }

    private void readSoundLevels(CompoundNBT compound) {
        this.soundLevels.forEach((category, level) ->
                this.soundLevels.replace(category, level, compound.getFloat(category.getName())));
    }

    @Override
    public void onLoad() {
        if(Objects.requireNonNull(getWorld()).isRemote) {
            MufflingCache.addMufflingPos(this.getPos(), this.range);
        }
    }
}
