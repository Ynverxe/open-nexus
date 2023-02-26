package com.github.ynverxe.dtn.board;

import com.github.ynverxe.util.CursoredStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class SimpleComplexBoard extends DTNBoardImpl implements ComplexBoard {

    private volatile BoardComponent title;
    private final BoardComponent[] boardComponents;
    
    public SimpleComplexBoard(BoardEntryHandler entryHandler, UUID uuid) {
        super(entryHandler, uuid);
        this.title = BoardComponent.of(20,"Awesome title");
        this.boardComponents = new BoardComponent[15];
        this.compositeTextInterceptor().addInterceptor(new ColorTextInterceptor()).addInterceptor(new PAPILineInterceptor());
    }
    
    public void setTitleComponent(@NotNull BoardComponent boardComponent) {
        this.title = boardComponent;
        this.setTitle(boardComponent.frames().current());
    }
    
    @NotNull
    public ComplexBoard setLineComponent(int index, @NotNull BoardComponent boardComponent) {
        this.checkIndexRange(index);
        this.boardComponents[index] = boardComponent;
        return this;
    }
    
    @NotNull
    public ComplexBoard setLineComponents(@NotNull List<BoardComponent> components) {
        Arrays.fill(this.boardComponents, null);
        for (int i = 0; i < components.size(); ++i) {
            this.boardComponents[i] = components.get(i);
        }

        return this;
    }
    
    public void run() {
        List<String> currentFrames = new ArrayList<>();
        if (this.title.canUpdate()) {
            CursoredStack<String> stack = this.title.frames();
            this.setTitle(stack.peekOrReset());
        }

        for (BoardComponent boardComponent : this.boardComponents) {
            if (boardComponent != null) {
                if (boardComponent.canUpdate()) {
                    CursoredStack<String> frames = boardComponent.frames();
                    String frame = frames.peekOrReset();

                    currentFrames.add(frame);
                }
            }
        }

        if (!currentFrames.isEmpty()) {
            this.setLines(currentFrames);
        }
    }
}
