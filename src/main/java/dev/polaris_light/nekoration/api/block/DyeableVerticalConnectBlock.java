package dev.polaris_light.nekoration.api.block;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class DyeableVerticalConnectBlock extends DyeableBlock {
    public enum ConnectionType {
        DOUBLE,
        TRIPLE,
        PILLAR
    }

    private final ConnectionType connectionType;
    private final Set<Block> compatibleBlocks;

    protected DyeableVerticalConnectBlock(Properties properties) {
        this(properties, ConnectionType.TRIPLE);
    }

    protected DyeableVerticalConnectBlock(Properties properties, ConnectionType connectionType, Block... compatibleBlocks) {
        super(properties);
        this.connectionType = connectionType;
        this.compatibleBlocks = ImmutableSet.copyOf(compatibleBlocks);
        this.registerDefaultState(this.defaultBlockState().setValue(NekorationBlockStateProperties.VERTICAL_CONNECTION, VerticalConnection.S0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NekorationBlockStateProperties.VERTICAL_CONNECTION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState below = context.getLevel().getBlockState(pos.below());
        BlockState above = context.getLevel().getBlockState(pos.above());

        VerticalConnection connection = VerticalConnection.S0;
        if (connectsTo(below)) {
            connection = nextFromNeighbor(below.getValue(NekorationBlockStateProperties.VERTICAL_CONNECTION), true);
        } else if (connectsTo(above)) {
            connection = nextFromNeighbor(above.getValue(NekorationBlockStateProperties.VERTICAL_CONNECTION), false);
        }
        return super.getStateForPlacement(context).setValue(NekorationBlockStateProperties.VERTICAL_CONNECTION, connection);
    }

    @Override
    protected BlockState updateShape(
        BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos
    ) {
        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        }

        boolean connectedBelow = connectsTo(level.getBlockState(pos.below()));
        boolean connectedAbove = connectsTo(level.getBlockState(pos.above()));
        VerticalConnection updated = connectedBelow && connectedAbove
            ? VerticalConnection.T1
            : connectedBelow ? anchorBottom()
            : connectedAbove ? anchorTop()
            : VerticalConnection.S0;
        return state.setValue(NekorationBlockStateProperties.VERTICAL_CONNECTION, updated);
    }

    protected boolean connectsTo(BlockState state) {
        if (!(state.getBlock() instanceof DyeableVerticalConnectBlock)) {
            return false;
        }
        return state.getBlock() == this || this.compatibleBlocks.contains(state.getBlock());
    }

    private VerticalConnection nextFromNeighbor(VerticalConnection neighborConnection, boolean fromBottom) {
        return switch (neighborConnection) {
            case S0, T0 -> fromBottom ? VerticalConnection.D1 : VerticalConnection.D0;
            case D1 -> this.connectionType == ConnectionType.DOUBLE ? VerticalConnection.S0 : VerticalConnection.T2;
            case D0 -> this.connectionType == ConnectionType.DOUBLE ? VerticalConnection.S0 : VerticalConnection.T0;
            case T1 -> fromBottom ? VerticalConnection.T2 : VerticalConnection.T0;
            case T2 -> this.connectionType == ConnectionType.PILLAR ? VerticalConnection.T2 : VerticalConnection.S0;
        };
    }

    private VerticalConnection anchorBottom() {
        return this.connectionType == ConnectionType.DOUBLE ? VerticalConnection.D1 : VerticalConnection.T2;
    }

    private VerticalConnection anchorTop() {
        return this.connectionType == ConnectionType.DOUBLE ? VerticalConnection.D0 : VerticalConnection.T0;
    }
}
