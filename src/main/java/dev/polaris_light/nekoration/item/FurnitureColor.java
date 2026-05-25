package dev.polaris_light.nekoration.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum FurnitureColor {
    BLACK(0, "black", 0x5C3C1B, Items.BLACK_DYE),
    BLUE(1, "blue", 0x446184, Items.BLUE_DYE),
    BROWN(2, "brown", 0x886541, Items.BROWN_DYE),
    CYAN(3, "cyan", 0x389A99, Items.CYAN_DYE),
    GRAY(4, "gray", 0xB38564, Items.GRAY_DYE),
    GREEN(5, "green", 0x179930, Items.GREEN_DYE),
    LIGHT_BLUE(6, "light_blue", 0x4D71B0, Items.LIGHT_BLUE_DYE),
    LIGHT_GRAY(7, "light_gray", 0xB9955B, Items.LIGHT_GRAY_DYE),
    LIME(8, "lime", 0x9FAD81, Items.LIME_DYE),
    MAGENTA(9, "magenta", 0x873468, Items.MAGENTA_DYE),
    ORANGE(10, "orange", 0xB4653A, Items.ORANGE_DYE),
    PINK(11, "pink", 0x9A4A45, Items.PINK_DYE),
    PURPLE(12, "purple", 0x7B688C, Items.PURPLE_DYE),
    RED(13, "red", 0x9C2525, Items.RED_DYE),
    WHITE(14, "white", 0xE8D699, Items.WHITE_DYE),
    YELLOW(15, "yellow", 0xCD9144, Items.YELLOW_DYE);

    public static final FurnitureColor DEFAULT = BROWN;

    private final int id;
    private final String name;
    private final int color;
    private final Item dye;

    FurnitureColor(int id, String name, int color, Item dye) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.dye = dye;
    }

    public int id() {
        return this.id;
    }

    public String serializedName() {
        return this.name;
    }

    public int color() {
        return this.color;
    }

    public Item dye() {
        return this.dye;
    }

    public static FurnitureColor byId(int id) {
        for (FurnitureColor color : values()) {
            if (color.id == id) {
                return color;
            }
        }
        return DEFAULT;
    }
}
