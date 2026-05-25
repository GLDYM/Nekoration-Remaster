package dev.polaris_light.nekoration.init;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.init.item.BlockItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Nekoration.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FURNITURE = TABS.register(
        "furniture",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.nekoration.furniture"))
            .icon(() -> new ItemStack(BlockItemRegistry.OAK_CHAIR.get()))
            .displayItems((parameters, output) ->
                BlockItemRegistry.ITEMS.getEntries().forEach(entry -> output.accept(new ItemStack(entry.get())))
            )
            .build()
    );

    private CreativeModeTabRegistry() {
    }
}
