package com.whitewhistle.bleedingedge.nbt;

import net.minecraft.nbt.NbtElement;

import java.util.function.Function;
import java.util.function.Supplier;

public interface NbtSerializable<T> {
    T create();

    T fromNbt(NbtElement nbt);

    NbtElement toNbt(T value);

    static <T> NbtSerializable<T>of(Function<NbtElement, T> fromNbt, Function<T, NbtElement> toNbt, Supplier<T> create) {
        return new NbtSerializable<T>() {
            @Override
            public T create() {
                return create.get();
            }

            @Override
            public T fromNbt(NbtElement nbt) {
                return fromNbt.apply(nbt);
            }

            @Override
            public NbtElement toNbt(T value) {
                return toNbt.apply(value);
            }
        };
    }
}