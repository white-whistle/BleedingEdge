package com.whitewhistle.bleedingedge.util;

import java.util.List;
import java.util.Random;

public class RandomUtil {
    public static final Random r = new Random();
    public static float r0() {
        return 0.5f + (r.nextFloat() * 0.5f);
    }

    public static <T> T sample(List<T> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("List is empty");
        int index = r.nextInt(list.size());
        return list.get(index);
    }
}
