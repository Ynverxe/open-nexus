package com.github.ynverxe.dtn.board;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import java.util.Objects;
import java.util.List;
import java.util.Arrays;
import com.github.ynverxe.util.Pair;

public class ScoreEntryUpdater implements BoardEntryHandler {

    private static final Pair<String, String> BACKING_DIFFERENTIATORS = new Pair<>("first", "second");;
    
    @Override
    public void updateLines(DTNBoardImpl dtnBoard, String[] newLines) {
        this.updateLines(dtnBoard, Arrays.asList(newLines));
    }
    
    @Override
    public void updateLine(DTNBoardImpl dtnBoard, int index, String lineText) {
        this.updateLines(dtnBoard, dtnBoard.lines());
    }
    
    private void updateLines(DTNBoardImpl dtnBoard, List<String> newLines) {
        Scoreboard scoreboard = dtnBoard.scoreboard;
        Objective showing = dtnBoard.showing();
        String currentBackingDifferentiator = showing.getName();
        if (currentBackingDifferentiator.equals(ScoreEntryUpdater.BACKING_DIFFERENTIATORS.left())) {
            currentBackingDifferentiator = ScoreEntryUpdater.BACKING_DIFFERENTIATORS.right();
        } else {
            currentBackingDifferentiator = ScoreEntryUpdater.BACKING_DIFFERENTIATORS.left();
        }

        Objective backing = scoreboard.registerNewObjective(currentBackingDifferentiator, "dummy");
        for (int i = 0; i < newLines.size(); ++i) {
            String line = newLines.get(i);
            if (line != null) {
                String current = dtnBoard.getLineText(i);
                if (Objects.equals(current, line)) {
                    return;
                }

                int maxScoreLineLength = BoardLimitation.CURRENT.getMaxScoreChars();
                if (line.length() > maxScoreLineLength) {
                    line = line.substring(0, maxScoreLineLength);
                }

                int score = newLines.size() - i;
                backing.getScore(line).setScore(score);
            }
        }

        backing.setDisplayName(showing.getDisplayName());
        backing.setDisplaySlot(DisplaySlot.SIDEBAR);
        dtnBoard.setShowing(backing);
        showing.unregister();
    }
}
