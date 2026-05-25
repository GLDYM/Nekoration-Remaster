package dev.polaris_light.nekoration.data.client;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.api.block.HorizontalConnection;
import dev.polaris_light.nekoration.api.block.NekorationBlockStateProperties;
import dev.polaris_light.nekoration.block.storage.CabinetBlock;
import dev.polaris_light.nekoration.block.storage.CupboardBlock;
import dev.polaris_light.nekoration.block.storage.ShelfBlock;
import dev.polaris_light.nekoration.init.block.BlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class NekorationBlockStateProvider extends BlockStateProvider {
    public NekorationBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Nekoration.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        chair(BlockRegistry.OAK_CHAIR.get(), "oak_chair", "oak_side", "oak_top");
        chair(BlockRegistry.SPRUCE_CHAIR.get(), "spruce_chair", "spruce_side", "spruce_top");
        chair(BlockRegistry.BIRCH_CHAIR.get(), "birch_chair", "birch_side", "birch_top");
        chair(BlockRegistry.JUNGLE_CHAIR.get(), "jungle_chair", "jungle_side", "jungle_top");
        chair(BlockRegistry.ACACIA_CHAIR.get(), "acacia_chair", "acacia_side", "acacia_top");
        chair(BlockRegistry.DARK_OAK_CHAIR.get(), "dark_oak_chair", "dark_oak_side", "dark_oak_top");
        chair(BlockRegistry.CRIMSON_CHAIR.get(), "crimson_chair", "crimson_side", "crimson_top");
        chair(BlockRegistry.WARPED_CHAIR.get(), "warped_chair", "warped_side", "warped_top");
        chair(BlockRegistry.MANGROVE_CHAIR.get(), "mangrove_chair", "mangrove_side", "mangrove_top");
        chair(BlockRegistry.PUMPKIN_CHAIR.get(), "pumpkin_chair", "pumpkin_side", "pumpkin_top");
        table(BlockRegistry.OAK_TABLE.get(), "oak_table", "oak_side", "oak_top");
        table(BlockRegistry.SPRUCE_TABLE.get(), "spruce_table", "spruce_side", "spruce_top");
        table(BlockRegistry.BIRCH_TABLE.get(), "birch_table", "birch_side", "birch_top");
        table(BlockRegistry.JUNGLE_TABLE.get(), "jungle_table", "jungle_side", "jungle_top");
        table(BlockRegistry.ACACIA_TABLE.get(), "acacia_table", "acacia_side", "acacia_top");
        table(BlockRegistry.DARK_OAK_TABLE.get(), "dark_oak_table", "dark_oak_side", "dark_oak_top");
        table(BlockRegistry.CRIMSON_TABLE.get(), "crimson_table", "crimson_side", "crimson_top");
        table(BlockRegistry.WARPED_TABLE.get(), "warped_table", "warped_side", "warped_top");
        table(BlockRegistry.MANGROVE_TABLE.get(), "mangrove_table", "mangrove_side", "mangrove_top");
        table(BlockRegistry.PUMPKIN_TABLE.get(), "pumpkin_table", "pumpkin_side", "pumpkin_top");
        roundTable(BlockRegistry.OAK_ROUND_TABLE.get(), "oak_round_table", "oak_leg", "oak_rtop");
        roundTable(BlockRegistry.SPRUCE_ROUND_TABLE.get(), "spruce_round_table", "spruce_leg", "spruce_rtop");
        roundTable(BlockRegistry.BIRCH_ROUND_TABLE.get(), "birch_round_table", "birch_leg", "birch_rtop");
        roundTable(BlockRegistry.JUNGLE_ROUND_TABLE.get(), "jungle_round_table", "jungle_leg", "jungle_rtop");
        roundTable(BlockRegistry.ACACIA_ROUND_TABLE.get(), "acacia_round_table", "acacia_leg", "acacia_rtop");
        roundTable(BlockRegistry.DARK_OAK_ROUND_TABLE.get(), "dark_oak_round_table", "dark_oak_leg", "dark_oak_rtop");
        roundTable(BlockRegistry.CRIMSON_ROUND_TABLE.get(), "crimson_round_table", "crimson_leg", "crimson_rtop");
        roundTable(BlockRegistry.WARPED_ROUND_TABLE.get(), "warped_round_table", "warped_leg", "warped_rtop");
        roundTable(BlockRegistry.MANGROVE_ROUND_TABLE.get(), "mangrove_round_table", "mangrove_leg", "mangrove_rtop");
        glassTable(BlockRegistry.GLASS_TABLE.get(), "glass_table");
        glassTable(BlockRegistry.GLASS_ROUND_TABLE.get(), "glass_round_table");
        chair(BlockRegistry.ARM_CHAIR.get(), "arm_chair", "arm_side", "arm_top");
        bench(BlockRegistry.BENCH.get());
        cabinet(BlockRegistry.DRAWER.get(), "drawer");
        cabinet(BlockRegistry.CABINET.get(), "cabinet");
        cabinet(BlockRegistry.DRAWER_CHEST.get(), "drawer_chest");
        cupboard(BlockRegistry.CUPBOARD.get());
        shelf(BlockRegistry.SHELF.get());
        wallShelf(BlockRegistry.WALL_SHELF.get());
    }

    private void chair(Block block, String name, String sideTexture, String topTexture) {
        ModelFile model = models()
            .getExistingFile(modLoc("block/furniture/" + name));

        getVariantBuilder(block).forAllStates(state -> {
            int y = switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            return ConfiguredModel.builder().modelFile(model).rotationY(y).build();
        });
    }

    private void table(Block block, String name, String sideTexture, String topTexture) {
        ModelFile model = models()
            .withExistingParent("block/furniture/" + name, modLoc("block/furniture/table_base"))
            .texture("0", modLoc("block/furniture/" + sideTexture))
            .texture("1", modLoc("block/furniture/" + topTexture))
            .texture("particle", modLoc("block/furniture/" + sideTexture));

        simpleBlock(block, model);
    }

    private void roundTable(Block block, String name, String legTexture, String topTexture) {
        ModelFile model = models()
            .withExistingParent("block/furniture/" + name, modLoc("block/furniture/round_table_base"))
            .texture("0", modLoc("block/furniture/" + legTexture))
            .texture("1", modLoc("block/furniture/" + topTexture))
            .texture("particle", modLoc("block/furniture/" + legTexture));

        simpleBlock(block, model);
    }

    private void glassTable(Block block, String name) {
        simpleBlock(block, models().getExistingFile(modLoc("block/furniture/" + name)));
    }

    private void bench(Block block) {
        ModelFile s0 = models().getExistingFile(modLoc("block/furniture/bench_s0"));
        ModelFile t0 = models().getExistingFile(modLoc("block/furniture/bench_t0"));
        ModelFile t1 = models().getExistingFile(modLoc("block/furniture/bench_t1"));
        ModelFile t2 = models().getExistingFile(modLoc("block/furniture/bench_t2"));

        getVariantBuilder(block).forAllStates(state -> {
            int y = switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            HorizontalConnection connection = state.getValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION);
            ModelFile model = switch (connection) {
                case S0 -> s0;
                case D0, T0 -> t0;
                case T1 -> t1;
                case D1, T2 -> t2;
            };
            return ConfiguredModel.builder().modelFile(model).rotationY(y).build();
        });
    }

    private void cabinet(Block block, String name) {
        ModelFile closed = models().getExistingFile(modLoc("block/storage/" + name));
        ModelFile open = models().getExistingFile(modLoc("block/storage/" + name + "_open"));

        getVariantBuilder(block).forAllStates(state -> {
            int y = switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            ModelFile model = state.getValue(CabinetBlock.OPEN) ? open : closed;
            return ConfiguredModel.builder().modelFile(model).rotationY(y).build();
        });
    }

    private void cupboard(Block block) {
        ModelFile model = models().getExistingFile(modLoc("block/storage/cupboard"));
        ModelFile bottomModel = models().getExistingFile(modLoc("block/storage/cupboard_bottom"));

        getVariantBuilder(block).forAllStates(state -> {
            int y = switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            ModelFile configuredModel = state.getValue(CupboardBlock.BOTTOM) ? bottomModel : model;
            return ConfiguredModel.builder().modelFile(configuredModel).rotationY(y).build();
        });
    }

    private void shelf(Block block) {
        ModelFile model = models().getExistingFile(modLoc("block/storage/shelf"));
        ModelFile bottomModel = models().getExistingFile(modLoc("block/storage/shelf_bottom"));

        getVariantBuilder(block).forAllStates(state -> {
            int y = switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            ModelFile configuredModel = state.getValue(ShelfBlock.BOTTOM) ? bottomModel : model;
            return ConfiguredModel.builder().modelFile(configuredModel).rotationY(y).build();
        });
    }

    private void wallShelf(Block block) {
        ModelFile s0 = models().getExistingFile(modLoc("block/storage/wall_shelf_s0"));
        ModelFile t0 = models().getExistingFile(modLoc("block/storage/wall_shelf_t0"));
        ModelFile t1 = models().getExistingFile(modLoc("block/storage/wall_shelf_t1"));
        ModelFile t2 = models().getExistingFile(modLoc("block/storage/wall_shelf_t2"));

        getVariantBuilder(block).forAllStates(state -> {
            int y = switch (state.getValue(HorizontalDirectionalBlock.FACING)) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            HorizontalConnection connection = state.getValue(NekorationBlockStateProperties.HORIZONTAL_CONNECTION);
            ModelFile model = switch (connection) {
                case S0 -> s0;
                case D0, T0 -> t0;
                case T1 -> t1;
                case D1, T2 -> t2;
            };
            return ConfiguredModel.builder().modelFile(model).rotationY(y).build();
        });
    }

    public ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Nekoration.MODID, path);
    }
}
