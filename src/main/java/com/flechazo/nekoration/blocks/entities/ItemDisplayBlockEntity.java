package com.flechazo.nekoration.blocks.entities;

import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.blocks.ItemDisplayBlock;
import com.flechazo.nekoration.network.ModPacketHandler;
import com.flechazo.nekoration.network.S2CUpdateCupboardData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;


public class ItemDisplayBlockEntity extends ContainerBlockEntity {
    private static final ItemStack AIR = ItemStack.EMPTY;
    public ItemStack[] renderItems = {AIR, AIR, AIR, AIR};

    public final boolean wallShelf;
    public final boolean playSound;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
            if (playSound)
                ItemDisplayBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
            ItemDisplayBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
            if (playSound)
                ItemDisplayBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
            ItemDisplayBlockEntity.this.updateBlockState(state, false);
            syncRenderItems(pos);
        }

        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int a, int b) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu) {
                return menu.getContainer() == ItemDisplayBlockEntity.this;
            }
            return false;
        }
    };

    public ItemDisplayBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, false, true);
    }

    public ItemDisplayBlockEntity(BlockPos pos, BlockState state, boolean s, boolean p) {
        super(ModBlockEntityType.ITEM_DISPLAY_TYPE.get(), pos, state);
        this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        this.wallShelf = s;
        this.playSound = p;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
        refreshRenderItems();
    }

    private void refreshRenderItems() {
        renderItems = new ItemStack[]{AIR, AIR, AIR, AIR};
        int idx = 0;
        for (ItemStack item : this.items) {
            if (!item.is(Items.AIR)) {
                renderItems[idx] = item.copy();
                if (++idx >= 4) break;
            }
        }
    }

    private void syncRenderItems(BlockPos pos) {
        refreshRenderItems();
        ModPacketHandler.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new S2CUpdateCupboardData(pos, renderItems));
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory) {
        return ChestMenu.threeRows(windowID, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        items = newItems;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block." + Nekoration.MODID + ".cupboard");
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    private void updateBlockState(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(ItemDisplayBlock.OPEN, open), 3);
    }

    private void playSound(BlockState state, SoundEvent sound) {
        Vec3i vec = state.getValue(ItemDisplayBlock.FACING).getNormal();
        double dx = this.worldPosition.getX() + 0.5D + (double) vec.getX() / 2.0D;
        double dy = this.worldPosition.getY() + 0.5D + (double) vec.getY() / 2.0D;
        double dz = this.worldPosition.getZ() + 0.5D + (double) vec.getZ() / 2.0D;
        this.level.playSound(null, dx, dy, dz, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}