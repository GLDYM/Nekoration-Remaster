package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.common.VanillaCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DyeableDoorBlock extends DoorBlock {
    protected static final VoxelShape TALL_SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 3.0D);
    protected static final VoxelShape TALL_NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 32.0D, 16.0D);
    protected static final VoxelShape TALL_WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    protected static final VoxelShape TALL_EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 32.0D, 16.0D);

    public static final IntegerProperty COLOR = BlockStateProperties.LEVEL;
    public final boolean isTall;

    // Indexed by Direction.get2DDataValue(): S=0, W=1, N=2, E=3
    private static final VoxelShape[] CLOSED_BY_DIR = {SOUTH_AABB, WEST_AABB, NORTH_AABB, EAST_AABB};
    private static final VoxelShape[] OPEN_RIGHT_BY_DIR = {EAST_AABB, SOUTH_AABB, WEST_AABB, NORTH_AABB};
    private static final VoxelShape[] OPEN_LEFT_BY_DIR = {WEST_AABB, NORTH_AABB, EAST_AABB, SOUTH_AABB};
    private static final VoxelShape[] TALL_CLOSED_BY_DIR = {TALL_SOUTH_AABB, TALL_WEST_AABB, TALL_NORTH_AABB, TALL_EAST_AABB};
    private static final VoxelShape[] TALL_OPEN_RIGHT_BY_DIR = {TALL_EAST_AABB, TALL_SOUTH_AABB, TALL_WEST_AABB, TALL_NORTH_AABB};
    private static final VoxelShape[] TALL_OPEN_LEFT_BY_DIR = {TALL_WEST_AABB, TALL_NORTH_AABB, TALL_EAST_AABB, TALL_SOUTH_AABB};

    public DyeableDoorBlock(Properties settings, BlockSetType blockSetType) {
        this(settings, blockSetType, false);
    }

    public DyeableDoorBlock(Properties settings, BlockSetType blockSetType, boolean tall) {
        super(settings, blockSetType);
        this.registerDefaultState(this.defaultBlockState().setValue(COLOR, 14));
        isTall = tall;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
		super.createBlockStateDefinition(s);
        s.add(COLOR);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        int dirIdx = state.getValue(FACING).get2DDataValue();
        boolean closed = !state.getValue(OPEN);
        boolean rightHinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        boolean tallUpper = isTall && state.getValue(HALF) == DoubleBlockHalf.UPPER;

        if (closed) {
            return tallUpper ? TALL_CLOSED_BY_DIR[dirIdx] : CLOSED_BY_DIR[dirIdx];
        }
        return tallUpper
                ? (rightHinge ? TALL_OPEN_RIGHT_BY_DIR[dirIdx] : TALL_OPEN_LEFT_BY_DIR[dirIdx])
                : (rightHinge ? OPEN_RIGHT_BY_DIR[dirIdx] : OPEN_LEFT_BY_DIR[dirIdx]);
    }

    private Block getTallerOne(Block door) {
        if (door == ModBlocks.DOOR_1.get()) return ModBlocks.DOOR_TALL_1.get();
        if (door == ModBlocks.DOOR_2.get()) return ModBlocks.DOOR_TALL_2.get();
        if (door == ModBlocks.DOOR_3.get()) return ModBlocks.DOOR_TALL_3.get();
        return ModBlocks.DOOR_TALL_1.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem())) {
            world.setBlock(pos, state.setValue(COLOR, VanillaCompat.COLOR_ITEMS.get(itemStack.getItem())), 3);
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        if (itemStack.getItem() == Items.BONE_MEAL && !isTall) {
            growTall(state, world, pos);
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return super.use(state, world, pos, player, hand, hit);
    }

    private void growTall(BlockState state, Level world, BlockPos pos) {
        BlockState bs = getTallerOne(state.getBlock()).defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(HINGE, state.getValue(HINGE))
                .setValue(OPEN, state.getValue(OPEN))
                .setValue(POWERED, state.getValue(POWERED))
                .setValue(COLOR, state.getValue(COLOR))
                .setValue(HALF, DoubleBlockHalf.LOWER);

        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
            world.setBlock(pos, bs, 3);
            world.setBlock(pos.above(), bs.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        } else {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            world.setBlock(pos.below(), bs, 3);
            world.setBlock(pos, bs.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        }
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER,
                (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @javax.annotation.Nullable net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
        }
    }
}