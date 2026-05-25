package com.flechazo.nekoration.blocks.entities;

import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.blocks.containers.EaselMenuMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class EaselMenuBlockEntity extends ContainerBlockEntity {
    public static final int NUMBER_OF_SLOTS = 8;

    private final Component[] messages = new Component[NUMBER_OF_SLOTS];
    private final DyeColor[] textColors = new DyeColor[NUMBER_OF_SLOTS];
    public ItemStack[] renderItems = new ItemStack[NUMBER_OF_SLOTS];
    private boolean isEditable = true;
    private Player playerWhoMayEdit;
    private boolean isGlowing;

    public final boolean white;

    public EaselMenuBlockEntity(boolean w, BlockPos pos, BlockState state) {
        super(ModBlockEntityType.EASEL_MENU_TYPE.get(), pos, state);
        white = w;
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
            messages[i] = CommonComponents.EMPTY;
            textColors[i] = DyeColor.GRAY;
            renderItems[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
            tag.putString("Text" + (i + 1), Component.Serializer.toJson(messages[i]));
        }
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
            tag.putString("Color" + i, textColors[i].getName());
        }
        tag.putBoolean("Glowing", isGlowing);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        for (int i = 0; i < NUMBER_OF_SLOTS; i++) {
            renderItems[i] = getItem(i);
            String s = tag.getString("Text" + (i + 1));
            messages[i] = Component.Serializer.fromJson(s.isEmpty() ? "\"\"" : s);
            textColors[i] = DyeColor.byName(tag.getString("Color" + i), DyeColor.GRAY);
        }
        isGlowing = tag.getBoolean("Glowing");
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

    public boolean isEditable() {
        return this.isEditable;
    }

    @OnlyIn(Dist.CLIENT)
    public void setEditable(boolean editable) {
        this.isEditable = editable;
        if (!editable) {
            this.playerWhoMayEdit = null;
        }
    }

    public void setAllowedPlayerEditor(Player player) {
        this.playerWhoMayEdit = player;
    }

    public Player getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }

    @Override
    public int getContainerSize() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block." + Nekoration.MODID + ".easel_menu");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block." + Nekoration.MODID + ".easel_menu");
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInventory) {
        return new EaselMenuMenu(windowId, playerInventory, this);
    }

    public Component getMessage(int line) {
        return messages[line];
    }

    public void setMessage(int line, Component text) {
        messages[line] = text;
    }

    public Component[] getMessages() {
        return messages;
    }

    public DyeColor[] getColors() {
        return textColors;
    }

    public void setColors(DyeColor[] colors) {
        System.arraycopy(colors, 0, textColors, 0, NUMBER_OF_SLOTS);
    }

    public DyeColor getColor(int line) {
        return textColors[line];
    }

    public void setColor(int line, DyeColor color) {
        textColors[line] = color;
    }

    public boolean getGlowing() {
        return isGlowing;
    }

    public void setGlowing(boolean glow) {
        isGlowing = glow;
    }

    public boolean toggleGlowing() {
        return (isGlowing = !isGlowing);
    }
}