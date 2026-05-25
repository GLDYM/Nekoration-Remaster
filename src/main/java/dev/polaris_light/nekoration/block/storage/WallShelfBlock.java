package dev.polaris_light.nekoration.block.storage;

import com.mojang.serialization.MapCodec;
import dev.polaris_light.nekoration.api.block.HorizontalConnection;
import dev.polaris_light.nekoration.api.block.NekorationBlockStateProperties;
import dev.polaris_light.nekoration.block.entity.DisplayShelfBlockEntity;
import dev.polaris_light.nekoration.init.item.ColoredFurnitureItem;
import dev.polaris_light.nekoration.item.FurnitureColor;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallShelfBlock extends AbstractDisplayShelfBlock {
    public static final MapCodec<WallShelfBlock> CODEC = simpleCodec(WallShelfBlock::new);

    private static final Map<Direction, VoxelShape> AABBS = getAabbs(6.0D);

    public WallShelfBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION, HorizontalConnection.S0));
    }

    @Override
    public MapCodec<? extends WallShelfBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NekorationBlockStateProperties.HORIZONTAL_CONNECTION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState baseState = super.getStateForPlacement(context);
        if (baseState == null) {
            return null;
        }
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

        return baseState
            .setValue(HorizontalDirectionalBlock.FACING, facing)
            .setValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION, connection);
    }

    @Override
    protected BlockState updateShape(
        BlockState state,
        Direction direction,
        BlockState neighborState,
        LevelAccessor level,
        BlockPos pos,
        BlockPos neighborPos
    ) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        if (direction != getLeftDir(facing) && direction != getRightDir(facing)) {
            return state;
        }

        boolean leftConnected = connectsTo(level.getBlockState(getLeftBlock(pos, facing)));
        boolean rightConnected = connectsTo(level.getBlockState(getRightBlock(pos, facing)));
        HorizontalConnection updated = leftConnected && rightConnected
            ? HorizontalConnection.T1
            : leftConnected ? HorizontalConnection.T2
            : rightConnected ? HorizontalConnection.T0
            : HorizontalConnection.S0;
        return state.setValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION, updated);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayShelfBlockEntity(pos, state, true, false);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ColoredFurnitureItem.get("wall_shelf", FurnitureColor.byId(state.getValue(NekorationBlockStateProperties.COLOR))).getItemStack();
    }

    private boolean connectsTo(BlockState state) {
        return state.getBlock() == this;
    }

    private HorizontalConnection nextFromNeighbor(HorizontalConnection neighborConnection, boolean fromLeft) {
        return switch (neighborConnection) {
            case S0, T0 -> fromLeft ? HorizontalConnection.D1 : HorizontalConnection.D0;
            case D1 -> HorizontalConnection.T2;
            case D0 -> HorizontalConnection.T0;
            case T1 -> fromLeft ? HorizontalConnection.T2 : HorizontalConnection.T0;
            case T2 -> HorizontalConnection.T2;
        };
    }
}
