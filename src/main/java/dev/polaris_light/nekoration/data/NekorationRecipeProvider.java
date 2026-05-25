package dev.polaris_light.nekoration.data;

import dev.polaris_light.nekoration.init.item.BlockItemRegistry;
import dev.polaris_light.nekoration.init.item.ColoredFurnitureItem;
import dev.polaris_light.nekoration.item.FurnitureColor;
import dev.polaris_light.nekoration.tag.NekorationItemTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public final class NekorationRecipeProvider extends RecipeProvider {
    public NekorationRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        makeChair(output, Items.OAK_PLANKS, BlockItemRegistry.OAK_CHAIR.get(), "oak_chair");
        makeChair(output, Items.SPRUCE_PLANKS, BlockItemRegistry.SPRUCE_CHAIR.get(), "spruce_chair");
        makeChair(output, Items.BIRCH_PLANKS, BlockItemRegistry.BIRCH_CHAIR.get(), "birch_chair");
        makeChair(output, Items.JUNGLE_PLANKS, BlockItemRegistry.JUNGLE_CHAIR.get(), "jungle_chair");
        makeChair(output, Items.ACACIA_PLANKS, BlockItemRegistry.ACACIA_CHAIR.get(), "acacia_chair");
        makeChair(output, Items.DARK_OAK_PLANKS, BlockItemRegistry.DARK_OAK_CHAIR.get(), "dark_oak_chair");
        makeChair(output, Items.CRIMSON_PLANKS, BlockItemRegistry.CRIMSON_CHAIR.get(), "crimson_chair");
        makeChair(output, Items.WARPED_PLANKS, BlockItemRegistry.WARPED_CHAIR.get(), "warped_chair");
        makeChair(output, Items.MANGROVE_PLANKS, BlockItemRegistry.MANGROVE_CHAIR.get(), "mangrove_chair");
        makeChair(output, Items.PUMPKIN, BlockItemRegistry.PUMPKIN_CHAIR.get(), "pumpkin_chair");
        makeTable(output, Items.OAK_PLANKS, BlockItemRegistry.OAK_TABLE.get(), "oak_table");
        makeTable(output, Items.SPRUCE_PLANKS, BlockItemRegistry.SPRUCE_TABLE.get(), "spruce_table");
        makeTable(output, Items.BIRCH_PLANKS, BlockItemRegistry.BIRCH_TABLE.get(), "birch_table");
        makeTable(output, Items.JUNGLE_PLANKS, BlockItemRegistry.JUNGLE_TABLE.get(), "jungle_table");
        makeTable(output, Items.ACACIA_PLANKS, BlockItemRegistry.ACACIA_TABLE.get(), "acacia_table");
        makeTable(output, Items.DARK_OAK_PLANKS, BlockItemRegistry.DARK_OAK_TABLE.get(), "dark_oak_table");
        makeTable(output, Items.CRIMSON_PLANKS, BlockItemRegistry.CRIMSON_TABLE.get(), "crimson_table");
        makeTable(output, Items.WARPED_PLANKS, BlockItemRegistry.WARPED_TABLE.get(), "warped_table");
        makeTable(output, Items.MANGROVE_PLANKS, BlockItemRegistry.MANGROVE_TABLE.get(), "mangrove_table");
        makeTable(output, Items.PUMPKIN, BlockItemRegistry.PUMPKIN_TABLE.get(), "pumpkin_table");
        makeRoundTable(output, Items.OAK_PLANKS, BlockItemRegistry.OAK_ROUND_TABLE.get(), "oak_round_table");
        makeRoundTable(output, Items.SPRUCE_PLANKS, BlockItemRegistry.SPRUCE_ROUND_TABLE.get(), "spruce_round_table");
        makeRoundTable(output, Items.BIRCH_PLANKS, BlockItemRegistry.BIRCH_ROUND_TABLE.get(), "birch_round_table");
        makeRoundTable(output, Items.JUNGLE_PLANKS, BlockItemRegistry.JUNGLE_ROUND_TABLE.get(), "jungle_round_table");
        makeRoundTable(output, Items.ACACIA_PLANKS, BlockItemRegistry.ACACIA_ROUND_TABLE.get(), "acacia_round_table");
        makeRoundTable(output, Items.DARK_OAK_PLANKS, BlockItemRegistry.DARK_OAK_ROUND_TABLE.get(), "dark_oak_round_table");
        makeRoundTable(output, Items.CRIMSON_PLANKS, BlockItemRegistry.CRIMSON_ROUND_TABLE.get(), "crimson_round_table");
        makeRoundTable(output, Items.WARPED_PLANKS, BlockItemRegistry.WARPED_ROUND_TABLE.get(), "warped_round_table");
        makeRoundTable(output, Items.MANGROVE_PLANKS, BlockItemRegistry.MANGROVE_ROUND_TABLE.get(), "mangrove_round_table");

        // TODO: More Recipes
        makeGlassTable(output, ItemTags.PLANKS, BlockItemRegistry.BROWN_GLASS_TABLE.get(), "brown_glass_table");
        makeGlassRoundTable(output, ItemTags.PLANKS, BlockItemRegistry.BROWN_GLASS_ROUND_TABLE.get(), "brown_glass_round_table");
        makeArmChair(output, ItemTags.PLANKS, Items.STICK, BlockItemRegistry.BROWN_ARM_CHAIR.get(), "brown_arm_chair");
        makeBench(output, ItemTags.PLANKS, Items.SPRUCE_SIGN, BlockItemRegistry.BROWN_BENCH.get(), "brown_bench");
        makeCupboard(output, Items.OAK_PLANKS, Items.OAK_PRESSURE_PLATE, BlockItemRegistry.BROWN_CUPBOARD.get(), "cupboard");
        makeShelf(output, Items.OAK_PLANKS, Items.OAK_PRESSURE_PLATE, BlockItemRegistry.BROWN_SHELF.get(), "shelf");
        makeWallShelf(output, Items.OAK_PRESSURE_PLATE, Items.STICK, BlockItemRegistry.BROWN_WALL_SHELF.get(), "wall_shelf");
        for (FurnitureColor color : FurnitureColor.values()) {
            recolor(output, "glass_table", NekorationItemTags.GLASS_TABLES, color);
            recolor(output, "glass_round_table", NekorationItemTags.GLASS_ROUND_TABLES, color);
            recolor(output, "arm_chair", NekorationItemTags.ARM_CHAIRS, color);
            recolor(output, "bench", NekorationItemTags.BENCHES, color);
            recolor(output, "cupboard", NekorationItemTags.CUPBOARDS, color);
            recolor(output, "shelf", NekorationItemTags.SHELVES, color);
            recolor(output, "wall_shelf", NekorationItemTags.WALL_SHELVES, color);
        }
    }

    private void makeChair(RecipeOutput output, ItemLike material, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("#  ")
            .pattern("###")
            .pattern("# #")
            .define('#', material)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeTable(RecipeOutput output, ItemLike material, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("###")
            .pattern("# #")
            .pattern("# #")
            .define('#', material)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeRoundTable(RecipeOutput output, ItemLike material, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("###")
            .pattern(" # ")
            .pattern(" # ")
            .define('#', material)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeGlassTable(RecipeOutput output, TagKey<Item> material, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("#G#")
            .pattern("# #")
            .pattern("# #")
            .define('#', material)
            .define('G', Items.GLASS)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeGlassRoundTable(RecipeOutput output, TagKey<Item> material, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("GGG")
            .pattern(" # ")
            .pattern(" # ")
            .define('#', material)
            .define('G', Items.GLASS)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeArmChair(RecipeOutput output, TagKey<Item> material, ItemLike support, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("#  ")
            .pattern("#SS")
            .pattern("###")
            .define('#', material)
            .define('S', support)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeBench(RecipeOutput output, TagKey<Item> material, ItemLike sign, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result, 4)
            .pattern("S  ")
            .pattern("###")
            .pattern("# #")
            .define('#', material)
            .define('S', sign)
            .unlockedBy("has_material", has(material))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "furniture/" + name));
    }

    private void makeCupboard(RecipeOutput output, ItemLike planks, ItemLike pressurePlate, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result)
            .pattern("###")
            .pattern("#P ")
            .pattern("###")
            .define('#', planks)
            .define('P', pressurePlate)
            .unlockedBy("has_material", has(planks))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "storage/" + name));
    }

    private void makeShelf(RecipeOutput output, ItemLike planks, ItemLike pressurePlate, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result)
            .pattern("#P#")
            .pattern("###")
            .pattern("# #")
            .define('#', planks)
            .define('P', pressurePlate)
            .unlockedBy("has_material", has(planks))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "storage/" + name));
    }

    private void makeWallShelf(RecipeOutput output, ItemLike pressurePlate, ItemLike stick, ItemLike result, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result)
            .pattern("PP")
            .pattern("S ")
            .define('P', pressurePlate)
            .define('S', stick)
            .unlockedBy("has_material", has(pressurePlate))
            .save(output, ResourceLocation.fromNamespaceAndPath("nekoration", "storage/" + name));
    }

    private void recolor(RecipeOutput output, String furnitureName, TagKey<Item> inputTag, FurnitureColor color) {
        ItemLike result = ColoredFurnitureItem.get(furnitureName, color).getItem();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, result)
            .requires(inputTag)
            .requires(color.dye())
            .unlockedBy("has_furniture", has(inputTag))
            .save(
                output,
                ResourceLocation.fromNamespaceAndPath(
                    "nekoration",
                    "furniture/" + color.serializedName() + "_" + furnitureName + "_recolor"
                )
            );
    }
}
