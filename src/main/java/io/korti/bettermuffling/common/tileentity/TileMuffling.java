package io.korti.bettermuffling.common.tileentity;

import com.mojang.authlib.GameProfile;
import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.client.util.MufflingCache;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import io.korti.bettermuffling.common.core.BetterMufflingTileEntities;
import io.korti.bettermuffling.common.network.PacketHandler;
import io.korti.bettermuffling.common.network.packet.MufflingAreaEventPacket;
import io.korti.bettermuffling.common.network.packet.MufflingDataPacket;
import io.korti.bettermuffling.common.network.packet.RequestMufflingUpdatePacket;
import io.korti.bettermuffling.common.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class TileMuffling extends BlockEntity {

    private final Set<ServerPlayer> playerCache = new HashSet<>();
    private final Map<SoundSource, Float> soundLevels = new HashMap<>();
    private final Map<SoundSource, SortedSet<String>> soundNames = new HashMap<>();
    private final Map<SoundSource, Boolean> whiteList = new HashMap<>();
    private short range = 6;
    private UUID placer;
    private SoundSource selectedCategory = SoundSource.RECORDS;
    private boolean placerOnly = false;
    private boolean advancedMode = false;
    private boolean listening = false;

    private int tickCount;

    public TileMuffling(BlockPos pos, BlockState blockState) {
        super(BetterMufflingTileEntities.MUFFLING_BLOCK, pos, blockState);
        this.init();
    }

    private void init() {
        final Set<SoundSource> categories = new HashSet<>(Arrays.asList(SoundSource.values()));
        categories.remove(SoundSource.MASTER);
        categories.remove(SoundSource.MUSIC);

        categories.forEach(category -> {
            this.soundLevels.put(category,
                    BetterMufflingConfig.COMMON.minVolume.get().floatValue());
            this.soundNames.put(category, new TreeSet<>(String::compareTo));
            this.whiteList.put(category, false);
        });
    }

    public Float getSoundLevel(SoundSource category) {
        return this.soundLevels.get(category);
    }

    public short getRange() {
        return this.range;
    }

    public boolean isPlacerOnly() {
        return this.placerOnly;
    }

    public boolean canAccess(Player player) {
        return !this.placerOnly || this.placer.equals(player.getUUID());
    }

    public void setPlacer(final UUID placer) {
        this.placer = placer;
    }

    public String getPlacerName() {
        if(!Objects.requireNonNull(this.getLevel()).isClientSide) {
            Optional<GameProfile> gameProfile = this.getLevel().getServer().getProfileCache().get(this.placer);
            if (gameProfile.isPresent()) {
                return gameProfile.get().getName();
            }
        }
        return "";
    }

    public void setRange(final short range) {
        this.range = range;
        this.syncToServer();
    }

    public void setSoundLevel(final SoundSource category, final float volume) {
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

    public boolean getWhiteListForCategory(SoundSource category) {
        return this.whiteList.get(category);
    }

    public void setWhiteListForCategory(SoundSource category, boolean flag) {
        this.whiteList.put(category, flag);
    }

    public void addSoundName(SoundSource category, String name) {
        this.soundNames.get(category).add(name);
    }

    public SortedSet<String> getNameSet(SoundSource category) {
        return this.soundNames.get(category);
    }

    public boolean muffleSound(SoundSource category, String name) {
        if(!soundLevels.containsKey(category)) {
            return false;
        }
        if (getWhiteListForCategory(category)) {
            return this.soundNames.get(category).contains(name);
        } else {
            return !this.soundNames.get(category).contains(name);
        }
    }

    public SoundSource getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(SoundSource selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        this.writeMufflingData(compoundTag);
        return compoundTag;
    }

    private CompoundTag writeMufflingData(CompoundTag compound) {
        return writeMufflingData(compound, false);
    }

    public CompoundTag writeMufflingData(CompoundTag compound, boolean writePlayerName) {
        this.writeSoundLevels(compound);
        this.writeSoundNames(compound);
        this.writeWhiteList(compound);
        compound.putShort("range", this.range);
        compound.putBoolean("placerOnly", this.placerOnly);
        compound.putUUID("placer", this.placer);
        compound.putBoolean("advancedMode", this.advancedMode);
        compound.putBoolean("listening", this.listening);
        compound.putShort("selectedCategory", (short) this.selectedCategory.ordinal());

        if(!Objects.requireNonNull(this.level).isClientSide && writePlayerName) {
            compound.putString("placerName", this.getPlacerName());
        }
        return compound;
    }

    private void writeSoundLevels(CompoundTag compound) {
        this.soundLevels.forEach((category, level) -> compound.putFloat(category.getName(), level));
    }

    private void writeSoundNames(CompoundTag compound) {
        if(this.advancedMode) {
            this.soundNames.forEach(((category, strings) -> {
                ListTag list = new ListTag();
                strings.forEach((s) -> list.add(StringTag.valueOf(s)));
                compound.put("names_" + category.getName(), list);
            }));
        }
    }

    private void writeWhiteList(CompoundTag compound) {
        if (this.advancedMode) {
            this.whiteList.forEach((category, b) -> compound.putBoolean("white_" + category.getName(), b));
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        readMufflingData(nbt);
        validateWithConfig();
    }

    public void readMufflingData(CompoundTag compound) {
        BetterMuffling.LOG.debug("Read muffling data.");
        this.advancedMode = compound.getBoolean("advancedMode");
        this.readSoundLevels(compound);
        this.readSoundNames(compound);
        this.readWhiteList(compound);
        this.range = compound.getShort("range");
        this.placerOnly = compound.getBoolean("placerOnly");
        if(compound.hasUUID("placer")) {
            this.placer = compound.getUUID("placer");
        }
        this.listening = compound.getBoolean("listening");
        this.selectedCategory = SoundSource
                .values()[net.minecraft.util.Mth
                .clamp(compound.getShort("selectedCategory"), SoundSource.RECORDS.ordinal(), SoundSource.VOICE.ordinal())];
    }

    private void readSoundLevels(CompoundTag compound) {
        this.soundLevels.forEach((category, level) ->
                this.soundLevels.replace(category, level, compound.getFloat(category.getName())));
    }

    private void readSoundNames(CompoundTag compound) {
        if(this.advancedMode) {
            this.soundNames.forEach(((category, strings) -> {
                ListTag list = compound.getList("names_" + category.getName(), 8);
                strings.clear();
                list.forEach(data -> strings.add(data.getAsString()));
            }));
        }
    }

    private void readWhiteList(CompoundTag compound) {
        if (this.advancedMode) {
            this.whiteList.forEach((category, aBoolean) ->
                    this.whiteList.replace(category, compound.getBoolean("white_" + category.getName())));
        }
    }

    private void validateWithConfig() {
        BetterMuffling.LOG.debug("Validating muffle data with config.");
        this.range = (short) net.minecraft.util.Mth
                .clamp(this.range, 2, BetterMufflingConfig.COMMON.maxRange.get());
        for(Map.Entry<SoundSource, Float> soundLevel : soundLevels.entrySet()) {
            soundLevels.replace(soundLevel.getKey(),
                    net.minecraft.util.Mth
                            .clamp(soundLevel.getValue(),
                                    BetterMufflingConfig.COMMON.minVolume.get().floatValue(),
                                    BetterMufflingConfig.COMMON.maxVolume.get().floatValue()));
        }
    }

    @Override
    public void onLoad() {
        if(Objects.requireNonNull(getLevel()).isClientSide) {
            MufflingCache.addMufflingPos(this.getBlockPos(), this);
            BetterMuffling.LOG.debug("Request init muffling data from server.");
            PacketHandler.send(PacketDistributor.SERVER.noArg(),
                    new RequestMufflingUpdatePacket(this.getBlockPos()));
        }
    }

    public void syncToAllClients() {
        BetterMuffling.LOG.debug("Sending muffling data to all data.");
        final CompoundTag mufflingData = new CompoundTag();
        this.writeMufflingData(mufflingData);
        PacketHandler.send(PacketDistributor.ALL.noArg(), new MufflingDataPacket(this.worldPosition, mufflingData));
    }

    public void syncToClient(final RequestMufflingUpdatePacket packet, final ServerPlayer player) {
        BetterMuffling.LOG.debug("Sending muffling data to the client.");
        final CompoundTag mufflingData = new CompoundTag();
        this.writeMufflingData(mufflingData);
        PacketHandler.send(PacketDistributor.PLAYER.with(() -> player),
                new MufflingDataPacket(this.getBlockPos(), mufflingData));
    }

    public void syncToServer() {
        BetterMuffling.LOG.debug("Sending muffling data to the server.");
        final CompoundTag mufflingData = new CompoundTag();
        this.writeMufflingData(mufflingData);
        PacketHandler.send(PacketDistributor.SERVER.noArg(), new MufflingDataPacket(this.getBlockPos(), mufflingData));
    }

    private void handleIndicator() {
        final List<ServerPlayer> playersInRange = getLevel().getEntitiesOfClass(ServerPlayer.class,
                calcRangeAABB(), player -> {
            if(player != null) {
                final Vec3 pos = Vec3.atLowerCornerOf(this.getBlockPos());
                final double distance = Math.sqrt(player.distanceToSqr(pos.add(0.5D, 0.5D, 0.5D)));
                return MathHelper.isInRange((float) distance, this.getRange());
            }
            return false;
        });

        if(!playersInRange.isEmpty()) {
            final Stream<ServerPlayer> s = playersInRange.stream();
            final Consumer<ServerPlayer> send = (player) -> {
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
            final Stream<ServerPlayer> s = new HashSet<>(this.playerCache).stream();
            final Consumer<ServerPlayer> send = (player) -> {
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

    private AABB calcRangeAABB() {
        final int xPos = this.getBlockPos().getX();
        final int yPos = this.getBlockPos().getY();
        final int zPos = this.getBlockPos().getZ();
        final short range = this.getRange();
        return new AABB(xPos - range - 1, yPos - range - 1, zPos - range - 1,
                xPos + range + 1, yPos + range + 1, zPos + range + 1);
    }
}
