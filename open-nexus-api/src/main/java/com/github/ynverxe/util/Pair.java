package com.github.ynverxe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public final class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @SuppressWarnings("unchecked")
    public static <L, R> List<Pair<L, R>> fromObjects(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalStateException("objects must be divisible by 2");
        }

        List<Pair<L, R>> pairs = new ArrayList<>();
        for (int i = 0; i < objects.length; ++i) {
            Pair<L, R> pair = new Pair<>((L) objects[i++], (R) objects[i]);
            pairs.add(pair);
        }

        return pairs;
    }

    public L left() {
        return this.left;
    }

    public R right() {
        return this.right;
    }

    public <T> T map(BiFunction<L, R, T> mapper) {
        return mapper.apply(left, right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Pair<?, ?> pair = (Pair<?, ?>) o;
        return left.equals(pair.left) && right.equals(pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
