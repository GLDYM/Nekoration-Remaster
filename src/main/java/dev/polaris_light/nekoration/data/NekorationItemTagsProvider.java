package dev.polaris_light.nekoration.data;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.init.item.BlockItemRegistry;
import dev.polaris_light.nekoration.tag.NekorationItemTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class NekorationItemTagsProvider extends ItemTagsProvider {
    public NekorationItemTagsProvider(
        PackOutput output,
        CompletableFuture<HolderLookup.Provider> lookupProvider,
        BlockTagsProvider blockTagsProvider,
        ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, blockTagsProvider.contentsGetter(), Nekoration.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NekorationItemTags.GLASS_TABLES)
            .add(
                BlockItemRegistry.BLACK_GLASS_TABLE.get(),
                BlockItemRegistry.BLUE_GLASS_TABLE.get(),
                BlockItemRegistry.BROWN_GLASS_TABLE.get(),
                BlockItemRegistry.CYAN_GLASS_TABLE.get(),
                BlockItemRegistry.GRAY_GLASS_TABLE.get(),
                BlockItemRegistry.GREEN_GLASS_TABLE.get(),
                BlockItemRegistry.LIGHT_BLUE_GLASS_TABLE.get(),
                BlockItemRegistry.LIGHT_GRAY_GLASS_TABLE.get(),
                BlockItemRegistry.LIME_GLASS_TABLE.get(),
                BlockItemRegistry.MAGENTA_GLASS_TABLE.get(),
                BlockItemRegistry.ORANGE_GLASS_TABLE.get(),
                BlockItemRegistry.PINK_GLASS_TABLE.get(),
                BlockItemRegistry.PURPLE_GLASS_TABLE.get(),
                BlockItemRegistry.RED_GLASS_TABLE.get(),
                BlockItemRegistry.WHITE_GLASS_TABLE.get(),
                BlockItemRegistry.YELLOW_GLASS_TABLE.get()
            );
        tag(NekorationItemTags.GLASS_ROUND_TABLES)
            .add(
                BlockItemRegistry.BLACK_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.BLUE_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.BROWN_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.CYAN_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.GRAY_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.GREEN_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.LIGHT_BLUE_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.LIGHT_GRAY_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.LIME_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.MAGENTA_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.ORANGE_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.PINK_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.PURPLE_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.RED_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.WHITE_GLASS_ROUND_TABLE.get(),
                BlockItemRegistry.YELLOW_GLASS_ROUND_TABLE.get()
            );
        tag(NekorationItemTags.ARM_CHAIRS)
            .add(
                BlockItemRegistry.BLACK_ARM_CHAIR.get(),
                BlockItemRegistry.BLUE_ARM_CHAIR.get(),
                BlockItemRegistry.BROWN_ARM_CHAIR.get(),
                BlockItemRegistry.CYAN_ARM_CHAIR.get(),
                BlockItemRegistry.GRAY_ARM_CHAIR.get(),
                BlockItemRegistry.GREEN_ARM_CHAIR.get(),
                BlockItemRegistry.LIGHT_BLUE_ARM_CHAIR.get(),
                BlockItemRegistry.LIGHT_GRAY_ARM_CHAIR.get(),
                BlockItemRegistry.LIME_ARM_CHAIR.get(),
                BlockItemRegistry.MAGENTA_ARM_CHAIR.get(),
                BlockItemRegistry.ORANGE_ARM_CHAIR.get(),
                BlockItemRegistry.PINK_ARM_CHAIR.get(),
                BlockItemRegistry.PURPLE_ARM_CHAIR.get(),
                BlockItemRegistry.RED_ARM_CHAIR.get(),
                BlockItemRegistry.WHITE_ARM_CHAIR.get(),
                BlockItemRegistry.YELLOW_ARM_CHAIR.get()
            );
        tag(NekorationItemTags.BENCHES)
            .add(
                BlockItemRegistry.BLACK_BENCH.get(),
                BlockItemRegistry.BLUE_BENCH.get(),
                BlockItemRegistry.BROWN_BENCH.get(),
                BlockItemRegistry.CYAN_BENCH.get(),
                BlockItemRegistry.GRAY_BENCH.get(),
                BlockItemRegistry.GREEN_BENCH.get(),
                BlockItemRegistry.LIGHT_BLUE_BENCH.get(),
                BlockItemRegistry.LIGHT_GRAY_BENCH.get(),
                BlockItemRegistry.LIME_BENCH.get(),
                BlockItemRegistry.MAGENTA_BENCH.get(),
                BlockItemRegistry.ORANGE_BENCH.get(),
                BlockItemRegistry.PINK_BENCH.get(),
                BlockItemRegistry.PURPLE_BENCH.get(),
                BlockItemRegistry.RED_BENCH.get(),
                BlockItemRegistry.WHITE_BENCH.get(),
                BlockItemRegistry.YELLOW_BENCH.get()
            );
    }
}
