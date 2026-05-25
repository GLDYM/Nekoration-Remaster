package dev.polaris_light.nekoration.block.entity;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.init.block.BlockEntityRegistry;
import dev.polaris_light.nekoration.block.storage.AbstractDisplayShelfBlock;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayShelfBlockEntity extends RandomizableContainerBlockEntity {
    private static final int CONTAINER_SIZE = 27;
    private static final int DISPLAY_SLOT_COUNT = 4;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private final boolean wallMounted;
    private final boolean playSound;
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            if (DisplayShelfBlockEntity.this.playSound) {
                DisplayShelfBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
            }
            DisplayShelfBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            if (DisplayShelfBlockEntity.this.playSound) {
                DisplayShelfBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
            }
            DisplayShelfBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int openCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu chestMenu) {
                Container container = chestMenu.getContainer();
                return container == DisplayShelfBlockEntity.this;
            }
            return false;
        }
    };

    public DisplayShelfBlockEntity(BlockPos pos, BlockState state, boolean wallMounted, boolean playSound) {
        super(BlockEntityRegistry.DISPLAY_SHELF.get(), pos, state);
        this.wallMounted = wallMounted;
        this.playSound = playSound;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, registries);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, registries);
        }
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        String key;
        if (this.wallMounted) {
            key = "wall_shelf";
        } else if (this.playSound) {
            key = "cupboard";
        } else {
            key = "shelf";
        }
        return Component.translatable("block." + Nekoration.MODID + "." + key);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
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

    public boolean isWallMounted() {
        return this.wallMounted;
    }

    public List<ItemStack> getDisplayItems() {
        NonNullList<ItemStack> displayItems = NonNullList.withSize(DISPLAY_SLOT_COUNT, ItemStack.EMPTY);
        int index = 0;
        for (ItemStack itemStack : this.items) {
            if (itemStack.isEmpty()) {
                continue;
            }
            displayItems.set(index++, itemStack);
            if (index >= DISPLAY_SLOT_COUNT) {
                break;
            }
        }
        return displayItems;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    private void updateBlockState(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(AbstractDisplayShelfBlock.OPEN, open), 3);
    }

    private void playSound(BlockState state, SoundEvent sound) {
        Vec3i vec3i = state.getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING).getNormal();
        double x = this.worldPosition.getX() + 0.5D + vec3i.getX() / 2.0D;
        double y = this.worldPosition.getY() + 0.5D + vec3i.getY() / 2.0D;
        double z = this.worldPosition.getZ() + 0.5D + vec3i.getZ() / 2.0D;
        this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}
