package com.whitewhistle.bleedingedge.util;

import java.util.UUID;

import static com.whitewhistle.bleedingedge.BleedingEdge.MOD_ID;

public class UuidUtil {
    public static UUID from(String name) {
        return UUID.nameUUIDFromBytes((MOD_ID + ":" + name).getBytes());
    }
}