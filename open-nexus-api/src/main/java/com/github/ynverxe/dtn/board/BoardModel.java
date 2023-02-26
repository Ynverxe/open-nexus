package com.github.ynverxe.dtn.board;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@SerializableAs("DTNBoard")
@SuppressWarnings("unchecked")
public class BoardModel implements ConfigurationSerializable {
    private final BoardComponent[] lineComponents;
    private BoardComponent titleComponent;

    public BoardModel() {
        titleComponent = BoardComponent.of(20, "Awesome Title!");
        lineComponents = new BoardComponent[15];
        setLineComponent(0, BoardComponent.of(20, "Awesome Line"));
    }

    public static @NotNull BoardModel deserialize(@NotNull Map<String, Object> objectMap) {
        BoardModel boardModel = new BoardModel();
        BoardComponent title = BoardComponent.ofData(objectMap.get("title"));
        boardModel.setTitleComponent(title);

        List<String> lines = (List<String>) objectMap.get("lines");

        if (lines == null) {
            for (int i = 1; i <= 15; ++i) {
                Object componentData = objectMap.get(i + "");
                if (componentData != null) {
                    BoardComponent component = BoardComponent.ofData(componentData);
                    boardModel.setLineComponent(i - 1, component);
                }
            }
        } else {
            List<BoardComponent> components = lines.stream().map(BoardComponent::ofData).collect(Collectors.toList());
            boardModel.setLineComponents(components);
        }

        return boardModel;
    }

    public @NotNull BoardModel setTitleComponent(@NotNull BoardComponent titleComponent) {
        this.titleComponent = titleComponent;
        return this;
    }

    public @NotNull BoardModel setTitleComponent(int updateInterval, @NotNull Object... objects) {
        titleComponent = BoardComponent.of(updateInterval, objects);
        return this;
    }

    public @NotNull BoardModel setLineComponent(int index, @NotNull BoardComponent lineComponent) {
        lineComponents[index] = lineComponent.copy();
        return this;
    }

    public @NotNull BoardModel setLineComponent(int index, int updateInterval, @NotNull Object... objects) {
        lineComponents[index] = BoardComponent.of(updateInterval, objects);
        return this;
    }

    public @NotNull BoardModel setLineComponents(@NotNull BoardComponent... boardComponents) {
        return setLineComponents(Arrays.asList(boardComponents));
    }

    public @NotNull BoardModel setLineComponents(@NotNull Object... boardComponents) {
        for (int i = 0; i < boardComponents.length; ++i) {
            lineComponents[i] = BoardComponent.ofData(boardComponents[i]);
        }
        return this;
    }

    public @NotNull BoardModel setLineComponents(@NotNull List<BoardComponent> boardComponents) {
        for (int i = 0; i < boardComponents.size(); ++i) {
            lineComponents[i] = boardComponents.get(i);
        }
        return this;
    }

    public @NotNull BoardComponent titleComponent() {
        return titleComponent;
    }

    public @NotNull List<BoardComponent> lineComponents() {
        return Collections.unmodifiableList(Arrays.asList(lineComponents));
    }

    public void applyToBoard(ComplexBoard... boards) {
        for (ComplexBoard board : boards) {
            board.setTitleComponent(titleComponent);

            board.setLineComponents(Arrays.asList(lineComponents));
        }
    }

    @Override
    public String toString() {
        return "BoardModel{titleComponent=" + titleComponent + ", lineComponents=" + Arrays.toString(lineComponents) + '}';
    }

    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("title", titleComponent.toMap());
        for (int i = 1; i <= lineComponents.length; ++i) {
            objectMap.put(i + "", lineComponents[i]);
        }
        return objectMap;
    }
}
