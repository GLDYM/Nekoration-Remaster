package dev.polaris_light.nekoration.block.furniture;

import com.mojang.serialization.MapCodec;
import dev.polaris_light.nekoration.api.block.DyeableHorizontalConnectBlock;
import dev.polaris_light.nekoration.api.block.HorizontalConnection;
import dev.polaris_light.nekoration.api.block.NekorationBlockStateProperties;
import dev.polaris_light.nekoration.entity.SeatEntity;
import dev.polaris_light.nekoration.init.item.ColoredFurnitureItem;
import dev.polaris_light.nekoration.item.FurnitureColor;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BenchBlock extends DyeableHorizontalConnectBlock implements SeatBlock {
    public static final MapCodec<BenchBlock> CODEC = simpleCodec(BenchBlock::new);

    private static final double SEAT_Y_OFFSET = 0.5D;
    private static final Map<HorizontalConnection, VoxelShape> NORTH_SHAPES = createNorthShapes();

    public BenchBlock(Properties properties) {
        super(properties, ConnectionType.TRIPLE);
    }

    @Override
    public MapCodec<? extends BenchBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape northShape = NORTH_SHAPES.get(state.getValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION));
        return rotateByFacing(northShape, state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult
    ) {
        return SeatEntity.seatPlayer(level, pos, SEAT_Y_OFFSET, player);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ColoredFurnitureItem.get("bench", FurnitureColor.byId(state.getValue(NekorationBlockStateProperties.COLOR))).getItemStack();
    }

    private static Map<HorizontalConnection, VoxelShape> createNorthShapes() {
        Map<HorizontalConnection, VoxelShape> shapes = new EnumMap<>(HorizontalConnection.class);
        shapes.put(HorizontalConnection.S0, makeShape(true, true, false, false));
        shapes.put(HorizontalConnection.D0, makeShape(false, true, true, false));
        shapes.put(HorizontalConnection.D1, makeShape(true, false, false, true));
        shapes.put(HorizontalConnection.T0, makeShape(false, true, false, false));
        shapes.put(HorizontalConnection.T1, makeShape(false, false, false, false));
        shapes.put(HorizontalConnection.T2, makeShape(true, false, false, false));
        return shapes;
    }

    private static VoxelShape makeShape(boolean leftLegs, boolean rightLegs, boolean leftPost, boolean rightPost) {
        VoxelShape shape = Shapes.or(
            Block.box(0.0D, 8.0D, 4.0D, 16.0D, 9.0D, 16.0D),
            Block.box(0.0D, 13.0D, 14.0D, 16.0D, 16.0D, 15.0D),
            Block.box(0.0D, 18.0D, 14.0D, 16.0D, 21.0D, 15.0D)
        );
        if (leftLegs) {
            shape = Shapes.or(
                shape,
                Block.box(1.0D, 0.0D, 13.0D, 3.0D, 8.0D, 15.0D),
                Block.box(1.0D, 0.0D, 5.0D, 3.0D, 8.0D, 7.0D)
            );
        }
        if (rightLegs) {
            shape = Shapes.or(
                shape,
                Block.box(13.0D, 0.0D, 13.0D, 15.0D, 8.0D, 15.0D),
                Block.box(13.0D, 0.0D, 5.0D, 15.0D, 8.0D, 7.0D)
            );
        }
        if (leftPost) {
            shape = Shapes.or(shape, Block.box(2.0D, 9.0D, 15.0D, 4.0D, 21.0D, 16.0D));
        }
        if (rightPost) {
            shape = Shapes.or(shape, Block.box(12.0D, 9.0D, 15.0D, 14.0D, 21.0D, 16.0D));
        }
        return shape;
    }

    private static VoxelShape rotateByFacing(VoxelShape shape, Direction facing) {
        VoxelShape result = shape;
        for (int i = 0; i < facing.get2DDataValue(); i++) {
            result = rotateShapeY(result);
        }
        return result;
    }

    private static VoxelShape rotateShapeY(VoxelShape shape) {
        VoxelShape[] buffer = { shape, Shapes.empty() };
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
            buffer[1] = Shapes.or(
                buffer[1],
                Block.box(
                    minZ * 16.0D,
                    minY * 16.0D,
                    (1.0D - maxX) * 16.0D,
                    maxZ * 16.0D,
                    maxY * 16.0D,
                    (1.0D - minX) * 16.0D
                )
            )
        );
        return buffer[1];
    }
}
