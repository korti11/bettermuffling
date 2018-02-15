package net.korti.bettermuffling.common.tileentity;

import net.korti.bettermuffling.common.config.ModConfig;
import net.korti.bettermuffling.common.network.PacketNetworkHandler;
import net.korti.bettermuffling.common.network.UpdateTileEntityMessage;
import net.korti.bettermuffling.common.network.UpdateTileEntityRequestMessage;
import net.korti.bettermuffling.common.util.TileCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TileMuffling extends TileEntity implements ITickable {

    private final Map<SoundCategory, Float> soundLevels = new HashMap<>();
    private int range = 6;
    private UUID placedBy;
    private boolean placerOnly = false;

    public TileMuffling() {
        init();
    }

    private void init() {
        final Set<String> categoryNames = SoundCategory.getSoundCategoryNames();
        categoryNames.remove(SoundCategory.MASTER.getName());
        categoryNames.remove(SoundCategory.MUSIC.getName());

        for (String categoryName : categoryNames) {
            final SoundCategory category = SoundCategory.getByName(categoryName);
            soundLevels.put(category, (float) ModConfig.minVolume);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        writeMufflingData(compound);
        return super.writeToNBT(compound);
    }

    public void writeMufflingData(NBTTagCompound compound) {
        for (Map.Entry<SoundCategory, Float> soundLevel : soundLevels.entrySet()) {
            compound.setFloat(soundLevel.getKey().getName(), soundLevel.getValue());
        }
        compound.setInteger("range", range);
        compound.setUniqueId("placedBy", placedBy);
        compound.setBoolean("placerOnly", placerOnly);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readMufflingData(compound);
        validateWithConfig();
    }

    public void readMufflingData(NBTTagCompound compound) {
        for (SoundCategory category : soundLevels.keySet()) {
            soundLevels.replace(category, compound.getFloat(category.getName()));
        }
        range = compound.getInteger("range");
        placedBy = compound.getUniqueId("placedBy");
        placerOnly = compound.getBoolean("placerOnly");
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
                    iSound.getSoundLocation(), category, iSound.getVolume() * soundLevel, iSound.getPitch(),
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
        return isInRange(distance);
    }

    @SideOnly(Side.CLIENT)
    private boolean isInRange(double distance) {
        return distance <= (range + 1);
    }

    private void writePos(BlockPos pos, NBTTagCompound compound) {
        compound.setInteger("pos_x", pos.getX());
        compound.setInteger("pos_y", pos.getY());
        compound.setInteger("pos_z", pos.getZ());
    }

    private BlockPos readPos(NBTTagCompound compound) {
        return new BlockPos(
                compound.getInteger("pos_x"),
                compound.getInteger("pos_y"),
                compound.getInteger("pos_z")
        );
    }

    @Override
    public void update() {
        if(getWorld().isRemote) {
            handleIndicator();
        }
    }

    @SideOnly(Side.CLIENT)
    private void handleIndicator() {
        final EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        final double distance = Math.sqrt(player.getDistanceSqToCenter(this.getPos())) + 0.5;
        if (isInRange(distance)) {
            showIndicator();
        } else {
            hideIndicator();
        }
    }

    @SideOnly(Side.CLIENT)
    private void showIndicator() {
        final EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        final NBTTagCompound entityData = player.getEntityData();
        final NBTTagCompound mufflingIndicator = entityData.getCompoundTag("muffling_indicator");
        if(!mufflingIndicator.getBoolean("render")) {
            mufflingIndicator.setBoolean("render", true);
            writePos(this.getPos(), mufflingIndicator);
            entityData.setTag("muffling_indicator", mufflingIndicator);
        }
    }

    @SideOnly(Side.CLIENT)
    public void hideIndicator() {
        final EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        final NBTTagCompound entityData = player.getEntityData();
        final NBTTagCompound mufflingIndicator = entityData.getCompoundTag("muffling_indicator");
        if(readPos(mufflingIndicator).equals(this.getPos())
                && mufflingIndicator.getBoolean("render")) {
            mufflingIndicator.setBoolean("render", false);
            writePos(BlockPos.ORIGIN, mufflingIndicator);
            entityData.setTag("muffling_indicator", mufflingIndicator);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onLoad() {
        if(getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.register(this);
            TileCache.addTileEntity(this);
            PacketNetworkHandler.sendToServer(new UpdateTileEntityRequestMessage(getPos()));
        }
    }

    //region Sync to client
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeMufflingData(compound);
        return new SPacketUpdateTileEntity(getPos(), -1, compound);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readMufflingData(pkt.getNbtCompound());
    }

    public void syncToClient() {
        if (!world.isRemote) {
            markDirty();
            final IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(getPos(), state, state, 2);
        }
    }
    //endregion

    //region Sync to server
    private void syncToServer() {
        NBTTagCompound compound = new NBTTagCompound();
        writeMufflingData(compound);
        PacketNetworkHandler.sendToServer(new UpdateTileEntityMessage(getPos(), compound));
    }

    public void onDataPacket(UpdateTileEntityMessage message) {
        readMufflingData(message.getCompound());
        syncToClient();
    }
    //endregion

    private void validateWithConfig() {
        this.range = MathHelper.clamp(this.range, 2, ModConfig.maxRange);
        for(Map.Entry<SoundCategory, Float> soundLevel : soundLevels.entrySet()) {
            soundLevels.replace(soundLevel.getKey(),
                    MathHelper.clamp(soundLevel.getValue(),
                    (float) ModConfig.minVolume,
                    (float) ModConfig.maxVolume));
        }
    }

    public float getSoundLevel(SoundCategory soundCategory) {
        return this.soundLevels.get(soundCategory);
    }

    public int getRange() {
        return range;
    }

    public void updateSoundLevel(SoundCategory soundCategory, float volume) {
        this.soundLevels.replace(soundCategory, volume);
        this.syncToServer();
    }

    public void updateRange(int range) {
        this.range = range;
        this.syncToServer();
    }

    public void setPlacedBy(UUID placedBy) {
        this.placedBy = placedBy;
    }

    public boolean getPlacerOnly() {
        return this.placerOnly;
    }

    public boolean switchPlacerOnly() {
        this.placerOnly = !this.placerOnly;
        this.syncToServer();
        return this.placerOnly;
    }

    public boolean isPlayerAllowedToOpen(EntityPlayer player) {
        return !this.placerOnly || this.placedBy.equals(player.getUniqueID());
    }
}
