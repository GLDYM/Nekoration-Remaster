package com.flechazo.nekoration.items;

import com.flechazo.nekoration.entities.WallPaperEntity;
import com.flechazo.nekoration.entities.WallPaperEntity.Part;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class WallPaperItem extends Item {
    public WallPaperItem(Properties settings) {
        super(settings);
    }

    public InteractionResult useOn(UseOnContext ctx) {
        BlockPos blockpos = ctx.getClickedPos();
        Direction direction = ctx.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();
        if (player != null && !this.mayPlace(player, direction, stack, blockpos1)) {
            return InteractionResult.FAIL;
        } else {
            Level world = ctx.getLevel();
            WallPaperEntity wallpaper;
            // First try placing a 1x2 one...
            wallpaper = new WallPaperEntity(world, blockpos1, direction, stack, Part.FULL);
            wallpaper.setPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());

            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null) {
                EntityType.updateCustomEntityTag(world, player, wallpaper, compoundnbt);
            }

            if (wallpaper.survives()) {
                if (!world.isClientSide) {
                    wallpaper.playPlacementSound();
                    world.addFreshEntity(wallpaper);
                }
                stack.shrink(1);
                return InteractionResult.sidedSuccess(world.isClientSide);
            } else {
                // Then try a 1x1 one(half banner)...
                wallpaper.setPart(Part.UPPER);
                // We need to set the position again to recalculate the hitbox...
                wallpaper.setPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                if (wallpaper.survives()) {
                    if (!world.isClientSide) {
                        wallpaper.playPlacementSound();
                        world.addFreshEntity(wallpaper);
                    }
                    stack.shrink(1);
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player player, Direction dir, ItemStack stack, BlockPos pos) {
        return !dir.getAxis().isVertical() && player.mayUseItemAt(pos, dir, stack);
    }

    public boolean hasTag(ItemStack stack) {
        return stack.getTagElement("BlockEntityTag") != null;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack), (Component.translatable("color.nekoration." + (hasTag(stack) ? getColor(stack).getSerializedName() : "blank"))).getString());
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> texts, TooltipFlag flag) {
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, texts);
    }

    public static DyeColor getColor(ItemStack stack) {
        return DyeColor.byId(stack.getOrCreateTagElement("BlockEntityTag").getInt("Base"));
    }
}
