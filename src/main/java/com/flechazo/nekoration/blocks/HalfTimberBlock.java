package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.common.VanillaCompat;
import com.flechazo.nekoration.items.HalfTimberBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class HalfTimberBlock extends Block {
    public static final IntegerProperty COLOR0 = BlockStateProperties.LEVEL;
    public static final IntegerProperty COLOR1 = BlockStateProperties.AGE_15;

    public HalfTimberBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR0, 2).setValue(COLOR1, 14));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
        s.add(COLOR0, COLOR1);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (world.isClientSide) {
            return (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem()) || VanillaCompat.RAW_COLOR_ITEMS.containsKey(itemStack.getItem())) ?
                    InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (VanillaCompat.RAW_COLOR_ITEMS.containsKey(itemStack.getItem())) {
            world.setBlock(pos, state.setValue(COLOR1, VanillaCompat.RAW_COLOR_ITEMS.get(itemStack.getItem())), 3);
            return InteractionResult.SUCCESS;
        }
        if (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem())) {
            world.setBlock(pos, state.setValue(COLOR0, VanillaCompat.COLOR_ITEMS.get(itemStack.getItem())), 3);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        if (stack.getItem() instanceof HalfTimberBlockItem)
            return this.defaultBlockState().setValue(COLOR0, HalfTimberBlockItem.getColor0(stack).getNBTId()).setValue(COLOR1, HalfTimberBlockItem.getColor1(stack).getNBTId());
        return this.defaultBlockState();
    }

    private ItemStack createColoredStack(BlockState state) {
        ItemStack stack = new ItemStack(this.asItem());
        HalfTimberBlockItem.setColor0(stack, NekoColors.EnumWoodenColor.getColorEnumFromID(state.getValue(COLOR0).byteValue()));
        HalfTimberBlockItem.setColor1(stack, NekoColors.EnumNekoColor.getColorEnumFromID(state.getValue(COLOR1).byteValue()));
        return stack;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return createColoredStack(state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && !player.isCreative()) {
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, createColoredStack(state));
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }
}