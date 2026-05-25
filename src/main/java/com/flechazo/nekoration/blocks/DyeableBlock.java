package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.common.VanillaCompat;
import com.flechazo.nekoration.items.DyeableBlockItem;
import com.flechazo.nekoration.items.DyeableWoodenBlockItem;
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

public class DyeableBlock extends Block {
    public static final IntegerProperty COLOR = BlockStateProperties.LEVEL;

    public DyeableBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(COLOR, 14));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
		super.createBlockStateDefinition(s);
        s.add(COLOR);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (world.isClientSide) {
            return (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem())) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem())) {
            world.setBlock(pos, state.setValue(COLOR, VanillaCompat.COLOR_ITEMS.get(itemStack.getItem())), 3);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        if (stack.getItem() instanceof DyeableBlockItem)
            return this.defaultBlockState().setValue(COLOR, DyeableBlockItem.getColor(stack).getNBTId());
        else if (stack.getItem() instanceof DyeableWoodenBlockItem)
            return this.defaultBlockState().setValue(COLOR, DyeableWoodenBlockItem.getColor(stack).getNBTId());
        return this.defaultBlockState();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(this.asItem());
        DyeableBlockItem.setColor(stack, NekoColors.EnumNekoColor.getColorEnumFromID(state.getValue(COLOR).byteValue()));
        return stack;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide && !player.isCreative()) {
            ItemStack stack = new ItemStack(this.asItem());
            DyeableBlockItem.setColor(stack, NekoColors.EnumNekoColor.getColorEnumFromID(state.getValue(COLOR).byteValue()));
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }
}