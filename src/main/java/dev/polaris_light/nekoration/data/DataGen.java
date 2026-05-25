package dev.polaris_light.nekoration.data;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.data.client.NekorationBlockStateProvider;
import dev.polaris_light.nekoration.data.client.NekorationItemModelProvider;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Nekoration.MODID)
public final class DataGen {
    private DataGen() {
    }

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new NekorationBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new NekorationItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new NekorationRecipeProvider(output, lookupProvider));
        NekorationBlockTagsProvider blockTagsProvider = new NekorationBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new NekorationItemTagsProvider(output, lookupProvider, blockTagsProvider, existingFileHelper));
        generator.addProvider(event.includeClient(), new NekorationLanguageProvider(output, "en_us"));
    }
}
