package com.flechazo.nekoration.blocks.frames;

import com.flechazo.nekoration.blocks.DyeableHorizontalBlock;
import com.flechazo.nekoration.blocks.DyeableHorizontalConnectBlock;
import com.flechazo.nekoration.blocks.WindowBlock;
import com.flechazo.nekoration.blocks.states.FramePart;
import com.flechazo.nekoration.blocks.states.HorizontalConnection;
import com.flechazo.nekoration.blocks.states.ModStateProperties;
import com.flechazo.nekoration.common.VanillaCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

import java.util.Map;

public class DyeableWindowFrameBlock extends DyeableHorizontalBlock {
    private static final Map<Direction, VoxelShape> AABBs = getAABBs(4.0D);

    public static final IntegerProperty COLOR = BlockStateProperties.LEVEL;
    public static final EnumProperty<FramePart> PART = ModStateProperties.FRAME_PART;
    public static final BooleanProperty LEFT = ModStateProperties.LEFT;
    public static final BooleanProperty RIGHT = ModStateProperties.RIGHT;
    public final boolean connectOthers;

    public DyeableWindowFrameBlock(Properties settings) {
        super(settings);
        connectOthers = false;
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, 14));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
        s.add(COLOR, FACING, PART, LEFT, RIGHT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return AABBs.get(state.getValue(FACING));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem())) {
            if (world.isClientSide) return InteractionResult.SUCCESS;
            world.setBlock(pos, state.setValue(COLOR, VanillaCompat.COLOR_ITEMS.get(itemStack.getItem())), 3);
            return InteractionResult.CONSUME;
        }
        if (itemStack.getItem() instanceof AxeItem) {
            if (world.isClientSide) return InteractionResult.SUCCESS;
            world.setBlock(pos, state.cycle(PART), 3);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    // --- Neighbor gathering ---

    private record Neighbors(BlockState l, BlockState r, BlockState u, BlockState d,
                             BlockPos lPos, BlockPos rPos, BlockPos uPos, BlockPos dPos) {
    }

    private Neighbors gatherNeighbors(LevelAccessor world, BlockPos pos, Direction dir) {
        BlockPos lPos = getLeftBlock(pos, dir), rPos = getRightBlock(pos, dir);
        BlockPos uPos = pos.above(), dPos = pos.below();
        return new Neighbors(
                world.getBlockState(lPos), world.getBlockState(rPos),
                world.getBlockState(uPos), world.getBlockState(dPos),
                lPos, rPos, uPos, dPos);
    }

    // --- Shared frame state computation ---

    private record FrameResult(FramePart part, boolean left, boolean right) {
    }

    private FrameResult computeFrame(Neighbors n, BlockPos pos, Direction dir,
                                     LevelAccessor world, boolean defaultLeft, boolean defaultRight) {
        boolean l = isFrameOrSibling(n.l), r = isFrameOrSibling(n.r);
        boolean u = isFrameOrSibling(n.u), d = isFrameOrSibling(n.d);

        FramePart part = computePart(u, d, l, r, n.l, n.r, n.u, n.d, pos, dir, world);
        boolean[] sides = computeSides(u, d, l, r, n.l, n.r, n.u, n.d, defaultLeft, defaultRight);
        return new FrameResult(part, sides[0], sides[1]);
    }

    private FramePart computePart(boolean u, boolean d, boolean l, boolean r,
                                  BlockState sl, BlockState sr, BlockState su, BlockState sd,
                                  BlockPos pos, Direction dir, LevelAccessor world) {
        if (!u) {
            if (!d) {
                if (l && !isPart(sl, FramePart.MIDDLE)) return getPartOf(sl);
                if (r && !isPart(sr, FramePart.MIDDLE)) return getPartOf(sr);
                BlockPos behind = pos.offset(dir.getOpposite().getNormal());
                return checkWindowBlock(world.getBlockState(behind.above()))
                        ? FramePart.BOTTOM : FramePart.TOP;
            }
            return isPart(sd, FramePart.TOP) ? FramePart.BOTTOM : FramePart.TOP;
        }
        if (isPart(su, FramePart.BOTTOM)) return FramePart.TOP;
        if (!d) return FramePart.BOTTOM;
        return FramePart.MIDDLE;
    }

    private boolean[] computeSides(boolean u, boolean d, boolean l, boolean r,
                                   BlockState sl, BlockState sr, BlockState su, BlockState sd,
                                   boolean defaultLeft, boolean defaultRight) {
        boolean resl = defaultLeft, resr = defaultRight;
        if (r && l) {
            boolean mid = isPart(su, FramePart.MIDDLE) || isPart(sd, FramePart.MIDDLE);
            resl = resr = mid;
        } else if (l) {
            resr = true;
            resl = false;
        } else if (r) {
            resl = true;
            resr = false;
        } else if (u) {
            resl = getLeftOf(su);
            resr = getRightOf(su);
        } else if (d) {
            resl = getLeftOf(sd);
            resr = getRightOf(sd);
        }
        return new boolean[]{resl, resr};
    }

    // --- Placement and update ---

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.getHorizontalDirection().getOpposite();
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        var n = gatherNeighbors(world, pos, dir);
        var result = computeFrame(n, pos, dir, world, true, false);
        return super.getStateForPlacement(ctx)
                .setValue(FACING, dir).setValue(PART, result.part)
                .setValue(LEFT, result.left).setValue(RIGHT, result.right);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState,
                                  LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        Direction dir = state.getValue(FACING);
        if (direction == dir || direction == dir.getOpposite()) return state;

        var n = gatherNeighbors(world, pos, dir);
        if (posFrom.equals(n.lPos)) n = new Neighbors(newState, n.r, n.u, n.d, n.lPos, n.rPos, n.uPos, n.dPos);
        if (posFrom.equals(n.rPos)) n = new Neighbors(n.l, newState, n.u, n.d, n.lPos, n.rPos, n.uPos, n.dPos);
        if (posFrom.equals(n.uPos)) n = new Neighbors(n.l, n.r, newState, n.d, n.lPos, n.rPos, n.uPos, n.dPos);
        if (posFrom.equals(n.dPos)) n = new Neighbors(n.l, n.r, n.u, newState, n.lPos, n.rPos, n.uPos, n.dPos);

        var result = computeFrame(n, pos, dir, world, state.getValue(LEFT), state.getValue(RIGHT));
        return state.setValue(PART, result.part).setValue(LEFT, result.left).setValue(RIGHT, result.right);
    }

    // --- Block state queries ---

    private boolean isFrameOrSibling(BlockState state) {
        return state.getBlock() instanceof DyeableWindowFrameBlock
                || state.getBlock() instanceof DyeableWindowSillBlock
                || state.getBlock() instanceof DyeableWindowTopBlock;
    }

    private boolean checkWindowBlock(BlockState state) {
        return state.isAir() || state.getBlock() instanceof WindowBlock
                || state.is(Tags.Blocks.GLASS) || state.is(Tags.Blocks.GLASS_PANES);
    }

    private static boolean isPart(BlockState state, FramePart part) {
        if (part == FramePart.BOTTOM && state.getBlock() instanceof DyeableWindowSillBlock) return true;
        if (part == FramePart.TOP && state.getBlock() instanceof DyeableWindowTopBlock) return true;
        if (state.getBlock() instanceof DyeableWindowFrameBlock) return state.getValue(PART) == part;
        return false;
    }

    private static FramePart getPartOf(BlockState state) {
        if (state.getBlock() instanceof DyeableWindowSillBlock) return FramePart.BOTTOM;
        if (state.getBlock() instanceof DyeableWindowTopBlock) return FramePart.TOP;
        if (state.getBlock() instanceof DyeableWindowFrameBlock) return state.getValue(PART);
        return FramePart.MIDDLE;
    }

    private static boolean getLeftOf(BlockState state) {
        if (state.getBlock() instanceof DyeableHorizontalConnectBlock) {
            var c = state.getValue(DyeableHorizontalConnectBlock.CONNECTION);
            return c == HorizontalConnection.S0 || c == HorizontalConnection.D0 || c == HorizontalConnection.T0;
        }
        if (state.getBlock() instanceof DyeableWindowFrameBlock) return state.getValue(LEFT);
        return false;
    }

    private static boolean getRightOf(BlockState state) {
        if (state.getBlock() instanceof DyeableHorizontalConnectBlock) {
            var c = state.getValue(DyeableHorizontalConnectBlock.CONNECTION);
            return c == HorizontalConnection.S0 || c == HorizontalConnection.D1 || c == HorizontalConnection.T2;
        }
        if (state.getBlock() instanceof DyeableWindowFrameBlock) return state.getValue(RIGHT);
        return false;
    }

    // --- Direction helpers ---

    @Override
    public BlockPos getLeftBlock(BlockPos pos, Direction dir) {
        return switch (dir) {
            case NORTH -> pos.east();
            case EAST -> pos.south();
            case SOUTH -> pos.west();
            default -> pos.north();
        };
    }

    @Override
    public BlockPos getRightBlock(BlockPos pos, Direction dir) {
        return switch (dir) {
            case NORTH -> pos.west();
            case EAST -> pos.north();
            case SOUTH -> pos.east();
            default -> pos.south();
        };
    }

    @Override
    public Direction getLeftDir(Direction selfDir) {
        return switch (selfDir) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            default -> Direction.NORTH;
        };
    }

    @Override
    public Direction getRightDir(Direction selfDir) {
        return switch (selfDir) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            default -> Direction.SOUTH;
        };
    }
}