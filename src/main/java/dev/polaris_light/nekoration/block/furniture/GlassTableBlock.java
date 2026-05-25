package dev.polaris_light.nekoration.block.furniture;

import com.mojang.serialization.MapCodec;
import dev.polaris_light.nekoration.api.block.DyeableBlock;
import dev.polaris_light.nekoration.api.block.NekorationBlockStateProperties;
import dev.polaris_light.nekoration.init.item.ColoredFurnitureItem;
import dev.polaris_light.nekoration.item.FurnitureColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlassTableBlock extends DyeableBlock {
    public static final MapCodec<GlassTableBlock> CODEC = simpleCodec(GlassTableBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(
        box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D),
        box(0.0D, 0.0D, 0.0D, 2.0D, 14.0D, 2.0D),
        box(14.0D, 0.0D, 0.0D, 16.0D, 14.0D, 2.0D),
        box(14.0D, 0.0D, 14.0D, 16.0D, 14.0D, 16.0D),
        box(0.0D, 0.0D, 14.0D, 2.0D, 14.0D, 16.0D)
    );

    public GlassTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends GlassTableBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ColoredFurnitureItem.get("glass_table", FurnitureColor.byId(state.getValue(NekorationBlockStateProperties.COLOR))).getItemStack();
    }
}
