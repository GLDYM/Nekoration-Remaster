package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.blocks.entities.EaselMenuBlockEntity;
import com.flechazo.nekoration.items.DyeableWoodenBlockItem;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class EaselMenuBlock extends DyeableHorizontalWoodenBlock implements EntityBlock {
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    public final boolean white;

    public EaselMenuBlock(Properties settings, boolean w) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, 2));
        white = w;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EaselMenuBlockEntity(white, pos, state);
    }

    // Called just after the player places a block.
    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        // Set default text colors...
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof EaselMenuBlockEntity te) { // prevent a crash if not the right type, or is null
            final DyeColor[] colors;
            if (white) {
                colors = new DyeColor[]{DyeColor.PURPLE, DyeColor.PINK, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE, DyeColor.CYAN, DyeColor.BLUE};
            } else {
                colors = new DyeColor[]{DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE, DyeColor.WHITE};
            }
            te.setColors(colors);
            return;
        }
        LogUtils.getLogger().error("Tile Entity NOT Found!");
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!world.isClientSide()) {
            if (world.getBlockEntity(pos) instanceof EaselMenuBlockEntity blockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, blockEntity, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof Container container) {
                Containers.dropContents(world, pos, container);
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(this.asItem());
        DyeableWoodenBlockItem.setColor(stack, NekoColors.EnumWoodenColor.getColorEnumFromID(state.getValue(COLOR).byteValue()));
        return stack;
    }
}
