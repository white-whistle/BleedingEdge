package com.whitewhistle.bleedingedge.items;

import com.whitewhistle.bleedingedge.BleedingEdge;
import com.whitewhistle.bleedingedge.items.impl.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedList;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class ModItems {
    public static LinkedList<Item> modItems = new LinkedList<>();

    // creative
    public static final Item SUPPLY_DROP = registerItem("supply_drop", new SupplyDropItem(new FabricItemSettings().maxCount(1)));

    // tools
    public static final Item HEATING_SLOT = registerItem("heating_slot", new HeatingSlotItem(new FabricItemSettings().maxCount(1)));
    public static final Item REPAIR_SLOT = registerItem("repair_slot", new RepairSlotItem(new FabricItemSettings().maxCount(1)));
    public static final Item BRAIN_JAR = registerItem("brain_jar", new BrainJarItem(new FabricItemSettings().maxCount(1)));
    public static final Item BASIC_SLOT = registerItem("basic_slot", new SlotItem(new FabricItemSettings().maxCount(1)));

    // gadgets
    public static final Item CLOAKING_DEVICE = registerItem("cloaking_device", new CloakingDeviceItem(new FabricItemSettings().maxCount(1)));
    public static final Item SHIELD_GENERATOR = registerItem("shield_generator", new ShieldGeneratorItem(new FabricItemSettings().maxCount(1)));
    public static final Item NIGHT_VISION_GOGGLES = registerItem("night_vision_goggles", new NightVisionGogglesItem(new FabricItemSettings().maxCount(1)));
    public static final Item THREAT_VISOR = registerItem("threat_visor", new ThreatVisorItem(new FabricItemSettings().maxCount(1))); // TODO: render threat
    public static final Item HOVER_PACK = registerItem("hover_pack", new HoverPackItem(new FabricItemSettings().maxCount(1)));
    public static final Item LIQUID_COOLING = registerItem("liquid_cooling", new LiquidCoolingItem(new FabricItemSettings().maxCount(1)));
    public static final Item STEEL_KIDNEY = registerItem("steel_kidney", new SteelKidneyItem(new FabricItemSettings().maxCount(1)));
    public static final Item OBSIDIAN_SASH = registerItem("obsidian_sash", new ObsidianSashItem(new FabricItemSettings().maxCount(1)));
    public static final Item KEVLAR_TOTEM = registerItem("kevlar_totem", new KevlarTotemItem(new FabricItemSettings().maxCount(1)));
    public static final Item TESLA_PACK = registerItem("tesla_pack", new TeslaPackItem(new FabricItemSettings().maxCount(1))); // zap yourself and nearby entities, zapped entities affected by EMP
    public static final Item BRAIN_DRILL = registerItem("brain_drill", new BrainDrillItem(new FabricItemSettings().maxCount(1))); // brain extraction device, harms entities without helmets, damage is blocked by helmets but damages durability faster


    // ingredients
    public static final Item CARBON_INGOT = registerItem("carbon_ingot", new Item(new FabricItemSettings()));
    public static final Item SMALL_BRAIN = registerItem("small_brain", new BrainItem(new FabricItemSettings(),1));
    public static final Item ROTTEN_BRAIN = registerItem("rotten_brain", new BrainItem(new FabricItemSettings(),3));
    public static final BrainItem BRAIN = registerItem("brain", new BrainItem(new FabricItemSettings(), 10));
    public static final Item STEEL_BRAIN = registerItem("steel_brain", new BrainItem(new FabricItemSettings(), 25));
    public static final Item EUREKA = registerItem("eureka", new Item(new FabricItemSettings()));
    public static final Item TECHNOLOGY = registerItem("technology", new Item(new FabricItemSettings())); // craft science with iron and carbon

    // weapons
    public static final Item BREACH_HAMMER = registerItem("breach_hammer", new BreachHammer(ModToolMaterials.CARBON, 2, 26.5f, -3.3f, new FabricItemSettings().maxCount(1)));
    public static final Item STORM_BENDER = registerItem("storm_bender", new StormBenderItem(new FabricItemSettings().maxCount(1))); // calls lightning to self, shoots out EMP projectile (like wardens) maybe call it storm rail instead? looks likea bow so storm bender is more fitting

    // consumable
    public static final Item NUTRIENT_BLOCK = registerItem("nutrient_block", new Item(new FabricItemSettings().food(new FoodComponent.Builder().snack().saturationModifier(2f).hunger(2).build())));


    // TODO
    // tools
    public static final Item ASSEMBLY_SLOT = registerItem("assembly_slot", new SlotItem(new FabricItemSettings().maxCount(1)));

    // // consumable
    // public static final Item STIM = registerItem("stim", new Item(new FabricItemSettings().maxCount(1)));

    // gadgets

    public static final Item CREEPER_SANDALS = registerItem("creeper_sandals", new Item(new FabricItemSettings().maxCount(1))); // + belt slots
    public static final Item SLIME_SOCKS = registerItem("slime_socks", new Item(new FabricItemSettings().maxCount(1))); // + belt slots
    public static final Item MEDICORE = registerItem("medicore", new Item(new FabricItemSettings().maxCount(1))); // + belt slots
    public static final Item HEAVYCORE = registerItem("heavycore", new Item(new FabricItemSettings().maxCount(1))); // for tanks, + back slots - belt slots
    public static final Item TACTICORE = registerItem("tacticore", new Item(new FabricItemSettings().maxCount(1))); // + face + legs + belt
    public static final Item SPIDERCORE = registerItem("spidercore", new Item(new FabricItemSettings().maxCount(1))); // + face + legs

    // projectile
    // public static final Item ANTIGRAVITY_MINE = registerItem("antigravity_mine", new Item(new FabricItemSettings().maxCount(1)));
    // misc
    // public static final Item REMOTE_DETONATOR = registerItem("remote_detonator", new Item(new FabricItemSettings().maxCount(1))); // maybe repurpose to activate bound toggleable items? i think we will just use hotkeys so maybe we scrap this


    public static <T extends Item> T registerItem(String name, T item) {
        modItems.push(item);
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
    }

    public static void registerModItems() {
        BleedingEdge.LOGGER.info("Register Items for:" + MOD_ID);
    }
}
