package dev.polaris_light.nekoration.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class DyeableDoorBlock extends DoorBlock {
    protected static final VoxelShape TALL_SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 3.0D);
    protected static final VoxelShape TALL_NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 32.0D, 16.0D);
    protected static final VoxelShape TALL_WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    protected static final VoxelShape TALL_EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 32.0D, 16.0D);

    private static final VoxelShape[] CLOSED_BY_DIR = { SOUTH_AABB, WEST_AABB, NORTH_AABB, EAST_AABB };
    private static final VoxelShape[] OPEN_RIGHT_BY_DIR = { EAST_AABB, SOUTH_AABB, WEST_AABB, NORTH_AABB };
    private static final VoxelShape[] OPEN_LEFT_BY_DIR = { WEST_AABB, NORTH_AABB, EAST_AABB, SOUTH_AABB };
    private static final VoxelShape[] TALL_CLOSED_BY_DIR = { TALL_SOUTH_AABB, TALL_WEST_AABB, TALL_NORTH_AABB, TALL_EAST_AABB };
    private static final VoxelShape[] TALL_OPEN_RIGHT_BY_DIR = { TALL_EAST_AABB, TALL_SOUTH_AABB, TALL_WEST_AABB, TALL_NORTH_AABB };
    private static final VoxelShape[] TALL_OPEN_LEFT_BY_DIR = { TALL_WEST_AABB, TALL_NORTH_AABB, TALL_EAST_AABB, TALL_SOUTH_AABB };

    private final boolean tall;

    protected DyeableDoorBlock(Properties properties, BlockSetType blockSetType) {
        this(properties, blockSetType, false);
    }

    protected DyeableDoorBlock(Properties properties, BlockSetType blockSetType, boolean tall) {
        super(blockSetType, properties);
        this.tall = tall;
        this.registerDefaultState(this.defaultBlockState().setValue(NekorationBlockStateProperties.COLOR, 14));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NekorationBlockStateProperties.COLOR);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int dirIdx = state.getValue(FACING).get2DDataValue();
        boolean closed = !state.getValue(OPEN);
        boolean rightHinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        boolean tallUpper = this.tall && state.getValue(HALF) == DoubleBlockHalf.UPPER;
        if (closed) {
            return tallUpper ? TALL_CLOSED_BY_DIR[dirIdx] : CLOSED_BY_DIR[dirIdx];
        }
        return tallUpper ? (rightHinge ? TALL_OPEN_RIGHT_BY_DIR[dirIdx] : TALL_OPEN_LEFT_BY_DIR[dirIdx])
            : (rightHinge ? OPEN_RIGHT_BY_DIR[dirIdx] : OPEN_LEFT_BY_DIR[dirIdx]);
    }

    @Override
    protected ItemInteractionResult useItemOn(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        Integer colorId = DyeableBlock.DYE_COLOR_IDS.get(stack.getItem());
        if (colorId != null) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(NekorationBlockStateProperties.COLOR, colorId), 3);
                stack.consume(1, player);
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (stack.is(Items.BONE_MEAL) && !this.tall) {
            if (!level.isClientSide) {
                growTall(state, level, pos);
                stack.consume(1, player);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult
    ) {
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    protected BlockState makeTallState(BlockState state) {
        return this.defaultBlockState()
            .setValue(FACING, state.getValue(FACING))
            .setValue(HINGE, state.getValue(HINGE))
            .setValue(OPEN, state.getValue(OPEN))
            .setValue(POWERED, state.getValue(POWERED))
            .setValue(NekorationBlockStateProperties.COLOR, state.getValue(NekorationBlockStateProperties.COLOR))
            .setValue(HALF, DoubleBlockHalf.LOWER);
    }

    protected void growTall(BlockState state, Level level, BlockPos pos) {
        BlockState tallState = makeTallState(state);
        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        level.setBlock(lowerPos, tallState, 3);
        level.setBlock(lowerPos.above(), tallState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        level.addParticle(
            ParticleTypes.EXPLOSION_EMITTER,
            lowerPos.getX() + 0.5D,
            lowerPos.getY() + 0.5D,
            lowerPos.getZ() + 0.5D,
            0.0D,
            0.0D,
            0.0D
        );
    }
}
