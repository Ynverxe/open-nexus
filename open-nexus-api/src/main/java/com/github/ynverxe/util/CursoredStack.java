package com.github.ynverxe.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CursoredStack<T> {

    private final List<T> values;
    private final int cursorLimit;
    private int cursor;

    public CursoredStack(List<T> values) {
        this.values = new ArrayList<>(values);
        cursor = 0;
        cursorLimit = values.size() - 1;
    }

    public @NotNull T peek() {
        if (cursor == cursorLimit) {
            throw new IndexOutOfBoundsException(String.format("Current: %s, Limit: %s", cursor, cursorLimit));
        }

        return values.get(++cursor);
    }

    public @NotNull T peekOrReset() {
        if (isCursorOnTop()) {
            return resetCursor();
        }

        return peek();
    }

    public @NotNull T current() {
        return values.get(cursor);
    }

    public @NotNull T previous() {
        if (cursor == 0) {
            throw new IndexOutOfBoundsException("Cursor is on the bottom of the stack");
        }

        return values.get(--cursor);
    }

    public boolean isCursorOnTop() {
        return cursor == cursorLimit;
    }

    public boolean isCursorOnBottom() {
        return cursor == 0;
    }

    public @NotNull T resetCursor() {
        cursor = 0;
        return current();
    }

    public @NotNull List<T> values() {
        return Collections.unmodifiableList(values);
    }
}
