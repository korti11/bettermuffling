package io.korti.bettermuffling.common.item;

import io.korti.bettermuffling.BetterMuffling;
import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import io.korti.bettermuffling.common.tileentity.TileMuffling;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class UpgradeItem extends Item {

    public UpgradeItem() {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
        setRegistryName(BetterMuffling.MOD_ID, "upgrade");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        final String[] lines = I18n.get("tooltip.upgrade.info").split("\n");
        Arrays.stream(lines).forEach(l -> tooltip.add(new TextComponent(l)));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final Level world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final Player player = context.getPlayer();
        final BlockState oldBlockState = world.getBlockState(pos);
        final BlockState newBlockState = BetterMufflingBlocks.advancedMufflingBlock.defaultBlockState();

        if(!world.isClientSide && player != null
                && oldBlockState.getBlock().equals(BetterMufflingBlocks.mufflingBlock)) {
            final BlockEntity oldTe = world.getBlockEntity(pos);
            if(oldTe instanceof TileMuffling) {
                // Set new block without update the client.
                world.setBlock(pos, newBlockState, 0);

                // Get the new tile entity and copy the data from the old one to the new one
                final TileMuffling newTe = (TileMuffling) world.getBlockEntity(pos);
                final CompoundTag teData = oldTe.serializeNBT();
                newTe.deserializeNBT(teData);
                newTe.setAdvancedMode(true);

                // Notify the world of the block update
                world.sendBlockUpdated(pos, newBlockState, newBlockState, 3);

                // Reduce the upgrade stack by one
                player.getItemInHand(context.getHand()).grow(-1);
            }
        }

        return super.useOn(context);
    }
}
