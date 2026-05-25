package dev.polaris_light.nekoration.data.client;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.item.FurnitureColor;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class NekorationItemModelProvider extends ItemModelProvider {
    public NekorationItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Nekoration.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        furniture("oak_chair");
        furniture("spruce_chair");
        furniture("birch_chair");
        furniture("jungle_chair");
        furniture("acacia_chair");
        furniture("dark_oak_chair");
        furniture("crimson_chair");
        furniture("warped_chair");
        furniture("mangrove_chair");
        furniture("pumpkin_chair");
        furniture("oak_table");
        furniture("spruce_table");
        furniture("birch_table");
        furniture("jungle_table");
        furniture("acacia_table");
        furniture("dark_oak_table");
        furniture("crimson_table");
        furniture("warped_table");
        furniture("mangrove_table");
        furniture("pumpkin_table");
        furniture("oak_round_table");
        furniture("spruce_round_table");
        furniture("birch_round_table");
        furniture("jungle_round_table");
        furniture("acacia_round_table");
        furniture("dark_oak_round_table");
        furniture("crimson_round_table");
        furniture("warped_round_table");
        furniture("mangrove_round_table");
        coloredFurniture("glass_table", "glass_table");
        coloredFurniture("glass_round_table", "glass_round_table");
        coloredFurniture("arm_chair", "arm_chair");
        coloredBench();
        coloredStorage("cupboard", "cupboard");
        coloredStorage("shelf", "shelf");
        coloredStorage("wall_shelf", "wall_shelf_s0");
    }

    private void furniture(String name) {
        withExistingParent(name, modLoc("block/furniture/" + name));
    }

    private void coloredFurniture(String itemSuffix, String blockModelName) {
        for (FurnitureColor color : FurnitureColor.values()) {
            withExistingParent(color.serializedName() + "_" + itemSuffix, modLoc("block/furniture/" + blockModelName));
        }
    }

    private void coloredBench() {
        for (FurnitureColor color : FurnitureColor.values()) {
            withExistingParent(color.serializedName() + "_bench", modLoc("block/furniture/bench_s0"));
        }
    }

    private void coloredStorage(String itemSuffix, String blockModelName) {
        for (FurnitureColor color : FurnitureColor.values()) {
            withExistingParent(color.serializedName() + "_" + itemSuffix, modLoc("block/storage/" + blockModelName));
        }
    }
}
