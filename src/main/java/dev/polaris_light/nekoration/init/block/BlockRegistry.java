package dev.polaris_light.nekoration.init.block;

import dev.polaris_light.nekoration.Nekoration;
import dev.polaris_light.nekoration.block.furniture.ArmChairBlock;
import dev.polaris_light.nekoration.block.furniture.BenchBlock;
import dev.polaris_light.nekoration.block.furniture.ChairBlock;
import dev.polaris_light.nekoration.block.furniture.GlassRoundTableBlock;
import dev.polaris_light.nekoration.block.furniture.GlassTableBlock;
import dev.polaris_light.nekoration.block.furniture.RoundTableBlock;
import dev.polaris_light.nekoration.block.furniture.TableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Nekoration.MODID);

    public static final DeferredBlock<ChairBlock> OAK_CHAIR = registerChair("oak_chair");
    public static final DeferredBlock<ChairBlock> SPRUCE_CHAIR = registerChair("spruce_chair");
    public static final DeferredBlock<ChairBlock> BIRCH_CHAIR = registerChair("birch_chair");
    public static final DeferredBlock<ChairBlock> JUNGLE_CHAIR = registerChair("jungle_chair");
    public static final DeferredBlock<ChairBlock> ACACIA_CHAIR = registerChair("acacia_chair");
    public static final DeferredBlock<ChairBlock> DARK_OAK_CHAIR = registerChair("dark_oak_chair");
    public static final DeferredBlock<ChairBlock> CRIMSON_CHAIR = registerChair("crimson_chair");
    public static final DeferredBlock<ChairBlock> WARPED_CHAIR = registerChair("warped_chair");
    public static final DeferredBlock<ChairBlock> MANGROVE_CHAIR = registerChair("mangrove_chair");
    public static final DeferredBlock<ChairBlock> PUMPKIN_CHAIR = registerChair("pumpkin_chair");
    public static final DeferredBlock<TableBlock> OAK_TABLE = registerTable("oak_table");
    public static final DeferredBlock<TableBlock> SPRUCE_TABLE = registerTable("spruce_table");
    public static final DeferredBlock<TableBlock> BIRCH_TABLE = registerTable("birch_table");
    public static final DeferredBlock<TableBlock> JUNGLE_TABLE = registerTable("jungle_table");
    public static final DeferredBlock<TableBlock> ACACIA_TABLE = registerTable("acacia_table");
    public static final DeferredBlock<TableBlock> DARK_OAK_TABLE = registerTable("dark_oak_table");
    public static final DeferredBlock<TableBlock> CRIMSON_TABLE = registerTable("crimson_table");
    public static final DeferredBlock<TableBlock> WARPED_TABLE = registerTable("warped_table");
    public static final DeferredBlock<TableBlock> MANGROVE_TABLE = registerTable("mangrove_table");
    public static final DeferredBlock<TableBlock> PUMPKIN_TABLE = registerTable("pumpkin_table");
    public static final DeferredBlock<RoundTableBlock> OAK_ROUND_TABLE = registerRoundTable("oak_round_table");
    public static final DeferredBlock<RoundTableBlock> SPRUCE_ROUND_TABLE = registerRoundTable("spruce_round_table");
    public static final DeferredBlock<RoundTableBlock> BIRCH_ROUND_TABLE = registerRoundTable("birch_round_table");
    public static final DeferredBlock<RoundTableBlock> JUNGLE_ROUND_TABLE = registerRoundTable("jungle_round_table");
    public static final DeferredBlock<RoundTableBlock> ACACIA_ROUND_TABLE = registerRoundTable("acacia_round_table");
    public static final DeferredBlock<RoundTableBlock> DARK_OAK_ROUND_TABLE = registerRoundTable("dark_oak_round_table");
    public static final DeferredBlock<RoundTableBlock> CRIMSON_ROUND_TABLE = registerRoundTable("crimson_round_table");
    public static final DeferredBlock<RoundTableBlock> WARPED_ROUND_TABLE = registerRoundTable("warped_round_table");
    public static final DeferredBlock<RoundTableBlock> MANGROVE_ROUND_TABLE = registerRoundTable("mangrove_round_table");
    public static final DeferredBlock<GlassTableBlock> GLASS_TABLE = registerGlassTable("glass_table");
    public static final DeferredBlock<GlassRoundTableBlock> GLASS_ROUND_TABLE = registerGlassRoundTable("glass_round_table");
    public static final DeferredBlock<ArmChairBlock> ARM_CHAIR = registerArmChair("arm_chair");
    public static final DeferredBlock<BenchBlock> BENCH = registerBench("bench");

    private static DeferredBlock<ChairBlock> registerChair(String name) {
        return BLOCKS.register(
            name,
            () -> new ChairBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<TableBlock> registerTable(String name) {
        return BLOCKS.register(
            name,
            () -> new TableBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<RoundTableBlock> registerRoundTable(String name) {
        return BLOCKS.register(
            name,
            () -> new RoundTableBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<GlassTableBlock> registerGlassTable(String name) {
        return BLOCKS.register(
            name,
            () -> new GlassTableBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<GlassRoundTableBlock> registerGlassRoundTable(String name) {
        return BLOCKS.register(
            name,
            () -> new GlassRoundTableBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<ArmChairBlock> registerArmChair(String name) {
        return BLOCKS.register(
            name,
            () -> new ArmChairBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private static DeferredBlock<BenchBlock> registerBench(String name) {
        return BLOCKS.register(
            name,
            () -> new BenchBlock(
                BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
            )
        );
    }

    private BlockRegistry() {
    }
}
