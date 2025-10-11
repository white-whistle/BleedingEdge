package com.whitewhistle.bleedingedge.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record ItemComponent<T>(Identifier identifier, NbtSerializable<T> codec, Supplier<T> create) {
    public ItemComponent(Identifier identifier, NbtSerializable<T> codec) {
        this(identifier, codec, codec::create);
    }

    public T get(NbtCompound nbt) {
        if (nbt == null || !nbt.contains(identifier.toString())) {
            return this.create.get();
        }

        var tag = nbt.get(identifier.toString());

        return codec.fromNbt(tag);
    }

    public T get(ItemStack stack) {
        var nbt = stack.getNbt();

        return get(nbt);
    }

    public void set(ItemStack stack, T data) {
        var nbt = stack.getOrCreateNbt();

        var tag = codec.toNbt(data);

        nbt.put(identifier.toString(), tag);

        stack.setNbt(nbt);
    }

    public boolean has(ItemStack stack) {
        var nbt = stack.getNbt();
        if (nbt == null) return false;

        return nbt.contains(identifier.toString());
    }

    public void remove(ItemStack stack) {
        var nbt = stack.getNbt();
        if (nbt == null) return;

        nbt.remove(identifier.toString());
    }

    public void update(ItemStack stack, Consumer<T> consumer) {
        var data = get(stack);

        consumer.accept(data);

        set(stack, data);
    }

    public T map(ItemStack stack, Function<T, T> mapper) {
        var data = get(stack);

        data = mapper.apply(data);

        set(stack, data);

        return data;
    }
}
