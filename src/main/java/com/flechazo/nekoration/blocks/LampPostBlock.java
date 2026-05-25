package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.blocks.states.LampPostType;
import com.flechazo.nekoration.blocks.states.ModStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;

public class LampPostBlock extends CrossCollisionBlock {
    private final VoxelShape[] occlusionByIndex;

    public static final EnumProperty<LampPostType> TYPE = ModStateProperties.LAMP_POST_TYPE;

    public static final VoxelShape SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0d, 12.0D);

    public LampPostBlock(Properties settings) {
        super(2.0F, 2.0F, 16.0F, 16.0F, 16.0F, settings);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(NORTH, false).setValue(EAST, false)
                        .setValue(SOUTH, false).setValue(WEST, false)
                        .setValue(WATERLOGGED, false).setValue(TYPE, LampPostType.BASE));
        this.occlusionByIndex = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
        s.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, TYPE);
    }

    public boolean canConnect(BlockState state, boolean neighborIsFullSquare, Direction dir) {
        return !isExceptionForConnection(state) && neighborIsFullSquare
                || state.getBlock() instanceof LampPostBlock;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return this.occlusionByIndex[this.getAABBIndex(state)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return this.getShape(state, world, pos, ctx);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState,
                                  LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        if (direction.getAxis().isHorizontal()) {
            return state.setValue(PROPERTY_BY_DIRECTION.get(direction),
                    canConnect(newState, newState.isFaceSturdy(world, posFrom, direction.getOpposite()),
                            direction.getOpposite()));
        }
        return state.setValue(TYPE, computeType(state, direction, newState));
    }

    private LampPostType computeType(BlockState state, Direction direction, BlockState newState) {
        LampPostType current = state.getValue(TYPE);
        if (direction == Direction.UP) {
            if (current == LampPostType.TOP && newState.getBlock() instanceof LampPostBlock) {
                return LampPostType.POLE;
            }
            if (isValidUpBlock(newState)) {
                return LampPostType.SIDE_UP;
            }
        } else if (direction == Direction.DOWN && isValidDownBlock(newState)) {
            return LampPostType.SIDE_DOWN;
        }
        return current;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    public LampPostType getType(Level blockView, BlockPos pos) {
        BlockState stateD = blockView.getBlockState(pos.below());
        BlockState stateU = blockView.getBlockState(pos.above());

        if (stateD.isFaceSturdy(blockView, pos.below(), Direction.DOWN)) {
            return LampPostType.BASE;
        }
        if (stateD.getBlock() instanceof LampPostBlock
                && stateD.getValue(TYPE) != LampPostType.SIDE_UP
                && stateD.getValue(TYPE) != LampPostType.SIDE_DOWN
                && !(stateU.getBlock() instanceof LampPostBlock)) {
            return LampPostType.TOP;
        }

        boolean hasSideSupport = Arrays
                .stream(Direction.values())
                .filter(Direction.Plane.HORIZONTAL)
                .map(pos::relative)
                .map(blockView::getBlockState)
                .anyMatch(s -> s.getBlock() instanceof LampPostBlock && s.getValue(TYPE) == LampPostType.TOP
                        || s.isSolidRender(blockView, pos));

        return hasSideSupport ? LampPostType.SIDE_DOWN : LampPostType.POLE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult result) {
        if (world.isClientSide) {
            return player.getItemInHand(hand).is(Items.LEAD)
                    ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return LeadItem.bindPlayerMobs(player, world, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        FluidState fluidState = world.getFluidState(pos);

        return super.getStateForPlacement(ctx)
                .setValue(NORTH, canConnectAt(world, pos.north(), Direction.SOUTH))
                .setValue(EAST, canConnectAt(world, pos.east(), Direction.WEST))
                .setValue(SOUTH, canConnectAt(world, pos.south(), Direction.NORTH))
                .setValue(WEST, canConnectAt(world, pos.west(), Direction.EAST))
                .setValue(TYPE, this.getType(world, pos))
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    private boolean canConnectAt(Level world, BlockPos neighborPos, Direction toward) {
        BlockState neighbor = world.getBlockState(neighborPos);
        return canConnect(neighbor, neighbor.isFaceSturdy(world, neighborPos, toward), toward);
    }

    private boolean isValidUpBlock(BlockState state) {
        return state.getBlock() instanceof LanternBlock || state.getBlock() instanceof BasketBlock;
    }

    private boolean isValidDownBlock(BlockState state) {
        return state.getBlock() instanceof ChainBlock
                || state.getBlock() instanceof LanternBlock
                || state.getBlock() instanceof BasketBlock;
    }
}