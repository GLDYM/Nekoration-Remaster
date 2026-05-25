package dev.polaris_light.nekoration.block.furniture;

import com.mojang.serialization.MapCodec;
import dev.polaris_light.nekoration.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChairBlock extends HorizontalDirectionalBlock implements SeatBlock {
    public static final MapCodec<ChairBlock> CODEC = simpleCodec(ChairBlock::new);

    private static final double SIDE_SPACE = 1.0D;
    private static final double SEAT_HEIGHT = 9.0D;
    private static final double BACKREST_HEIGHT = 24.0D;
    private static final double BACKREST_THICKNESS = 3.0D;
    private static final double SEAT_Y_OFFSET = (SEAT_HEIGHT + 1.0D) / 16.0D;

    private static final VoxelShape SEAT_SHAPE = Block.box(
        SIDE_SPACE, 0.0D, SIDE_SPACE, 16.0D - SIDE_SPACE, SEAT_HEIGHT, 16.0D - SIDE_SPACE
    );
    private static final VoxelShape NORTH_SHAPE = Shapes.or(
        SEAT_SHAPE,
        Block.box(SIDE_SPACE, 0.0D, 16.0D - BACKREST_THICKNESS, 16.0D - SIDE_SPACE, BACKREST_HEIGHT, 16.0D - SIDE_SPACE)
    );
    private static final VoxelShape SOUTH_SHAPE = Shapes.or(
        SEAT_SHAPE,
        Block.box(SIDE_SPACE, 0.0D, SIDE_SPACE, 16.0D - SIDE_SPACE, BACKREST_HEIGHT, BACKREST_THICKNESS)
    );
    private static final VoxelShape WEST_SHAPE = Shapes.or(
        SEAT_SHAPE,
        Block.box(16.0D - BACKREST_THICKNESS, 0.0D, SIDE_SPACE, 16.0D - SIDE_SPACE, BACKREST_HEIGHT, 16.0D - SIDE_SPACE)
    );
    private static final VoxelShape EAST_SHAPE = Shapes.or(
        SEAT_SHAPE,
        Block.box(SIDE_SPACE, 0.0D, SIDE_SPACE, BACKREST_THICKNESS, BACKREST_HEIGHT, 16.0D - SIDE_SPACE)
    );

    public ChairBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<? extends ChairBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult
    ) {
        return SeatEntity.seatPlayer(level, pos, SEAT_Y_OFFSET, player);
    }
}
