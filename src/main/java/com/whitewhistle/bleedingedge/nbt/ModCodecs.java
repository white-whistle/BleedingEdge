package com.whitewhistle.bleedingedge.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModCodecs {

    public static NbtSerializable<Integer> INT = NbtSerializable.of(
            (nbt) -> ((NbtInt)nbt).intValue(),
            NbtInt::of,
            () -> 0
    );

    public static NbtSerializable<Boolean> BOOLEAN = NbtSerializable.of(
            (nbt) -> ((NbtByte)nbt).byteValue() != 0,
            NbtByte::of,
            () -> false
    );

    public static NbtSerializable<UUID> UUID = NbtSerializable.of(
            NbtHelper::toUuid,
            NbtHelper::fromUuid,
            java.util.UUID::randomUUID
    );

    public static NbtSerializable<ItemStack> ITEMSTACK = NbtSerializable.of(
            nbt -> ItemStack.fromNbt((NbtCompound) nbt),
            stack -> stack.writeNbt(new NbtCompound()),
            () -> ItemStack.EMPTY
    );

    public record List<T>(NbtSerializable<T> codec) implements NbtSerializable<java.util.List<T>> {

        @Override
        public java.util.List<T> create() {
            return java.util.List.of();
        }

        @Override
        public java.util.List<T> fromNbt(NbtElement nbt) {
            var nbtList = (NbtList) nbt;

            return nbtList.stream().map(codec::fromNbt).collect(Collectors.toList());
        }

        @Override
        public NbtElement toNbt(java.util.List<T> value) {
            var nbtList = new NbtList();

            value.forEach(v -> {
                nbtList.add(codec.toNbt(v));
            });

            return nbtList;
        }
    }

    public record Map<K, V>(NbtSerializable<K> key, NbtSerializable<V> value) implements NbtSerializable<java.util.Map<K,V>>{
        @Override
        public java.util.Map<K, V> create() {
            return new HashMap<>();
        }

        @Override
        public java.util.Map<K, V> fromNbt(NbtElement nbt) {
            var nbtList = (NbtList) nbt;
            var map = new HashMap<K, V>();

            nbtList.forEach(entry -> {
                var nbtEntry = (NbtCompound)entry;
                map.put(this.key.fromNbt(nbtEntry.get("k")), this.value.fromNbt(nbtEntry.get("v")));
            });

            return map;
        }

        @Override
        public NbtElement toNbt(java.util.Map<K, V> value) {
            var nbtList = new NbtList();

            value.forEach((k, v) -> {
                var nbt = new NbtCompound();

                nbt.put("k", this.key.toNbt(k));
                nbt.put("v", this.value.toNbt(v));

                nbtList.add(nbt);
            });

            return nbtList;
        }


    }

    public static Map<UUID, ItemStack> UUID_TO_ITEMSTACK = new Map<>(UUID, ITEMSTACK);
    public static Map<UUID, UUID> UUID_TO_UUID = new Map<>(UUID, UUID);
    public static List<ItemStack> ITEMSTACK_LIST = new List<>(ITEMSTACK);
}
