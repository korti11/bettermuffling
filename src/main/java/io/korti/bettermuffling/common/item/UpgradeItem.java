package io.korti.bettermuffling.common.item;

import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.core.BetterMufflingBlocks;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;

public class UpgradeItem extends Item {

    public UpgradeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, Item.TooltipContext context, TooltipDisplay display, @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag flagIn) {
        final String[] lines = I18n.get("tooltip.upgrade.info").split("\n");
        Arrays.stream(lines).forEach(l -> builder.accept(Component.literal(l)));
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        final Level world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        final Player player = context.getPlayer();
        final BlockState oldBlockState = world.getBlockState(pos);
        final BlockState newBlockState = BetterMufflingBlocks.MUFFLING_BLOCK_ADVANCED.get().defaultBlockState();

        if (!world.isClientSide() && player != null
                && oldBlockState.getBlock().equals(BetterMufflingBlocks.MUFFLING_BLOCK.get())) {
            final BlockEntity oldBlockEntity = world.getBlockEntity(pos);
            if (oldBlockEntity instanceof MufflingBlockEntity oldMufflingBlockEntity) {
                world.setBlock(pos, newBlockState, 0);

                final MufflingBlockEntity newBlockEntity = (MufflingBlockEntity) world.getBlockEntity(pos);
                if (newBlockEntity != null) {
                    final CompoundTag teData = oldMufflingBlockEntity.writeMufflingData(new CompoundTag(), false);
                    newBlockEntity.readMufflingData(teData);
                    newBlockEntity.setAdvancedMode(true);

                    world.sendBlockUpdated(pos, newBlockState, newBlockState, 3);

                    player.getItemInHand(context.getHand()).grow(-1);
                }
            }
        }

        return super.useOn(context);
    }
}
