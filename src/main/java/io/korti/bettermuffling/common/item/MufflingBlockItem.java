package io.korti.bettermuffling.common.item;

import com.mojang.blaze3d.platform.InputConstants;
import io.korti.bettermuffling.common.block.MufflingBlock;
import io.korti.bettermuffling.common.blockentity.MufflingBlockEntity;
import io.korti.bettermuffling.common.config.BetterMufflingConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

public class MufflingBlockItem extends BlockItem {

    public MufflingBlockItem(MufflingBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable TooltipContext context, TooltipDisplay display,
                                @Nonnull Consumer<Component> builder, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, builder, flagIn);
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;
        CompoundTag tag = customData.copyTag();
        CompoundTag mufflingData = tag.getCompound("tileData").orElse(null);
        if (mufflingData != null && BetterMufflingConfig.CLIENT.tooltipEnable.get()) {
            com.mojang.blaze3d.platform.Window window = Minecraft.getInstance().getWindow();
            boolean shift = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                    InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
            if (shift) {
                mufflingData.getString("placerName").ifPresent(name ->
                        builder.accept(Component.literal("Owner: " + ChatFormatting.GRAY + name + ChatFormatting.RESET)));
                builder.accept(Component.literal(
                        I18n.get("button.muffling_block.range") + ": " +
                                ChatFormatting.GRAY + mufflingData.getShortOr("range", (short) 0) + ChatFormatting.RESET));
                Arrays.stream(SoundSource.values())
                        .filter(category -> !MufflingBlockEntity.IGNORED_CATEGORIES.contains(category))
                        .forEach(category -> {
                            final String categoryName =
                                    I18n.get("soundCategory." + category.getName()) + ": ";
                            final float value = mufflingData.getFloatOr(category.getName(), 0f);
                            final String categoryValue = value == 0.0F ? I18n.get("options.off") :
                                    (int) (value * 100) + "%";
                            builder.accept(Component.literal(categoryName +
                                    ChatFormatting.GRAY + categoryValue + ChatFormatting.RESET));
                        });
            } else {
                builder.accept(Component.translatable("tooltip.hold_key.info",
                        ChatFormatting.UNDERLINE + "Shift" + ChatFormatting.RESET));
            }
        }
    }
}
