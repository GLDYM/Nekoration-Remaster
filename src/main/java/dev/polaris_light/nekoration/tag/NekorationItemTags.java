package dev.polaris_light.nekoration.tag;

import dev.polaris_light.nekoration.Nekoration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class NekorationItemTags {
    public static final TagKey<Item> GLASS_TABLES = create("furniture/glass_tables");
    public static final TagKey<Item> GLASS_ROUND_TABLES = create("furniture/glass_round_tables");
    public static final TagKey<Item> ARM_CHAIRS = create("furniture/arm_chairs");
    public static final TagKey<Item> BENCHES = create("furniture/benches");
    public static final TagKey<Item> CUPBOARDS = create("furniture/cupboards");
    public static final TagKey<Item> SHELVES = create("furniture/shelves");
    public static final TagKey<Item> WALL_SHELVES = create("furniture/wall_shelves");

    private NekorationItemTags() {
    }

    private static TagKey<Item> create(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Nekoration.MODID, path));
    }
}
