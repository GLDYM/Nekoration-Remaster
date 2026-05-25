package dev.polaris_light.nekoration.api.block;

import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class DyeableHorizontalConnectBlock extends DyeableHorizontalBlock {
    public enum ConnectionType {
        DOUBLE,
        TRIPLE,
        BEAM
    }

    private static final Map<Direction, VoxelShape> AABBS = getAabbs(4.0D);

    private final ConnectionType connectionType;
    private final Set<Block> compatibleBlocks;

    protected DyeableHorizontalConnectBlock(Properties properties) {
        this(properties, ConnectionType.TRIPLE);
    }

    protected DyeableHorizontalConnectBlock(Properties properties, ConnectionType connectionType, Block... compatibleBlocks) {
        super(properties);
        this.connectionType = connectionType;
        this.compatibleBlocks = ImmutableSet.copyOf(compatibleBlocks);
        this.registerDefaultState(this.defaultBlockState().setValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION, HorizontalConnection.S0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NekorationBlockStateProperties.HORIZONTAL_CONNECTION);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        BlockState leftState = context.getLevel().getBlockState(getLeftBlock(pos, facing));
        BlockState rightState = context.getLevel().getBlockState(getRightBlock(pos, facing));

        HorizontalConnection connection = HorizontalConnection.S0;
        if (connectsTo(leftState)) {
            connection = nextFromNeighbor(leftState.getValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION), true);
        } else if (connectsTo(rightState)) {
            connection = nextFromNeighbor(rightState.getValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION), false);
        }

        return super.getStateForPlacement(context)
            .setValue(HorizontalDirectionalBlock.FACING, facing)
            .setValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION, connection);
    }

    @Override
    protected BlockState updateShape(
        BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        if (direction != getLeftDir(facing) && direction != getRightDir(facing)) {
            return state;
        }

        boolean leftConnected = connectsTo(level.getBlockState(getLeftBlock(pos, facing)));
        boolean rightConnected = connectsTo(level.getBlockState(getRightBlock(pos, facing)));
        HorizontalConnection updated = leftConnected && rightConnected
            ? HorizontalConnection.T1
            : leftConnected ? anchorLeft()
            : rightConnected ? anchorRight()
            : HorizontalConnection.S0;
        return state.setValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION, updated);
    }

    protected boolean connectsTo(BlockState state) {
        if (!(state.getBlock() instanceof DyeableHorizontalConnectBlock)) {
            return false;
        }
        return state.getBlock() == this || this.compatibleBlocks.contains(state.getBlock());
    }

    private HorizontalConnection nextFromNeighbor(HorizontalConnection neighborConnection, boolean fromLeft) {
        return switch (neighborConnection) {
            case S0, T0 -> fromLeft ? HorizontalConnection.D1 : HorizontalConnection.D0;
            case D1 -> this.connectionType == ConnectionType.DOUBLE ? HorizontalConnection.S0 : HorizontalConnection.T2;
            case D0 -> this.connectionType == ConnectionType.DOUBLE ? HorizontalConnection.S0 : HorizontalConnection.T0;
            case T1 -> fromLeft ? HorizontalConnection.T2 : HorizontalConnection.T0;
            case T2 -> this.connectionType == ConnectionType.BEAM ? HorizontalConnection.T2 : HorizontalConnection.S0;
        };
    }

    private HorizontalConnection anchorLeft() {
        return this.connectionType == ConnectionType.DOUBLE ? HorizontalConnection.D1 : HorizontalConnection.T2;
    }

    private HorizontalConnection anchorRight() {
        return this.connectionType == ConnectionType.DOUBLE ? HorizontalConnection.D0 : HorizontalConnection.T0;
    }
}
