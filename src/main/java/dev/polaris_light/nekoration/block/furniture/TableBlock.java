package dev.polaris_light.nekoration.block.furniture;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TableBlock extends Block {
    public static final MapCodec<TableBlock> CODEC = simpleCodec(TableBlock::new);

    private static final VoxelShape TOP_SHAPE = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape LEG_NORTH_WEST = Block.box(1.0D, 0.0D, 1.0D, 3.0D, 13.0D, 3.0D);
    private static final VoxelShape LEG_NORTH_EAST = Block.box(13.0D, 0.0D, 1.0D, 15.0D, 13.0D, 3.0D);
    private static final VoxelShape LEG_SOUTH_EAST = Block.box(13.0D, 0.0D, 13.0D, 15.0D, 13.0D, 15.0D);
    private static final VoxelShape LEG_SOUTH_WEST = Block.box(1.0D, 0.0D, 13.0D, 3.0D, 13.0D, 15.0D);
    private static final VoxelShape SHAPE = Shapes.or(
        TOP_SHAPE,
        LEG_NORTH_WEST,
        LEG_NORTH_EAST,
        LEG_SOUTH_EAST,
        LEG_SOUTH_WEST
    );

    public TableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends TableBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
