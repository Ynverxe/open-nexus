package com.github.ynverxe.util;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Random;

public final class RandomElementPicker {
    private static final Random RANDOM;
    
    @Nullable
    public static <E> E pickRandom(@NotNull List<E> list) {
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return list.get(RandomElementPicker.RANDOM.nextInt(list.size() - 1));
    }
    
    static {
        RANDOM = new Random();
    }
}
