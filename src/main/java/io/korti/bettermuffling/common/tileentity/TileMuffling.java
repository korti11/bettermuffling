package io.korti.bettermuffling.common.tileentity;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.core.BetterMufflingTileEntities;
import io.korti.bettermuffling.common.network.PacketHandler;
import io.korti.bettermuffling.common.network.packet.MufflingAreaEventPacket;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.network.packet.RequestMufflingUpdatePacket;
import io.korti.bettermuffling.common.util.MathHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class TileMuffling extends TileEntity implements ITickableTileEntity {

    private final Set<ServerPlayerEntity> playerCache = new HashSet<>();
    private final Map<SoundCategory, Float> soundLevels = new HashMap<>();
    private final Map<SoundCategory, SortedSet<String>> soundNames = new HashMap<>();
    private final Map<SoundCategory, Boolean> whiteList = new HashMap<>();
    private short range = 6;
    private UUID placer;
    private boolean placerOnly = false;
    private boolean advancedMode = false;
    private boolean listening = false;

    private int tickCount;

    public TileMuffling() {
        super(BetterMufflingTileEntities.MUFFLING_BLOCK);
        this.init();
    }

    private void init() {
        final Set<SoundCategory> categories = new HashSet<>(Arrays.asList(SoundCategory.values()));
        categories.remove(SoundCategory.MASTER);
        categories.remove(SoundCategory.MUSIC);

        categories.forEach(category -> {
            this.soundLevels.put(category,
                    BetterMufflingConfig.COMMON.minVolume.get().floatValue());
            this.soundNames.put(category, new TreeSet<>(String::compareTo));
            this.whiteList.put(category, false);
        });
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

    public String getPlacerName() {
        if(!Objects.requireNonNull(this.getWorld()).isRemote) {
            return this.getWorld().getServer().getPlayerProfileCache().getProfileByUUID(this.placer).getName();
        }
        return "";
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

    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

    public boolean isAdvancedMode() {
        return advancedMode;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
        this.syncToServer();
    }

    public boolean getWhiteListForCategory(SoundCategory category) {
        return this.whiteList.get(category);
    }

    public void setWhiteListForCategory(SoundCategory category, boolean flag) {
        this.whiteList.put(category, flag);
    }

    public void addSoundName(SoundCategory category, String name) {
        this.soundNames.get(category).add(name);
    }

    public SortedSet<String> getNameSet(SoundCategory category) {
        return this.soundNames.get(category);
    }

    public boolean muffleSound(SoundCategory category, String name) {
        if (getWhiteListForCategory(category)) {
            return this.soundNames.get(category).contains(name);
        } else {
            return !this.soundNames.get(category).contains(name);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.writeMufflingData(compound);
        return super.write(compound);
    }

    private CompoundNBT writeMufflingData(CompoundNBT compound) {
        return writeMufflingData(compound, false);
    }

    public CompoundNBT writeMufflingData(CompoundNBT compound, boolean writePlayerName) {
        this.writeSoundLevels(compound);
        this.writeSoundNames(compound);
        this.writeWhiteList(compound);
        compound.putShort("range", this.range);
        compound.putBoolean("placerOnly", this.placerOnly);
        compound.putUniqueId("placer", this.placer);
        compound.putBoolean("advancedMode", this.advancedMode);
        compound.putBoolean("listening", this.listening);

        if(!Objects.requireNonNull(this.world).isRemote && writePlayerName) {
            compound.putString("placerName", this.getPlacerName());
        }
        return compound;
    }

    private void writeSoundLevels(CompoundNBT compound) {
        this.soundLevels.forEach((category, level) -> compound.putFloat(category.getName(), level));
    }

    private void writeSoundNames(CompoundNBT compound) {
        if(this.advancedMode) {
            this.soundNames.forEach(((category, strings) -> {
                ListNBT list = new ListNBT();
                strings.forEach((s) -> list.add(new StringNBT(s)));
                compound.put("names_" + category.getName(), list);
            }));
        }
    }

    private void writeWhiteList(CompoundNBT compound) {
        if (this.advancedMode) {
            this.whiteList.forEach((category, b) -> compound.putBoolean("white_" + category.getName(), b));
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.readMufflingData(compound);
        validateWithConfig();
    }

    public void readMufflingData(CompoundNBT compound) {
        BetterMuffling.LOG.debug("Read muffling data.");
        this.readSoundLevels(compound);
        this.readSoundNames(compound);
        this.readWhiteList(compound);
        this.range = compound.getShort("range");
        this.placerOnly = compound.getBoolean("placerOnly");
        this.placer = compound.getUniqueId("placer");
        this.advancedMode = compound.getBoolean("advancedMode");
        this.listening = compound.getBoolean("listening");
    }

    private void readSoundLevels(CompoundNBT compound) {
        this.soundLevels.forEach((category, level) ->
                this.soundLevels.replace(category, level, compound.getFloat(category.getName())));
    }

    private void readSoundNames(CompoundNBT compound) {
        if(this.advancedMode) {
            this.soundNames.forEach(((category, strings) -> {
                ListNBT list = compound.getList("names_" + category.getName(), 8);
                strings.clear();
                list.forEach(data -> strings.add(data.getString()));
            }));
        }
    }

    private void readWhiteList(CompoundNBT compound) {
        if (this.advancedMode) {
            this.whiteList.forEach((category, aBoolean) ->
                    this.whiteList.replace(category, compound.getBoolean("white_" + category.getName())));
        }
    }

    private void validateWithConfig() {
        BetterMuffling.LOG.debug("Validating muffle data with config.");
        this.range = (short) net.minecraft.util.math.MathHelper
                .clamp(this.range, 2, BetterMufflingConfig.COMMON.maxRange.get());
        for(Map.Entry<SoundCategory, Float> soundLevel : soundLevels.entrySet()) {
            soundLevels.replace(soundLevel.getKey(),
                    net.minecraft.util.math.MathHelper
                            .clamp(soundLevel.getValue(),
                                    BetterMufflingConfig.COMMON.minVolume.get().floatValue(),
                                    BetterMufflingConfig.COMMON.maxVolume.get().floatValue()));
        }
    }

    @Override
    public void onLoad() {
        if(Objects.requireNonNull(getWorld()).isRemote) {
            MufflingCache.addMufflingPos(this.getPos(), this);
            BetterMuffling.LOG.debug("Request init muffling data from server.");
            PacketHandler.send(PacketDistributor.SERVER.noArg(),
                    new RequestMufflingUpdatePacket(this.getPos()));
        }
    }

    public void syncToClient(final RequestMufflingUpdatePacket packet, final ServerPlayerEntity player) {
        BetterMuffling.LOG.debug("Sending muffling data to the client.");
        final CompoundNBT mufflingData = new CompoundNBT();
        this.writeMufflingData(mufflingData);
        PacketHandler.send(PacketDistributor.PLAYER.with(() -> player),
                new MufflingDataPacket(this.getPos(), mufflingData));
    }

    public void syncToServer() {
        BetterMuffling.LOG.debug("Sending muffling data to the server.");
        final CompoundNBT mufflingData = new CompoundNBT();
        this.writeMufflingData(mufflingData);
        PacketHandler.send(PacketDistributor.SERVER.noArg(), new MufflingDataPacket(this.getPos(), mufflingData));
    }

    @Override
    public void tick() {
        if(!Objects.requireNonNull(this.getWorld()).isRemote && tickCount >=
                BetterMufflingConfig.COMMON.ticksIndicatorHandler.get()) {
            this.handleIndicator();
            tickCount = 0;
        }

        tickCount++;
    }

    private void handleIndicator() {
        final List<ServerPlayerEntity> playersInRange = getWorld().getEntitiesWithinAABB(ServerPlayerEntity.class,
                calcRangeAABB(), player -> {
            if(player != null) {
                final Vec3d pos = new Vec3d(this.getPos());
                final double distance = Math.sqrt(player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)));
                return MathHelper.isInRange((float) distance, this.getRange());
            }
            return false;
        });

        if(!playersInRange.isEmpty()) {
            final Stream<ServerPlayerEntity> s = playersInRange.stream();
            final Consumer<ServerPlayerEntity> send = (player) -> {
                PacketHandler
                    .send(PacketDistributor.PLAYER.with(() -> player), MufflingAreaEventPacket.PLAYER_ENTERED);
                this.playerCache.add(player);
            };
            if (this.playerCache.isEmpty()) {
                s.forEach(send);
            } else {
                s.filter(player -> !this.playerCache.contains(player)).forEach(send);
            }
        }
        if (!playerCache.isEmpty()) {
            final Stream<ServerPlayerEntity> s = new HashSet<>(this.playerCache).stream();
            final Consumer<ServerPlayerEntity> send = (player) -> {
                PacketHandler
                    .send(PacketDistributor.PLAYER.with(() -> player), MufflingAreaEventPacket.PLAYER_LEFT);
                this.playerCache.remove(player);
            };
            if (playersInRange.isEmpty()) {
                s.forEach(send);
            } else {
                s.filter(player -> !playersInRange.contains(player)).forEach(send);
            }
        }
    }

    private AxisAlignedBB calcRangeAABB() {
        final int xPos = this.getPos().getX();
        final int yPos = this.getPos().getY();
        final int zPos = this.getPos().getZ();
        final short range = this.getRange();
        return new AxisAlignedBB(xPos - range - 1, yPos - range - 1, zPos - range - 1,
                xPos + range + 1, yPos + range + 1, zPos + range + 1);
    }
}
