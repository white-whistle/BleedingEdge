package com.whitewhistle.bleedingedge.items;

import com.whitewhistle.bleedingedge.BleedingEdge;
import com.whitewhistle.bleedingedge.items.impl.CloakingDeviceItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedList;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModItems {
    public static LinkedList<Item> modItems = new LinkedList<>();

    // gadgets
    public static final Item TACTICORE = registerItem("tacticore", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item CLOAKING_DEVICE = registerItem("cloaking_device", new CloakingDeviceItem(new FabricItemSettings().maxCount(1)));
    public static final Item SHIELD_GENERATOR = registerItem("shield_generator", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item REMOTE_DETONATOR = registerItem("remote_detonator", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item NIGHT_VISION_GOGGLES = registerItem("night_vision_goggles", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item HOVER_PACK = registerItem("hover_pack", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item HOVER_MINE = registerItem("hover_mine", new Item(new FabricItemSettings().maxCount(1)));


    public static <T extends Item> T registerItem(String name, T item) {
        modItems.push(item);
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
    }

    public static void registerModItems() {
        BleedingEdge.LOGGER.info("Register Items for:" + MOD_ID);
    }
}
