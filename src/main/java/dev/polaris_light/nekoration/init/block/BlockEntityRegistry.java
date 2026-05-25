package dev.polaris_light.nekoration.init.block;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.block.entity.CabinetBlockEntity;
import dev.polaris_light.nekoration.block.storage.CabinetBlock;
import dev.polaris_light.nekoration.block.entity.DisplayShelfBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Nekoration.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DisplayShelfBlockEntity>> DISPLAY_SHELF =
        BLOCK_ENTITIES.register(
            "display_shelf",
            () -> BlockEntityType.Builder.of(
                (pos, state) -> {
                    if (state.getBlock() == BlockRegistry.WALL_SHELF.get()) {
                        return new DisplayShelfBlockEntity(pos, state, true, false);
                    }
                    return new DisplayShelfBlockEntity(pos, state, false, state.getBlock() == BlockRegistry.CUPBOARD.get());
                },
                BlockRegistry.CUPBOARD.get(),
                BlockRegistry.SHELF.get(),
                BlockRegistry.WALL_SHELF.get()
            ).build(null)
        );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CabinetBlockEntity>> CABINET =
        BLOCK_ENTITIES.register(
            "cabinet",
            () -> BlockEntityType.Builder.of(
                (pos, state) -> new CabinetBlockEntity(pos, state, ((CabinetBlock) state.getBlock()).type()),
                BlockRegistry.DRAWER.get(),
                BlockRegistry.CABINET.get(),
                BlockRegistry.DRAWER_CHEST.get()
            ).build(null)
        );

    private BlockEntityRegistry() {
    }
}
