package dev.polaris_light.nekoration.block.storage;

import com.mojang.serialization.MapCodec;
import dev.polaris_light.nekoration.api.block.NekorationBlockStateProperties;
import dev.polaris_light.nekoration.api.block.DyeableHorizontalBlock;
import dev.polaris_light.nekoration.block.entity.CabinetBlockEntity;
import dev.polaris_light.nekoration.init.item.ColoredFurnitureItem;
import dev.polaris_light.nekoration.item.FurnitureColor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CabinetBlock extends DyeableHorizontalBlock implements EntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private static final MapCodec<CabinetBlock> DRAWER_CODEC = simpleCodec(properties -> new CabinetBlock(properties, CabinetType.DRAWER));
    private static final MapCodec<CabinetBlock> CABINET_CODEC = simpleCodec(properties -> new CabinetBlock(properties, CabinetType.CABINET));
    private static final MapCodec<CabinetBlock> DRAWER_CHEST_CODEC = simpleCodec(properties -> new CabinetBlock(properties, CabinetType.DRAWER_CHEST));
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    private final CabinetType type;

    public CabinetBlock(Properties properties, CabinetType type) {
        super(properties);
        this.type = type;
        this.registerDefaultState(this.defaultBlockState().setValue(OPEN, false));
    }

    @Override
    public MapCodec<? extends CabinetBlock> codec() {
        return switch (this.type) {
            case DRAWER -> DRAWER_CODEC;
            case CABINET -> CABINET_CODEC;
            case DRAWER_CHEST -> DRAWER_CHEST_CODEC;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CabinetBlockEntity cabinetBlockEntity) {
            player.openMenu(cabinetBlockEntity);
            PiglinAi.angerNearbyPiglins(player, true);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CabinetBlockEntity cabinetBlockEntity) {
            cabinetBlockEntity.recheckOpen();
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state, this.type);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ColoredFurnitureItem.get(this.type.itemName(), FurnitureColor.byId(state.getValue(NekorationBlockStateProperties.COLOR))).getItemStack();
    }

    public CabinetType type() {
        return this.type;
    }

    public enum CabinetType {
        DRAWER("drawer", false),
        CABINET("cabinet", true),
        DRAWER_CHEST("drawer_chest", true);

        private final String itemName;
        private final boolean large;

        CabinetType(String itemName, boolean large) {
            this.itemName = itemName;
            this.large = large;
        }

        public String itemName() {
            return this.itemName;
        }

        public boolean large() {
            return this.large;
        }
    }
}
