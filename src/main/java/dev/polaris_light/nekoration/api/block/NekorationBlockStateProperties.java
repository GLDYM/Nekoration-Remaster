package dev.polaris_light.nekoration.api.block;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class NekorationBlockStateProperties {
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 15);
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION =
        EnumProperty.create("vertical_connection", VerticalConnection.class);
    public static final EnumProperty<HorizontalConnection> HORIZONTAL_CONNECTION =
        EnumProperty.create("horizontal_connection", HorizontalConnection.class);

    private NekorationBlockStateProperties() {
    }
}
