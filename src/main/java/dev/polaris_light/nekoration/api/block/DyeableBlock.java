package dev.polaris_light.nekoration.api.block;

import dev.polaris_light.nekoration.api.item.DyeableBlockItem;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public abstract class DyeableBlock extends Block {

    protected static final Map<Item, Integer> DYE_COLOR_IDS = Map.ofEntries(
        Map.entry(Items.BLACK_DYE, 0),
        Map.entry(Items.BLUE_DYE, 1),
        Map.entry(Items.BROWN_DYE, 2),
        Map.entry(Items.CYAN_DYE, 3),
        Map.entry(Items.GRAY_DYE, 4),
        Map.entry(Items.GREEN_DYE, 5),
        Map.entry(Items.LIGHT_BLUE_DYE, 6),
        Map.entry(Items.LIGHT_GRAY_DYE, 7),
        Map.entry(Items.LIME_DYE, 8),
        Map.entry(Items.MAGENTA_DYE, 9),
        Map.entry(Items.ORANGE_DYE, 10),
        Map.entry(Items.PINK_DYE, 11),
        Map.entry(Items.PURPLE_DYE, 12),
        Map.entry(Items.RED_DYE, 13),
        Map.entry(Items.WHITE_DYE, 14),
        Map.entry(Items.YELLOW_DYE, 15)
    );

    protected DyeableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(NekorationBlockStateProperties.COLOR, 14));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NekorationBlockStateProperties.COLOR);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        if (stack.getItem() instanceof DyeableBlockItem furnitureBlockItem) {
            return this.defaultBlockState().setValue(NekorationBlockStateProperties.COLOR, furnitureBlockItem.color().id());
        }
        return this.defaultBlockState();
    }

    @Override
    protected InteractionResult useWithoutItem(
        BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult
    ) {
        return InteractionResult.PASS;
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
        Integer colorId = DYE_COLOR_IDS.get(stack.getItem());
        if (colorId == null || !(stack.getItem() instanceof DyeItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (state.getValue(NekorationBlockStateProperties.COLOR) == colorId) {
            return ItemInteractionResult.SUCCESS;
        }
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(NekorationBlockStateProperties.COLOR, colorId), 3);
            stack.consume(1, player);
        }
        return ItemInteractionResult.SUCCESS;
    }
}
