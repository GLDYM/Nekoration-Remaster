package dev.polaris_light.nekoration.block.furniture;

import com.mojang.serialization.MapCodec;
import dev.polaris_light.nekoration.api.block.DyeableHorizontalBlock;
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

public class ArmChairBlock extends DyeableHorizontalBlock implements SeatBlock {
    public static final MapCodec<ArmChairBlock> CODEC = simpleCodec(ArmChairBlock::new);

    private static final double SEAT_Y_OFFSET = 0.5D;
    private static final Map<Direction, VoxelShape> SHAPES = createShapes();

    public ArmChairBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends ArmChairBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult
    ) {
        return SeatEntity.seatPlayer(level, pos, SEAT_Y_OFFSET, player);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ColoredFurnitureItem.get("arm_chair", FurnitureColor.byId(state.getValue(NekorationBlockStateProperties.COLOR))).getItemStack();
    }

    private static Map<Direction, VoxelShape> createShapes() {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        VoxelShape north = Shapes.or(
            Block.box(1.0D, 6.0D, 1.0D, 15.0D, 8.0D, 14.0D),
            Block.box(0.9D, 5.9D, 13.0D, 3.0D, 23.0D, 15.0D),
            Block.box(13.0D, 5.9D, 13.0D, 15.1D, 23.0D, 15.0D),
            Block.box(3.0D, 9.0D, 14.0D, 13.0D, 24.0D, 15.0D),
            Block.box(0.0D, 11.0D, 3.0D, 2.0D, 13.0D, 15.1D),
            Block.box(0.0D, 7.0D, 0.9D, 2.0D, 13.0D, 3.0D),
            Block.box(14.0D, 7.0D, 0.9D, 16.0D, 13.0D, 3.0D),
            Block.box(14.0D, 11.0D, 3.0D, 16.0D, 13.0D, 15.1D),
            Block.box(13.0D, 2.0D, 3.0D, 14.0D, 6.0D, 5.0D),
            Block.box(13.0D, 2.0D, 12.0D, 14.0D, 6.0D, 14.0D),
            Block.box(2.0D, 2.0D, 12.0D, 3.0D, 6.0D, 14.0D),
            Block.box(2.0D, 2.0D, 3.0D, 3.0D, 6.0D, 5.0D),
            Block.box(1.0D, 0.0D, 1.0D, 3.0D, 2.0D, 15.0D),
            Block.box(13.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D)
        );
        shapes.put(Direction.NORTH, north);
        shapes.put(Direction.WEST, rotateShapeY(north));
        shapes.put(Direction.SOUTH, rotateShapeY(shapes.get(Direction.WEST)));
        shapes.put(Direction.EAST, rotateShapeY(shapes.get(Direction.SOUTH)));
        return shapes;
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
