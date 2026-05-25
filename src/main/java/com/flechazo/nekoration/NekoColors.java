package com.flechazo.nekoration;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3d;

import java.awt.*;

public class NekoColors {
    public static int getBlockColorAt(int value, int min, int max, int minColor, int maxColor) {
        if (value >= max) return maxColor;
        if (value <= min) return minColor;
        double frac = ((double) value - (double) min) / ((double) max - (double) min);
        return getRGBColorBetween(frac, minColor, maxColor);
    }

    public static int getItemColor(ItemStack stack, int lc, int rc) {
        double frac = (double) stack.getCount() / stack.getMaxStackSize();
        return getRGBColorBetween(frac, lc, rc);
    }

    public static Color getRGBColor(Vec3i vec) {
        return new Color(clamp(vec.getX()), clamp(vec.getY()), clamp(vec.getZ()));
    }

    public static Color getRGBColor(Vector3d vec) {
        return new Color(clamp((int) vec.x), clamp((int) vec.y), clamp((int) vec.z));
    }

    public static Color getRGBColor(double col) {
        int col1 = (int) col;
        return new Color((col1 & 0xff0000) >> 16, (col1 & 0xff00) >> 8, col1 & 0xff);
    }

    public static Color getRGBColor(int col) {
        return new Color((col & 0xff0000) >> 16, (col & 0xff00) >> 8, col & 0xff);
    }

    public static int[] getRGBArray(int col) {
        return new int[]{(col & 0xff0000) >> 16, (col & 0xff00) >> 8, col & 0xff};
    }

    public static int getRGBColorBetween(double frac, int lc, int rc) {
        int red1 = (lc & 0xff0000) >> 16;
        int green1 = (lc & 0xff00) >> 8;
        int blue1 = lc & 0xff;
        int red2 = (rc & 0xff0000) >> 16;
        int green2 = (rc & 0xff00) >> 8;
        int blue2 = rc & 0xff;
        int red3 = (int) Mth.lerp(frac, red1, red2);
        int green3 = (int) Mth.lerp(frac, green1, green2);
        int blue3 = (int) Mth.lerp(frac, blue1, blue2);
        return (red3 << 16) + (green3 << 8) + blue3;
    }

    public static Color getRGBColorBetween(double frac, Color lc, Color rc) {
        return new Color(
                (int) Mth.lerp(frac, lc.getRed(), rc.getRed()),
                (int) Mth.lerp(frac, lc.getGreen(), rc.getGreen()),
                (int) Mth.lerp(frac, lc.getBlue(), rc.getBlue()));
    }

    public static int getRed(int c) {
        return (c & 0xff0000) >> 16;
    }

    public static int getGreen(int c) {
        return (c & 0xff00) >> 8;
    }

    public static int getBlue(int c) {
        return c & 0xff;
    }

    public static float getRedf(int c) {
        return (float) ((c & 0xff0000) >> 16) / 255.0F;
    }

    public static float getGreenf(int c) {
        return (float) ((c & 0xff00) >> 8) / 255.0F;
    }

    public static float getBluef(int c) {
        return (float) (c & 0xff) / 255.0F;
    }

    private static int clamp(int v) {
        return Math.min(Math.max(v, 0), 255);
    }

    public static EnumStoneColor getStoneFromNeko(EnumNekoColor col) {
        return EnumStoneColor.getColorEnumFromID(col.getNBTId());
    }

    public static int getNekoColorOrWhite(int id) {
        return EnumNekoColor.getColorValueFromID((byte) id);
    }

    public static int getStoneColorOrLightGray(int id) {
        return EnumStoneColor.getColorValueFromID((byte) id);
    }

    public static int getWoodenColorOrBrown(int id) {
        return EnumWoodenColor.getColorValueFromID((byte) id);
    }

    // Shared interface for all Neko color enums
    public interface INekoColor extends StringRepresentable {
        int getNBTId();

        int getColor();

        default float getPropertyOverrideValue() {
            return getNBTId();
        }

        @Override
        default String getSerializedName() {
            return toString();
        }

        default void putIntoNBT(CompoundTag tag, String key) {
            tag.putInt(key, getNBTId());
        }
    }

    // Shared static helpers for enum ID lookups
    private static <T extends INekoColor> T byId(T[] values, int id, T fallback) {
        for (T c : values) {
            if (c.getNBTId() == id) return c;
        }
        return fallback;
    }

    private static <T extends INekoColor> int colorById(T[] values, int id, int fallback) {
        for (T c : values) {
            if (c.getNBTId() == id) return c.getColor();
        }
        return fallback;
    }

    private static <T extends INekoColor> T fromNBT(T[] values, T fallback, CompoundTag tag, String key) {
        byte id = 0;
        if (tag.contains(key)) {
            id = tag.getByte(key);
        }
        return byId(values, id, fallback);
    }

    // Neko Colors
    public enum EnumNekoColor implements INekoColor {
        BLACK((byte) 0, "black", 0x5c5c5c),
        BLUE((byte) 1, "blue", 0x2891ff),
        BROWN((byte) 2, "brown", 0x673400),
        CYAN((byte) 3, "cyan", 0x94e2ff),
        GRAY((byte) 4, "gray", 0x9f9f9f),
        GREEN((byte) 5, "green", 0x33b54c),
        LIGHT_BLUE((byte) 6, "light_blue", 0x75aaff),
        LIGHT_GRAY((byte) 7, "light_gray", 0xbebebe),
        LIME((byte) 8, "lime", 0x7aff8f),
        MAGENTA((byte) 9, "magenta", 0xf976ff),
        ORANGE((byte) 10, "orange", 0xff7700),
        PINK((byte) 11, "pink", 0xffa3e0),
        PURPLE((byte) 12, "purple", 0xbc61ff),
        RED((byte) 13, "red", 0xe03f3f),
        WHITE((byte) 14, "white", 0xffffff),
        YELLOW((byte) 15, "yellow", 0xffc80a);

        private final byte nbtID;
        private final String name;
        private final int color;

        EnumNekoColor(byte nbtID, String name, int color) {
            this.nbtID = nbtID;
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public int getNBTId() {
            return nbtID;
        }

        @Override
        public int getColor() {
            return color;
        }

        public static EnumNekoColor getColorEnumFromID(byte id) {
            return byId(values(), id, LIGHT_GRAY);
        }

        public static int getColorValueFromID(byte id) {
            return colorById(values(), id, LIGHT_GRAY.color);
        }

        public static EnumNekoColor fromNBT(CompoundTag tag, String key) {
            return NekoColors.fromNBT(values(), LIGHT_GRAY, tag, key);
        }
    }

    // Stone Colors
    public enum EnumStoneColor implements INekoColor {
        BLACK((byte) 0, "black", 0x5c5c5c),
        BLUE((byte) 1, "blue", 0x549ae5),
        BROWN((byte) 2, "brown", 0xa37864),
        CYAN((byte) 3, "cyan", 0x8ed2ed),
        GRAY((byte) 4, "gray", 0x9f9f9f),
        GREEN((byte) 5, "green", 0x50ae5f),
        LIGHT_BLUE((byte) 6, "light_blue", 0x7eaeff),
        LIGHT_GRAY((byte) 7, "light_gray", 0xbebebe),
        LIME((byte) 8, "lime", 0x8de996),
        MAGENTA((byte) 9, "magenta", 0xe58dea),
        ORANGE((byte) 10, "orange", 0xeb9965),
        PINK((byte) 11, "pink", 0xe79acd),
        PURPLE((byte) 12, "purple", 0xb180d7),
        RED((byte) 13, "red", 0xe15252),
        WHITE((byte) 14, "white", 0xfef8ec),
        YELLOW((byte) 15, "yellow", 0xf1cf7b);

        private final byte nbtID;
        private final String name;
        private final int color;

        EnumStoneColor(byte nbtID, String name, int color) {
            this.nbtID = nbtID;
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public int getNBTId() {
            return nbtID;
        }

        @Override
        public int getColor() {
            return color;
        }

        public static EnumStoneColor getColorEnumFromID(int id) {
            return byId(values(), id, LIGHT_GRAY);
        }

        public static int getColorValueFromID(byte id) {
            return colorById(values(), id, LIGHT_GRAY.color);
        }
    }

    // Wooden Colors
    public enum EnumWoodenColor implements INekoColor {
        BLACK((byte) 0, "black", 0x5c3c1b),
        BLUE((byte) 1, "blue", 0x446184),
        BROWN((byte) 2, "brown", 0x886541),
        CYAN((byte) 3, "cyan", 0x389a99),
        GRAY((byte) 4, "gray", 0xb38564),
        GREEN((byte) 5, "green", 0x179930),
        LIGHT_BLUE((byte) 6, "light_blue", 0x4d71b0),
        LIGHT_GRAY((byte) 7, "light_gray", 0xb9955b),
        LIME((byte) 8, "lime", 0x9fad81),
        MAGENTA((byte) 9, "magenta", 0x873468),
        ORANGE((byte) 10, "orange", 0xb4653a),
        PINK((byte) 11, "pink", 0x9a4a45),
        PURPLE((byte) 12, "purple", 0x7b688c),
        RED((byte) 13, "red", 0x9c2525),
        WHITE((byte) 14, "white", 0xe8d699),
        YELLOW((byte) 15, "yellow", 0xcd9144);

        private final byte nbtID;
        private final String name;
        private final int color;

        EnumWoodenColor(byte nbtID, String name, int color) {
            this.nbtID = nbtID;
            this.name = name;
            this.color = color;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public int getNBTId() {
            return nbtID;
        }

        @Override
        public int getColor() {
            return color;
        }

        public static EnumWoodenColor getColorEnumFromID(byte id) {
            return byId(values(), id, BROWN);
        }

        public static int getColorValueFromID(byte id) {
            return colorById(values(), id, BROWN.color);
        }

        public static EnumWoodenColor fromNBT(CompoundTag tag, String key) {
            return NekoColors.fromNBT(values(), BROWN, tag, key);
        }
    }
}
