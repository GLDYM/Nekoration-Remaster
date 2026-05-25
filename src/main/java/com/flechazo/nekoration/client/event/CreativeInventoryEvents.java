package com.flechazo.nekoration.client.event;

import com.flechazo.nekoration.NekoColors.EnumNekoColor;
import com.flechazo.nekoration.NekoColors.EnumWoodenColor;
import com.flechazo.nekoration.Nekoration;
import com.flechazo.nekoration.blocks.ModBlocks;
import com.flechazo.nekoration.client.gui.widget.FilterButton;
import com.flechazo.nekoration.client.gui.widget.IconButton;
import com.flechazo.nekoration.items.DyeableBlockItem;
import com.flechazo.nekoration.utils.ItemIconHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Creative Screen Things, adapted from MrCrayfish's Furniture Mod...
public class CreativeInventoryEvents {
    private static final ResourceLocation ICONS = new ResourceLocation(Nekoration.MODID, "textures/gui/icons.png");
    private static int woodStartIndex;
    private static int decorStartIndex;

    private List<TagFilter<EnumWoodenColor>> woodFilters;
    private List<FilterButton> woodButtons;
    private List<TagFilter<EnumDecorType>> decorFilters;
    private List<FilterButton> decorButtons;
    private Button btnScrollUp;
    private Button btnScrollDown;
    private Button btnEnableAll;
    private Button btnDisableAll;
    private boolean viewingWoodTab;
    private boolean viewingDecorTab;
    private int guiCenterX = 0;
    private int guiCenterY = 0;

    @SubscribeEvent
    public void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        this.woodFilters = null;
        this.decorFilters = null;
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen) {
            if (this.woodFilters == null)
                this.compileWoodItems();

            if (this.decorFilters == null)
                this.compileDecorItems();

            this.viewingWoodTab = false;
            this.viewingDecorTab = false;
            this.guiCenterX = ((CreativeModeInventoryScreen) event.getScreen()).getGuiLeft();
            this.guiCenterY = ((CreativeModeInventoryScreen) event.getScreen()).getGuiTop();
            this.woodButtons = new ArrayList<>();
            this.decorButtons = new ArrayList<>();

            event.addListener(this.btnScrollUp = new IconButton(this.guiCenterX - 22, this.guiCenterY - 12, Component.translatable("gui.nekoration.button.scroll_up"), button -> {
                if (viewingWoodTab) {
                    if (woodStartIndex > 0)
                        woodStartIndex--;
                    this.updateWoodTagButtons();
                }
                if (viewingDecorTab) {
                    if (decorStartIndex > 0)
                        decorStartIndex--;
                    this.updateDecorTagButtons();
                }
            }, ICONS, 64, 0));

            event.addListener(this.btnScrollDown = new IconButton(this.guiCenterX - 22, this.guiCenterY + 127, Component.translatable("gui.nekoration.button.scroll_down"), button -> {
                if (viewingWoodTab) {
                    if (woodStartIndex <= woodFilters.size() - 4 - 1)
                        woodStartIndex++;
                    this.updateWoodTagButtons();
                }
                if (viewingDecorTab) {
                    if (decorStartIndex <= decorFilters.size() - 4 - 1)
                        decorStartIndex++;
                    this.updateDecorTagButtons();
                }
            }, ICONS, 80, 0));

            event.addListener(this.btnEnableAll = new IconButton(this.guiCenterX + 32, this.guiCenterY - 50, Component.translatable("gui.nekoration.button.enable_all"), button -> {
                if (viewingWoodTab) {
                    this.woodFilters.forEach(filters -> filters.setEnabled(true));
                    this.woodButtons.forEach(FilterButton::updateState);
                } else if (viewingDecorTab) {
                    this.decorFilters.forEach(filters -> filters.setEnabled(true));
                    this.decorButtons.forEach(FilterButton::updateState);
                }
                Screen screen = Minecraft.getInstance().screen;
                if (screen instanceof CreativeModeInventoryScreen) {
                    if (viewingWoodTab)
                        this.updateWoodItems((CreativeModeInventoryScreen) screen);
                    else if (viewingDecorTab)
                        this.updateDecorItems((CreativeModeInventoryScreen) screen);
                }
            }, ICONS, 96, 0));

            event.addListener(this.btnDisableAll = new IconButton(this.guiCenterX + 144, this.guiCenterY - 50, Component.translatable("gui.nekoration.button.disable_all"), button -> {
                Screen screen = Minecraft.getInstance().screen;
                if (viewingWoodTab) {
                    this.woodFilters.forEach(filters -> filters.setEnabled(false));
                    this.woodButtons.forEach(FilterButton::updateState);
                    if (screen instanceof CreativeModeInventoryScreen) {
                        this.updateWoodItems((CreativeModeInventoryScreen) screen);
                    }
                } else if (viewingDecorTab) {
                    this.decorFilters.forEach(filters -> filters.setEnabled(false));
                    this.decorButtons.forEach(FilterButton::updateState);
                    if (screen instanceof CreativeModeInventoryScreen) {
                        this.updateDecorItems((CreativeModeInventoryScreen) screen);
                    }
                }
            }, ICONS, 112, 0));

            this.btnScrollUp.visible = false;
            this.btnScrollDown.visible = false;
            this.btnEnableAll.visible = false;
            this.btnDisableAll.visible = false;

            this.updateWoodTagButtons();
            this.updateDecorTagButtons();

            /* CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) event.getScreen();
            if(screen. == ModItemTabs.WOODEN_GROUP){ // Check validity
                this.btnScrollUp.visible = this.btnScrollDown.visible = true;
                this.btnEnableAll.visible = this.btnDisableAll.visible = true;
                this.viewingWoodTab = true;
                this.viewingDecorTab = false;
                this.woodButtons.forEach(button -> button.visible = true);
                this.updateWoodItems(screen);
            } else if (screen. == ModItemTabs.DECOR_GROUP){
                this.btnScrollUp.visible = this.btnScrollDown.visible = true;
                this.btnEnableAll.visible = this.btnDisableAll.visible = true;
                this.viewingDecorTab = true;
                this.viewingWoodTab = false;
                this.decorButtons.forEach(button -> button.visible = true);
                this.updateDecorItems(screen);
            } */
        }
    }

    @SubscribeEvent
    public void onScreenClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_LEFT)
            return;

        if (event.getScreen() instanceof CreativeModeInventoryScreen) {
            if (viewingWoodTab)
                for (Button button : this.woodButtons)
                    if (button.isMouseOver(event.getMouseX(), event.getMouseY()))
                        if (button.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton()))
                            return;

            if (viewingDecorTab)
                for (Button button : this.decorButtons)
                    if (button.isMouseOver(event.getMouseX(), event.getMouseY()))
                        if (button.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton()))
                            return;
        }
    }

    @SubscribeEvent
    public void onScreenDrawPre(ScreenEvent.Render.Pre event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen) {
            /* CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) event.getScreen();

            if(screen. == ModItemTabs.WOODEN_GROUP){
                if(!this.viewingWoodTab){
                    this.updateWoodItems(screen);
                    this.viewingWoodTab = true;
                    this.viewingDecorTab = false;
                    // Update up/down buttons...
                    this.btnScrollUp.active = woodStartIndex > 0;
                    this.btnScrollDown.active = woodStartIndex <= this.woodFilters.size() - 4 - 1;
                }
            } else if(screen. == ModItemTabs.DECOR_GROUP){
                if(!this.viewingDecorTab){
                    this.updateDecorItems(screen);
                    this.viewingWoodTab = false;
                    this.viewingDecorTab = true;
                    // Update up/down buttons...
                    this.btnScrollUp.active = decorStartIndex > 0;
                    this.btnScrollDown.active = decorStartIndex <= this.decorFilters.size() - 4 - 1;
                }
            } else {
                this.viewingWoodTab = false;
                this.viewingDecorTab = false;
            } */
        }
    }

    @SubscribeEvent
    public void onScreenDrawPost(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen screen) {
            this.guiCenterX = screen.getGuiLeft();
            this.guiCenterY = screen.getGuiTop();

            /* if(screen. == ModItemTabs.WOODEN_GROUP || screen. == ModItemTabs.DECOR_GROUP){
                this.btnScrollUp.visible = true;
                this.btnScrollDown.visible = true;
                this.btnEnableAll.visible = true;
                this.btnDisableAll.visible = true;
                if (viewingWoodTab){
                    this.woodButtons.forEach(button -> button.visible = true);
                    // Render buttons
                    this.woodButtons.forEach(button -> {
                        button.render(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
                    });
                    // Render tooltips after so it renders above buttons
                    this.woodButtons.forEach(button -> {
                        if(button.isMouseOver(event.getMouseX(), event.getMouseY())){
                            screen.renderTooltip(event.getPoseStack(), button.getCategory().getName(), event.getMouseX(), event.getMouseY());
                        }
                    });
                } else if (viewingDecorTab){
                    this.decorButtons.forEach(button -> button.visible = true);
                    // Render buttons
                    this.decorButtons.forEach(button -> {
                        button.render(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
                    });
                    // Render tooltips after so it renders above buttons
                    this.decorButtons.forEach(button -> {
                        if(button.isMouseOver(event.getMouseX(), event.getMouseY())){
                            screen.renderTooltip(event.getPoseStack(), button.getCategory().getName(), event.getMouseX(), event.getMouseY());
                        }
                    });
                }
                if(this.btnEnableAll.isMouseOver(event.getMouseX(), event.getMouseY())){
                    screen.renderTooltip(event.getPoseStack(), this.btnEnableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }
                if(this.btnDisableAll.isMouseOver(event.getMouseX(), event.getMouseY())){
                    screen.renderTooltip(event.getPoseStack(), this.btnDisableAll.getMessage(), event.getMouseX(), event.getMouseY());
                }
            } else {
                this.btnScrollUp.visible = false;
                this.btnScrollDown.visible = false;
                this.btnEnableAll.visible = false;
                this.btnDisableAll.visible = false;
                this.woodButtons.forEach(button -> button.visible = false);
                this.decorButtons.forEach(button -> button.visible = false);
            } */
        }
    }

    private void updateWoodTagButtons() {
        final Button.OnPress pressable = button -> {
            Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof CreativeModeInventoryScreen) {
                this.updateWoodItems((CreativeModeInventoryScreen) screen);
            }
        };
        this.woodButtons.clear();
        for (int i = woodStartIndex; i < woodStartIndex + 4 && i < this.woodFilters.size(); i++) {
            FilterButton button = new FilterButton(this.guiCenterX - 28, this.guiCenterY + 29 * (i - woodStartIndex) + 10, this.woodFilters.get(i), pressable);
            this.woodButtons.add(button);
        }
        this.btnScrollUp.active = woodStartIndex > 0;
        this.btnScrollDown.active = woodStartIndex <= this.woodFilters.size() - 4 - 1;
    }

    private void updateDecorTagButtons() {
        final Button.OnPress pressable = button -> {
            Screen screen = Minecraft.getInstance().screen;
            if (screen instanceof CreativeModeInventoryScreen) {
                this.updateDecorItems((CreativeModeInventoryScreen) screen);
            }
        };
        this.decorButtons.clear();
        for (int i = decorStartIndex; i < decorStartIndex + 4 && i < this.decorFilters.size(); i++) {
            FilterButton button = new FilterButton(this.guiCenterX - 28, this.guiCenterY + 29 * (i - decorStartIndex) + 10, this.decorFilters.get(i), pressable);
            this.decorButtons.add(button);
        }
        this.btnScrollUp.active = decorStartIndex > 0;
        this.btnScrollDown.active = decorStartIndex <= this.decorFilters.size() - 4 - 1;
    }

    private void updateWoodItems(CreativeModeInventoryScreen screen) {
        CreativeModeInventoryScreen.ItemPickerMenu container = screen.getMenu();
        NonNullList<ItemStack> newItems = NonNullList.create();

        /*
        ForgeRegistries.ITEMS.getValues().stream()
        .filter(item -> item.getItemCategory() == ModItemTabs.WOODEN_GROUP)
        .forEach(item -> {
            for(WoodFilter filter : woodFilters){
                if (filter.isEnabled() && item instanceof HalfTimberBlockItem){
                    ((HalfTimberBlockItem)item).fillItemCategoryWithWoodType(ModItemTabs.WOODEN_GROUP, filter.getWood(), newItems);
                }
            }
        }); */

        container.items.clear();
        container.items.addAll(newItems);
        container.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem())));
        container.scrollTo(0);
    }

    private void updateDecorItems(CreativeModeInventoryScreen screen) {
        CreativeModeInventoryScreen.ItemPickerMenu container = screen.getMenu();
        NonNullList<ItemStack> newItems = NonNullList.create();

        /*
        ForgeRegistries.ITEMS.getValues().stream()
        .filter(item -> item.getItemCategory() == ModItemTabs.DECOR_GROUP)
        .forEach(item -> {
            boolean t0, t1, t2, t3 = false;
            String itemId = ForgeRegistries.ITEMS.getKey(item).getPath();
            if ((t0 = item == ModBlocks.STONE_POT.get().asItem() ||
                item == ModBlocks.STONE_PLANTER.get().asItem()) && decorFilters.get(0).enabled)
                item.fillItemCategory(ModItemTabs.DECOR_GROUP, newItems);
            else if ((t1 = item == ModBlocks.AWNING_PURE.get().asItem() ||
                    item == ModBlocks.AWNING_PURE_SHORT.get().asItem() ||
                    item == ModBlocks.AWNING_STRIPE.get().asItem() ||
                    item == ModBlocks.AWNING_STRIPE_SHORT.get().asItem()) && decorFilters.get(1).enabled)
                item.fillItemCategory(ModItemTabs.DECOR_GROUP, newItems);
            else if ((t2 = itemId.endsWith("table") || itemId.endsWith("chair") || itemId.endsWith("bench")) && decorFilters.get(2).enabled)
                item.fillItemCategory(ModItemTabs.DECOR_GROUP, newItems);
            else if ((t3 = item == ModBlocks.EASEL_MENU.get().asItem() ||
                    item == ModBlocks.EASEL_MENU_WHITE.get().asItem() ||
                    item == ModBlocks.DRAWER.get().asItem() ||
                    item == ModBlocks.CABINET.get().asItem() ||
                    item == ModBlocks.DRAWER_CHEST.get().asItem() ||
                    item == ModBlocks.CUPBOARD.get().asItem() ||
                    item == ModBlocks.SHELF.get().asItem() ||
                    item == ModBlocks.WALL_SHELF.get().asItem()) && decorFilters.get(3).enabled)
                item.fillItemCategory(ModItemTabs.DECOR_GROUP, newItems);
            else if (!(t0 || t1 || t2 || t3) && decorFilters.get(4).enabled) // Misc
                item.fillItemCategory(ModItemTabs.DECOR_GROUP, newItems);
        });*/

        container.items.clear();
        container.items.addAll(newItems);
        container.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem())));
        container.scrollTo(0);
    }

    private void compileWoodItems() {
        // Vanilla Wood Type
        final var OAK = new TagFilter<>(EnumWoodenColor.LIGHT_GRAY, "color.wooden.", new ItemStack(Blocks.OAK_PLANKS));
        final var BIRCH = new TagFilter<>(EnumWoodenColor.WHITE, "color.wooden.", new ItemStack(Blocks.BIRCH_PLANKS));
        final var ACACIA = new TagFilter<>(EnumWoodenColor.ORANGE, "color.wooden.", new ItemStack(Blocks.ACACIA_PLANKS));
        final var JUNGLE = new TagFilter<>(EnumWoodenColor.GRAY, "color.wooden.", new ItemStack(Blocks.JUNGLE_PLANKS));
        final var SPRUCE = new TagFilter<>(EnumWoodenColor.BROWN, "color.wooden.", new ItemStack(Blocks.SPRUCE_PLANKS));
        final var WARPED = new TagFilter<>(EnumWoodenColor.CYAN, "color.wooden.", new ItemStack(Blocks.WARPED_PLANKS));
        final var CRIMSON = new TagFilter<>(EnumWoodenColor.MAGENTA, "color.wooden.", new ItemStack(Blocks.CRIMSON_PLANKS));
        final var DARK_OAK = new TagFilter<>(EnumWoodenColor.BLACK, "color.wooden.", new ItemStack(Blocks.DARK_OAK_PLANKS));
        final var MANGROVE = new TagFilter<>(EnumWoodenColor.PINK, "color.wooden.", ItemIconHelper.getCustomBlockItem(10003));
        final var MAGIC = new TagFilter<>(EnumWoodenColor.LIGHT_BLUE, "color.wooden.", ItemIconHelper.getCustomBlockItem(10001));
        final var WILLOW = new TagFilter<>(EnumWoodenColor.LIME, "color.wooden.", ItemIconHelper.getCustomBlockItem(10002));
        final var UMBRAN = new TagFilter<>(EnumWoodenColor.PURPLE, "color.wooden.", ItemIconHelper.getCustomBlockItem(10004));
        final var CHERRY = new TagFilter<>(EnumWoodenColor.RED, "color.wooden.", ItemIconHelper.getCustomBlockItem(10005));
        final var PALM = new TagFilter<>(EnumWoodenColor.YELLOW, "color.wooden.", ItemIconHelper.getCustomBlockItem(10006));

        this.woodFilters = new ArrayList<>(Arrays.asList(OAK, BIRCH, ACACIA, JUNGLE, SPRUCE, WARPED, CRIMSON, DARK_OAK));
        this.woodFilters.addAll(Arrays.asList(MANGROVE, MAGIC, WILLOW, UMBRAN, CHERRY, PALM));
    }

    private void compileDecorItems() {
        // Prepare icon itemstacks...
        final ItemStack planterIcon = new ItemStack(ModBlocks.STONE_POT.get());
        DyeableBlockItem.setColor(planterIcon, EnumNekoColor.WHITE);
        final ItemStack awningIcon = new ItemStack(ModBlocks.AWNING_STRIPE_SHORT.get());
        DyeableBlockItem.setColor(awningIcon, EnumNekoColor.WHITE);
        final ItemStack containerIcon = new ItemStack(ModBlocks.DRAWER_CHEST.get());
        DyeableBlockItem.setColor(containerIcon, EnumNekoColor.ORANGE);
        final ItemStack miscIcon = new ItemStack(ModBlocks.CANDLE_HOLDER_GOLD.get());
        DyeableBlockItem.setColor(miscIcon, EnumNekoColor.WHITE);

        final var PLANTER = new TagFilter<>(EnumDecorType.PLANTER, "decortype.", planterIcon);
        final var AWNING = new TagFilter<>(EnumDecorType.AWNING, "decortype.", awningIcon);
        final var FURNITURE = new TagFilter<>(EnumDecorType.FURNITURE, "decortype.", new ItemStack(ModBlocks.SPRUCE_TABLE.get()));
        final var CONTAINER = new TagFilter<>(EnumDecorType.CONTAINER, "decortype.", containerIcon);
        final var MISC = new TagFilter<>(EnumDecorType.MISC, "decortype.", miscIcon);

        this.decorFilters = new ArrayList<>();
        this.decorFilters.addAll(Arrays.asList(PLANTER, AWNING, FURNITURE, CONTAINER, MISC));
    }

    public interface Filter {
        void setEnabled(boolean enabled);

        boolean isEnabled();

        Component getName();

        ItemStack getIcon();
    }

    public static class TagFilter<T extends StringRepresentable> implements Filter {
        private final T type;
        private final Component name;
        private final ItemStack icon;
        private boolean enabled = true;

        public TagFilter(T type, String namePrefix, ItemStack icon) {
            this.type = type;
            this.name = Component.translatable(namePrefix + type.getSerializedName().replace("/", "."));
            this.icon = icon;
        }

        public T getType() {
            return type;
        }

        @Override
        public ItemStack getIcon() {
            return this.icon;
        }

        @Override
        public Component getName() {
            return this.name;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean isEnabled() {
            return this.enabled;
        }
    }

    public enum EnumDecorType implements StringRepresentable {
        PLANTER("planter"),
        AWNING("awning"),
        FURNITURE("furniture"),
        CONTAINER("container"),
        MISC("misc");

        private final String name;

        EnumDecorType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
