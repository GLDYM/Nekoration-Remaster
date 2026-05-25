package com.flechazo.nekoration.blocks.containers;

import com.flechazo.nekoration.Nekoration;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuType {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Nekoration.MODID);

    public static final RegistryObject<MenuType<EaselMenuMenu>> EASEL_MENU_TYPE = MENU_TYPES.register("easel_menu", () -> IForgeMenuType.create(EaselMenuMenu::new));
}
