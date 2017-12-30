package net.korti.bettermuffling.common.tileentity;

import net.korti.bettermuffling.common.network.PacketNetworkHandler;
import net.korti.bettermuffling.common.network.UpdateTileEntityMessage;
import net.korti.bettermuffling.common.network.UpdateTileEntityRequestMessage;
import net.korti.bettermuffling.common.util.TileCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        syncWriteToNBT(compound);
        return super.writeToNBT(compound);
    }

    private void syncWriteToNBT(NBTTagCompound compound) {
        for (Map.Entry<SoundCategory, Float> soundLevel : soundLevels.entrySet()) {
            compound.setFloat(soundLevel.getKey().getName(), soundLevel.getValue());
        }
        compound.setInteger("range", range);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        syncReadFromNBT(compound);
    }

    private void syncReadFromNBT(NBTTagCompound compound) {
        for (SoundCategory category : soundLevels.keySet()) {
            soundLevels.replace(category, compound.getFloat(category.getName()));
        }
        range = compound.getInteger("range");
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
        return distance <= (range + 1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
        syncWriteToNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), -1, compound);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        syncReadFromNBT(pkt.getNbtCompound());
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
        syncWriteToNBT(compound);
        PacketNetworkHandler.sendToServer(new UpdateTileEntityMessage(getPos(), compound));
    }

    public void onDataPacket(UpdateTileEntityMessage message) {
        syncReadFromNBT(message.getCompound());
        syncToClient();
    }
    //endregion

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
}
