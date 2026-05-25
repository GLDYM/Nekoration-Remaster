package com.flechazo.nekoration.blocks;

import com.flechazo.nekoration.NekoColors;
import com.flechazo.nekoration.blocks.entities.CustomBlockEntity;
import com.flechazo.nekoration.items.ModItems;
import com.flechazo.nekoration.items.PaletteItem;
import com.flechazo.nekoration.items.TweakItem;
import com.flechazo.nekoration.network.ModPacketHandler;
import com.flechazo.nekoration.network.S2CUpdateCustomBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;

public class CustomBlock extends Block implements EntityBlock {
    public static final IntegerProperty LIGHT = BlockStateProperties.LEVEL;

    public CustomBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
        s.add(LIGHT);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        CustomBlockEntity te = (CustomBlockEntity) world.getBlockEntity(pos);
        if (te == null) return InteractionResult.PASS;

        boolean changed = applyInteraction(state, world, pos, player, hand, hit, stack, item, te);
        if (!changed) return InteractionResult.PASS;

        if (!world.isClientSide) {
            te.setChanged();
            var packet = new S2CUpdateCustomBlockData(te.getBlockPos(), te.dir, te.offset,
                    te.retint, te.showHint, te.color, te.displayState);
            ModPacketHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    private boolean applyInteraction(BlockState state, Level world, BlockPos pos, Player player,
                                     InteractionHand hand, BlockHitResult hit, ItemStack stack,
                                     Item item, CustomBlockEntity te) {
        if (item == Items.AIR) {
            te.showHint = !te.showHint;
            return true;
        }
        if (item instanceof TweakItem) {
            return false; // Let TweakItem handle it
        }
        if (item == ModItems.PALETTE.get()) {
            applyPaletteColor(stack, te);
            te.retint = true;
            return true;
        }
        if (item == ModItems.PAW.get()) {
            te.retint = false;
            return true;
        }
        if (item instanceof AxeItem) {
            world.setBlock(pos, state.cycle(LIGHT), 3);
            return false; // No block entity sync needed
        }
        if (item instanceof BlockItem bi && !(bi.getBlock() instanceof CustomBlock)) {
            return applyDisplayBlock(bi, stack, player, hand, hit, te);
        }
        return false;
    }

    private void applyPaletteColor(ItemStack stack, CustomBlockEntity te) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            byte a = nbt.getByte(PaletteItem.ACTIVE);
            int[] c = nbt.getIntArray(PaletteItem.COLORS);
            if (c.length > a) {
                te.color[0] = NekoColors.getRed(c[a]);
                te.color[1] = NekoColors.getGreen(c[a]);
                te.color[2] = NekoColors.getBlue(c[a]);
                return;
            }
        }
        var def = PaletteItem.DEFAULT_COLOR_SET[0];
        te.color[0] = def.getRed();
        te.color[1] = def.getGreen();
        te.color[2] = def.getBlue();
    }

    private boolean applyDisplayBlock(BlockItem bi, ItemStack stack, Player player,
                                      InteractionHand hand, BlockHitResult hit, CustomBlockEntity te) {
        BlockState newState = bi.getBlock().getStateForPlacement(
                new BlockPlaceContext(player, hand, stack, hit));
        if (newState == null) {
            newState = bi.getBlock().defaultBlockState();
            if (newState == null) return false;
        }
        if (te.displayState == newState) return false;
        te.displayState = newState;
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomBlockEntity(pos, state);
    }
}