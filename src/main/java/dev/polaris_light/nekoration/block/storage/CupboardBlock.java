package dev.polaris_light.nekoration.block.storage;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CupboardBlock extends AbstractDisplayShelfBlock {
    public static final MapCodec<CupboardBlock> CODEC = simpleCodec(CupboardBlock::new);
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    private static final Map<Direction, VoxelShape> AABBS = getAabbs(9.0D);
    private final boolean playSound;

    public CupboardBlock(Properties properties) {
        this(properties, true);
    }

    public CupboardBlock(Properties properties, boolean playSound) {
        super(properties);
        this.playSound = playSound;
        this.registerDefaultState(this.defaultBlockState().setValue(BOTTOM, false));
    }

    @Override
    public MapCodec<? extends CupboardBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BOTTOM);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        }
        BlockPos abovePos = context.getClickedPos().above();
        boolean hasCupboardAbove = context.getLevel().getBlockState(abovePos).getBlock() instanceof CupboardBlock;
        return state.setValue(BOTTOM, hasCupboardAbove);
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
        if (direction == Direction.UP) {
            return state.setValue(BOTTOM, neighborState.getBlock() instanceof CupboardBlock);
        }
        return state;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(HorizontalDirectionalBlock.FACING));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayShelfBlockEntity(pos, state, false, this.playSound);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ColoredFurnitureItem.get("cupboard", FurnitureColor.byId(state.getValue(NekorationBlockStateProperties.COLOR))).getItemStack();
    }
}
