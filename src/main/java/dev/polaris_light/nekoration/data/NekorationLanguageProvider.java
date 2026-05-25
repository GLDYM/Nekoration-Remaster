package dev.polaris_light.nekoration.data;

import dev.polaris_light.nekoration.Nekoration;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class NekorationLanguageProvider extends LanguageProvider {
    public NekorationLanguageProvider(PackOutput output, String locale) {
        super(output, Nekoration.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("block.nekoration.oak_chair", "Oak Chair");
        add("block.nekoration.spruce_chair", "Spruce Chair");
        add("block.nekoration.birch_chair", "Birch Chair");
        add("block.nekoration.jungle_chair", "Jungle Chair");
        add("block.nekoration.acacia_chair", "Acacia Chair");
        add("block.nekoration.dark_oak_chair", "Dark Oak Chair");
        add("block.nekoration.crimson_chair", "Crimson Chair");
        add("block.nekoration.warped_chair", "Warped Chair");
        add("block.nekoration.mangrove_chair", "Mangrove Chair");
        add("block.nekoration.pumpkin_chair", "Pumpkin Chair");
        add("block.nekoration.oak_table", "Oak Table");
        add("block.nekoration.spruce_table", "Spruce Table");
        add("block.nekoration.birch_table", "Birch Table");
        add("block.nekoration.jungle_table", "Jungle Table");
        add("block.nekoration.acacia_table", "Acacia Table");
        add("block.nekoration.dark_oak_table", "Dark Oak Table");
        add("block.nekoration.crimson_table", "Crimson Table");
        add("block.nekoration.warped_table", "Warped Table");
        add("block.nekoration.mangrove_table", "Mangrove Table");
        add("block.nekoration.pumpkin_table", "Pumpkin Table");
        add("block.nekoration.oak_round_table", "Round Oak Table");
        add("block.nekoration.spruce_round_table", "Round Spruce Table");
        add("block.nekoration.birch_round_table", "Round Birch Table");
        add("block.nekoration.jungle_round_table", "Round Jungle Table");
        add("block.nekoration.acacia_round_table", "Round Acacia Table");
        add("block.nekoration.dark_oak_round_table", "Round Dark Oak Table");
        add("block.nekoration.crimson_round_table", "Round Crimson Table");
        add("block.nekoration.warped_round_table", "Round Warped Table");
        add("block.nekoration.mangrove_round_table", "Round Mangrove Table");
        add("block.nekoration.glass_table", "Glass Table");
        add("block.nekoration.glass_round_table", "Round Glass Table");
        add("block.nekoration.arm_chair", "Arm Chair");
        add("block.nekoration.bench", "Bench");
        add("block.nekoration.cupboard", "Cupboard");
        add("block.nekoration.shelf", "Shelf");
        add("block.nekoration.wall_shelf", "Wall Shelf");
        add("entity.nekoration.seat", "Seat");
        add("itemGroup.nekoration.furniture", "Nekoration | Furniture");
    }
}
