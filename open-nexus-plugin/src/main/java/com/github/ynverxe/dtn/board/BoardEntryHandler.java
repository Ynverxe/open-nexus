package com.github.ynverxe.dtn.board;

public interface BoardEntryHandler {
    void updateLines(DTNBoardImpl p0, String[] p1);
    
    void updateLine(DTNBoardImpl p0, int p1, String p2);
}
