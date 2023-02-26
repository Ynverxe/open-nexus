package com.github.ynverxe.dtn.board;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import com.github.ynverxe.util.Pair;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import java.util.Objects;

public class TeamEntryHandler implements BoardEntryHandler {
    @Override
    public void updateLines(DTNBoardImpl dtnBoard, String[] newLines) {
        for (int i = 0; i < newLines.length; ++i) {
            final String lineText = newLines[i];
            final String current = dtnBoard.getLineText(i);
            if (!Objects.equals(current, lineText)) {
                this.updateLine(dtnBoard, i, lineText);
            }
        }
    }
    
    @Override
    public void updateLine(DTNBoardImpl dtnBoard, int index, String newEntry) {
        final Scoreboard scoreboard = dtnBoard.scoreboard;
        final String name = DTNBoardImpl.INVISIBLE_NAMES[index];
        Team team = scoreboard.getTeam(name);
        if (newEntry != null) {
            boolean needsDisplay = false;
            if (team == null) {
                team = scoreboard.registerNewTeam(name);
                needsDisplay = true;
            }
            final Pair<String, String> parts = this.formatTeamParts(newEntry);
            team.setPrefix(parts.left());
            team.setSuffix(parts.right());
            if (needsDisplay) {
                final int score = dtnBoard.lines().size() - index;
                team.addEntry(name);
                final Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
                objective.getScore(name).setScore(score);
            }
        }
        else {
            if (team == null) {
                return;
            }
            team.unregister();
            scoreboard.resetScores(name);
        }
    }
    
    private Pair<String, String> formatTeamParts(String text) {
        BoardLimitation limitation = BoardLimitation.CURRENT;
        int maxCharsPerPart = limitation.getMaxTeamPartChars();

        if (text.length() <= maxCharsPerPart) {
            return new Pair<>(text, "");
        }

        String prefix = text.substring(0, maxCharsPerPart);
        String suffix = text.substring(maxCharsPerPart);
        return this.fixColorCodesOnParts(prefix, suffix, maxCharsPerPart);
    }
    
    private Pair<String, String> fixColorCodesOnParts(String prefix, String suffix, int maxCharsPerPart) {
        String colorChar = "§";
        StringBuilder prefixBuilder = new StringBuilder(prefix);
        StringBuilder suffixBuilder = new StringBuilder(suffix);

        if (prefix.endsWith(colorChar)) {
            prefixBuilder.deleteCharAt(prefix.length() - 1);
            suffixBuilder.insert(0, colorChar);
        } else if (!suffix.startsWith(colorChar)) {
            String lastColors = ChatColor.getLastColors(prefix);
            suffixBuilder.insert(0, lastColors);
        }

        if (prefixBuilder.length() > maxCharsPerPart) {
            prefixBuilder.delete(maxCharsPerPart, prefixBuilder.length());
        }

        if (suffixBuilder.length() > maxCharsPerPart) {
            suffixBuilder.delete(maxCharsPerPart, suffixBuilder.length());
        }

        return new Pair<>(prefixBuilder.toString(), suffixBuilder.toString());
    }
}
