package net.korti.bettermuffling.common.tileentity;

import net.korti.bettermuffling.client.util.MufflingCache;
import net.korti.bettermuffling.common.core.BetterMufflingTileEntities;
import net.korti.bettermuffling.common.network.PacketHandler;
import net.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import net.korti.bettermuffling.common.network.packet.RequestMufflingUpdatePacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        this.syncToServer();
    }

    public void setSoundLevel(final SoundCategory category, final float volume) {
        this.soundLevels.replace(category, volume);
        this.syncToServer();
    }

    public void setPlacerOnly(final boolean placerOnly) {
        this.placerOnly = placerOnly;
        this.syncToServer();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.writeMufflingData(compound);
        compound.putUniqueId("placer", this.placer);
        return super.write(compound);
    }

    public void writeMufflingData(CompoundNBT compound) {
        this.writeSoundLevels(compound);
        compound.putShort("range", this.range);
        compound.putBoolean("placerOnly", this.placerOnly);
    }

    private void writeSoundLevels(CompoundNBT compound) {
        this.soundLevels.forEach((category, level) -> compound.putFloat(category.getName(), level));
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.readMufflingData(compound);
        this.placer = compound.getUniqueId("placer");
        // TODO: Validate with config.
    }

    public void readMufflingData(CompoundNBT compound) {
        this.readSoundLevels(compound);
        this.range = compound.getShort("range");
        this.placerOnly = compound.getBoolean("placerOnly");
    }

    private void readSoundLevels(CompoundNBT compound) {
        this.soundLevels.forEach((category, level) ->
                this.soundLevels.replace(category, level, compound.getFloat(category.getName())));
    }

    @Override
    public void onLoad() {
        if(Objects.requireNonNull(getWorld()).isRemote) {
            MufflingCache.addMufflingPos(this.getPos(), this.range);
            PacketHandler.send(PacketDistributor.SERVER.noArg(),
                    new RequestMufflingUpdatePacket(this.getDataHash(), this.getPos()));
        }
    }

    public void syncToClient(final RequestMufflingUpdatePacket packet, final ServerPlayerEntity player) {
        final String clientDataHash = packet.getDataHash();
        final String serverDataHash = getDataHash();
        if(!serverDataHash.equals(clientDataHash)) {
            final CompoundNBT mufflingData = new CompoundNBT();
            this.writeMufflingData(mufflingData);
            PacketHandler.send(PacketDistributor.PLAYER.with(() -> player),
                    new MufflingDataPacket(this.getPos(), mufflingData));
        }
    }

    public void syncToServer() {
        final CompoundNBT mufflingData = new CompoundNBT();
        this.writeMufflingData(mufflingData);
        PacketHandler.send(PacketDistributor.SERVER.noArg(), new MufflingDataPacket(this.getPos(), mufflingData));
    }

    private String getDataHash() {
        try {
            byte[] hashBytes = new byte[0];

            for(SoundCategory category : soundLevels.keySet()) {
                hashBytes = ArrayUtils.addAll(hashBytes, category.getName().getBytes());
                hashBytes = ArrayUtils.addAll(hashBytes, getSoundLevelBytes(category));
            }

            ArrayUtils.add(hashBytes, (byte)this.range);
            ArrayUtils.add(hashBytes, this.placerOnly ? (byte) 1 : 0);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hashBytes);

            return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Could not find MD5 algorithm!");
            return "";
        }
    }

    private byte[] getSoundLevelBytes(SoundCategory category) {
        final Float soundLevel = this.getSoundLevel(category);
        final String sSoundLevel = soundLevel.toString();
        if(sSoundLevel.length() == 1) {
            return sSoundLevel.getBytes();
        }
        return sSoundLevel.replace(".", "").substring(0, Math.min(sSoundLevel.length() - 1, 3)).getBytes();
    }
}
