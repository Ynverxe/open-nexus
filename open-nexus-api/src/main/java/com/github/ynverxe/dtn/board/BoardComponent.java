package com.github.ynverxe.dtn.board;

import com.github.ynverxe.util.CursoredStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class BoardComponent {

    private final CursoredStack<String> frames;
    private final int updateInterval;
    private int updateCalls;

    public BoardComponent(List<String> frames, int updateInterval) {
        this.frames = new CursoredStack<>(frames);
        this.updateInterval = updateInterval;
    }

    public static @NotNull BoardComponent of(int updateInterval, @NotNull Object... frames) {
        final List<String> stringFrames = Arrays.stream(frames).map(String::valueOf).collect(Collectors.toList());
        return new BoardComponent(stringFrames, updateInterval);
    }

    public static @NotNull BoardComponent of(int updateInterval, @NotNull List<String> frames) {
        return new BoardComponent(new ArrayList<>(frames), updateInterval);
    }

    public static @NotNull BoardComponent ofData(Object componentData) {
        if (componentData instanceof BoardComponent) {
            return (BoardComponent) componentData;
        }
        if (componentData instanceof String) {
            return new BoardComponent(Collections.singletonList((String) componentData), 20);
        }
        if (componentData instanceof List) {
            return new BoardComponent((List<String>) componentData, 20);
        }

        return fromMap((Map<String, Object>) componentData);
    }

    public @NotNull static BoardComponent fromMap(@NotNull Map<String, Object> objectMap) {
        int updateInterval = (int) objectMap.get("updateInterval");
        List<String> frames = (List<String>) objectMap.get("frames");

        return new BoardComponent(frames, updateInterval);
    }

    public int updateInterval() {
        return updateInterval;
    }

    public @NotNull CursoredStack<String> frames() {
        return frames;
    }

    public boolean canUpdate() {
        final boolean b = ++updateCalls >= updateInterval;
        if (b) {
            updateCalls = 0;
        }
        return b;
    }

    @Override
    public String toString() {
        return "BoardComponent{updateCalls=" + updateCalls + ", frames=" + frames + ", updateInterval=" + updateInterval + '}';
    }

    public @NotNull BoardComponent copy() {
        return new BoardComponent(new ArrayList<>(frames.values()), updateInterval);
    }

    public @NotNull Map<String, Object> toMap() {
        final Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("updateInterval", updateInterval);
        objectMap.put("frames", frames.values());
        return objectMap;
    }
}
