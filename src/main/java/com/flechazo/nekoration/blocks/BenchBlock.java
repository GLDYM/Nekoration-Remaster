package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.NekoConfig;
import com.flechazo.nekoration.NekoConfig.HorConnectionDir;
import com.flechazo.nekoration.blocks.states.HorizontalConnection;
import com.flechazo.nekoration.blocks.states.ModStateProperties;
import com.flechazo.nekoration.common.VanillaCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BenchBlock extends DyeableChairBlock {
    public static final EnumProperty<HorizontalConnection> CONNECTION = ModStateProperties.HONRIZONTAL_CONNECTION;

    public BenchBlock(Properties settings) {
        super(settings, 0, 12, 9, 21, 2);
        this.registerDefaultState(this.stateDefinition.any().setValue(COLOR, 14));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
        s.add(COLOR, FACING, CONNECTION);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (VanillaCompat.COLOR_ITEMS.containsKey(itemStack.getItem())) {
            if (world.isClientSide) return InteractionResult.SUCCESS;
            world.setBlock(pos, state.setValue(COLOR, VanillaCompat.COLOR_ITEMS.get(itemStack.getItem())), 3);
            return InteractionResult.CONSUME;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        return tryConnect(ctx, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState,
                                  LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        HorConnectionDir config = NekoConfig.SERVER.horConnectionDir.get();
        Direction facing = state.getValue(FACING);
        boolean fromRight = direction == getRightDir(facing)
                && (config == HorConnectionDir.LEFT2RIGHT || config == HorConnectionDir.BOTH);
        boolean fromLeft = direction == getLeftDir(facing)
                && (config == HorConnectionDir.RIGHT2LEFT || config == HorConnectionDir.BOTH);

        if ((!fromRight && !fromLeft) || !(newState.getBlock() instanceof BenchBlock)) {
            return state;
        }

        HorizontalConnection otherConn = newState.getValue(CONNECTION);
        HorizontalConnection mine = switch (otherConn) {
            case D1 -> HorizontalConnection.D0;
            case T1 -> fromRight
                    ? (world.getBlockState(getLeftBlock(pos, facing)).getBlock() instanceof BenchBlock
                    ? HorizontalConnection.T1 : HorizontalConnection.T0)
                    : (world.getBlockState(getRightBlock(pos, facing)).getBlock() instanceof BenchBlock
                    ? HorizontalConnection.T1 : HorizontalConnection.T2);
            case T2 -> HorizontalConnection.T1;
            case T0 -> HorizontalConnection.T1;
            case D0 -> HorizontalConnection.D1;
            default -> state.getValue(CONNECTION);
        };
        return state.setValue(CONNECTION, mine);
    }

    private BlockState tryConnect(BlockPlaceContext ctx, Direction facing) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        HorConnectionDir config = NekoConfig.SERVER.horConnectionDir.get();
        BlockState base = super.getStateForPlacement(ctx).setValue(FACING, facing);

        if (config == HorConnectionDir.NEITHER)
            return base.setValue(CONNECTION, HorizontalConnection.S0);

        boolean useLeft = config == HorConnectionDir.LEFT2RIGHT || config == HorConnectionDir.BOTH;
        BlockPos refPos = useLeft ? getLeftBlock(pos, facing) : getRightBlock(pos, facing);
        BlockState ref = world.getBlockState(refPos);

        boolean connect = ref.getBlock() instanceof BenchBlock;
        if (!connect && config == HorConnectionDir.BOTH) {
            refPos = getRightBlock(pos, facing);
            ref = world.getBlockState(refPos);
            connect = ref.getBlock() instanceof BenchBlock;
            useLeft = false;
        }
        if (!connect)
            return base.setValue(CONNECTION, HorizontalConnection.S0);

        HorizontalConnection conn = useLeft ? leftConnectionFrom(ref) : rightConnectionFrom(ref);
        return base.setValue(CONNECTION, conn);
    }

    private static HorizontalConnection leftConnectionFrom(BlockState ref) {
        return switch (ref.getValue(CONNECTION)) {
            case S0, D0, T0 -> HorizontalConnection.D1;
            default -> HorizontalConnection.T2;
        };
    }

    private static HorizontalConnection rightConnectionFrom(BlockState ref) {
        return switch (ref.getValue(CONNECTION)) {
            case S0, D1, T2 -> HorizontalConnection.D0;
            default -> HorizontalConnection.T0;
        };
    }
}