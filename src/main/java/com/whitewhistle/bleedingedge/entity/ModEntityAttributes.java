package com.whitewhistle.bleedingedge.entity;

import com.whitewhistle.bleedingedge.util.ModIdentifier;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;

public class ModEntityAttributes {
    public static final EntityAttribute CLOAKING = register("cloaking", new ClampedEntityAttribute(
            "attribute.bleeding-edge.generic.cloaking",
            0.0,   // default value
            0.0,   // minimum
            8 // maximum
    ).setTracked(true));

    public static final EntityAttribute SHIELD = register("shield", new ClampedEntityAttribute(
            "attribute.bleeding-edge.generic.shield",
            0.0,   // default value
            0.0,   // minimum
            256 // maximum
    ).setTracked(true));

    public static final EntityAttribute THREAT = register("threat", new ClampedEntityAttribute(
            "attribute.bleeding-edge.generic.threat",
            0.0,   // default value
            0.0,   // minimum
            1024 // maximum
    ).setTracked(true));

    public static final List<EntityAttribute> LIVING_ENTITY_ADDITIONAL_ATTRIBUTES = List.of(
            CLOAKING,
            SHIELD,
            THREAT
    );

    public static EntityAttribute register(String name, EntityAttribute attribute) {
        Registry.register(Registries.ATTRIBUTE, ModIdentifier.of(name), attribute);
        return attribute;
    }
}
