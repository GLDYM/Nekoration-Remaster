package dev.polaris_light.nekoration.data;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.init.block.BlockRegistry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class NekorationBlockTagsProvider extends BlockTagsProvider {
    public NekorationBlockTagsProvider(
        PackOutput output,
        CompletableFuture<HolderLookup.Provider> lookupProvider,
        ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, Nekoration.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(
                BlockRegistry.OAK_CHAIR.get(),
                BlockRegistry.SPRUCE_CHAIR.get(),
                BlockRegistry.BIRCH_CHAIR.get(),
                BlockRegistry.JUNGLE_CHAIR.get(),
                BlockRegistry.ACACIA_CHAIR.get(),
                BlockRegistry.DARK_OAK_CHAIR.get(),
                BlockRegistry.CRIMSON_CHAIR.get(),
                BlockRegistry.WARPED_CHAIR.get(),
                BlockRegistry.MANGROVE_CHAIR.get(),
                BlockRegistry.PUMPKIN_CHAIR.get(),
                BlockRegistry.OAK_TABLE.get(),
                BlockRegistry.SPRUCE_TABLE.get(),
                BlockRegistry.BIRCH_TABLE.get(),
                BlockRegistry.JUNGLE_TABLE.get(),
                BlockRegistry.ACACIA_TABLE.get(),
                BlockRegistry.DARK_OAK_TABLE.get(),
                BlockRegistry.CRIMSON_TABLE.get(),
                BlockRegistry.WARPED_TABLE.get(),
                BlockRegistry.MANGROVE_TABLE.get(),
                BlockRegistry.PUMPKIN_TABLE.get(),
                BlockRegistry.OAK_ROUND_TABLE.get(),
                BlockRegistry.SPRUCE_ROUND_TABLE.get(),
                BlockRegistry.BIRCH_ROUND_TABLE.get(),
                BlockRegistry.JUNGLE_ROUND_TABLE.get(),
                BlockRegistry.ACACIA_ROUND_TABLE.get(),
                BlockRegistry.DARK_OAK_ROUND_TABLE.get(),
                BlockRegistry.CRIMSON_ROUND_TABLE.get(),
                BlockRegistry.WARPED_ROUND_TABLE.get(),
                BlockRegistry.MANGROVE_ROUND_TABLE.get(),
                BlockRegistry.GLASS_TABLE.get(),
                BlockRegistry.GLASS_ROUND_TABLE.get(),
                BlockRegistry.ARM_CHAIR.get(),
                BlockRegistry.BENCH.get(),
                BlockRegistry.DRAWER.get(),
                BlockRegistry.CABINET.get(),
                BlockRegistry.DRAWER_CHEST.get(),
                BlockRegistry.CUPBOARD.get(),
                BlockRegistry.SHELF.get(),
                BlockRegistry.WALL_SHELF.get()
            );
    }
}
