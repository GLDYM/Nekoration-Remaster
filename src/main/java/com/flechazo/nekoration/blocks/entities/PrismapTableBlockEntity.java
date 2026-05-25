package com.flechazo.nekoration.blocks.entities;

import com.flechazo.nekoration.client.rendering.ChunkModel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PrismapTableBlockEntity extends BlockEntity {
    @Nullable
    public ChunkModel chunkModel;
    public int viewAreaRadius;

    public PrismapTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.PRISMAP_TABLE_TYPE.get(), pos, state);
        viewAreaRadius = 1;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        Level world = getLevel();
        if (world == null || !world.isClientSide)
            return;
        createIfNull();
    }

    public void createIfNull() {
        if (chunkModel == null)
            chunkModel = ChunkModel.forTileEntity(this.level, this, viewAreaRadius);
    }

    public void refresh() {
        if (chunkModel != null && !chunkModel.isCompiling())
            chunkModel.compile(Minecraft.getInstance(), this.getBlockPos());
    }

    public boolean shouldRenderFace(Direction dir) {
        return Block.shouldRenderFace(this.getBlockState(), this.level, this.getBlockPos(), dir, this.getBlockPos().relative(dir));
    }
}