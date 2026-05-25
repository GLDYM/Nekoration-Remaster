package dev.polaris_light.nekoration.block.furniture;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoundTableBlock extends Block {
    public static final MapCodec<RoundTableBlock> CODEC = simpleCodec(RoundTableBlock::new);

    private static final VoxelShape CENTER_POST = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 14.0D, 10.0D);
    private static final VoxelShape TOP_NORTH_SOUTH = Block.box(0.0D, 14.0D, 2.0D, 16.0D, 16.0D, 14.0D);
    private static final VoxelShape TOP_EAST_WEST = Block.box(2.0D, 14.0D, 0.0D, 14.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE = Shapes.or(CENTER_POST, TOP_NORTH_SOUTH, TOP_EAST_WEST);

    public RoundTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends RoundTableBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
