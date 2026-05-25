package com.flechazo.nekoration.blocks.containers;

import com.flechazo.nekoration.blocks.entities.EaselMenuBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

public class EaselMenuMenu extends AbstractContainerMenu {
    public final EaselMenuBlockEntity easel;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = EaselMenuBlockEntity.NUMBER_OF_SLOTS;

    public static final int TILE_INVENTORY_YPOS = 18;
    public static final int PLAYER_INVENTORY_YPOS = 140;

    public EaselMenuMenu(int windowId, Inventory playerInventory, EaselMenuBlockEntity blockEntity) {
        super(ModMenuType.EASEL_MENU_TYPE.get(), windowId);
        this.easel = blockEntity;

        PlayerInvWrapper playerForge = new PlayerInvWrapper(playerInventory);
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;

        // Hotbar
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new SlotItemHandler(playerForge, x, 8 + SLOT_X_SPACING * x, 198));
        }
        // Player inventory
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                addSlot(new SlotItemHandler(playerForge,
                        HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x,
                        8 + x * SLOT_X_SPACING, PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING));
            }
        }
        // Tile inventory
        for (int x = 0; x < TE_INVENTORY_SLOT_COUNT; x++) {
            addSlot(new Slot(easel, x, 8 + SLOT_X_SPACING * (x > 3 ? x + 1 : x), TILE_INVENTORY_YPOS));
        }
    }

    public EaselMenuMenu(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(windowId, playerInventory, (EaselMenuBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return easel.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int sourceSlotIndex) {
        Slot sourceSlot = slots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copy = sourceStack.copy();

        boolean moved;
        if (sourceSlotIndex < VANILLA_SLOT_COUNT) {
            moved = moveItemStackTo(sourceStack, VANILLA_SLOT_COUNT, VANILLA_SLOT_COUNT + TE_INVENTORY_SLOT_COUNT, false);
        } else if (sourceSlotIndex < VANILLA_SLOT_COUNT + TE_INVENTORY_SLOT_COUNT) {
            moved = moveItemStackTo(sourceStack, 0, VANILLA_SLOT_COUNT, false);
        } else {
            return ItemStack.EMPTY;
        }

        if (!moved) return ItemStack.EMPTY;

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copy;
    }
}