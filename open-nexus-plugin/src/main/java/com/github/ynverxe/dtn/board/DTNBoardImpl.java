package com.github.ynverxe.dtn.board;

import org.bukkit.ChatColor;
import java.util.Collections;
import java.util.Arrays;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.Bukkit;
import java.util.UUID;
import org.bukkit.scoreboard.Objective;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class DTNBoardImpl implements DTNBoard {

    protected static final String[] INVISIBLE_NAMES;
    private final String[] lineNames;
    final Scoreboard scoreboard;
    private volatile Player player;
    private final BoardEntryHandler entryHandler;
    private volatile Objective showing;
    private final CompositeTextInterceptor textInterceptor;
    private final UUID uuid;
    
    public DTNBoardImpl(BoardEntryHandler entryHandler, UUID uuid) {
        this.lineNames = new String[15];
        this.textInterceptor = new CompositeTextInterceptor();
        this.uuid = uuid;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.entryHandler = entryHandler;
        this.showing = this.scoreboard.registerNewObjective("objective", "dummy");
        this.showing.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.setLine(14, "Line #1");
    }
    
    @NotNull
    public UUID id() {
        return this.uuid;
    }
    
    public void setTitle(@NotNull String title) {
        title = this.interceptText(title);
        this.showing.setDisplayName(title);
    }
    
    public void setLine(int position, @NotNull String newLine) {
        this.checkIndexRange(position);
        String current = this.lineNames[position];
        newLine = this.interceptText(newLine);
        if (!Objects.equals(current, newLine)) {
            this.entryHandler.updateLine(this, position, newLine);
            this.lineNames[position] = newLine;
        }
    }
    
    public void setLines(@NotNull List<String> lines) {
        if (lines.size() > 15) {
            throw new IllegalArgumentException("Lines cannot be more than 15");
        }
        final String[] newLines = new String[15];
        for (int i = 0; i < this.lineNames.length; ++i) {
            String newLine = (i >= lines.size()) ? null : lines.get(i);
            if (newLine != null) {
                newLine = this.interceptText(newLine);
            }
            newLines[i] = newLine;
        }
        this.entryHandler.updateLines(this, newLines);
        System.arraycopy(newLines, 0, this.lineNames, 0, newLines.length);
    }
    
    @Nullable
    public String removeLine(int position) {
        this.checkIndexRange(position);
        final String removed = this.lineNames[position];
        this.lineNames[position] = null;
        this.entryHandler.updateLine(this, position, null);
        return removed;
    }

    public @Nullable String getLineText(int position) {
        this.checkIndexRange(position);
        return this.lineNames[position];
    }
    
    @NotNull
    public List<String> lines() {
        return Collections.unmodifiableList(Arrays.asList(this.lineNames));
    }
    
    public void clearLines() {
        Arrays.fill(this.lineNames, null);
        this.entryHandler.updateLines(this, this.lineNames);
    }
    
    public void display(@NotNull Player player) {
        if (this.player != null) {
            this.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        player.setScoreboard(this.scoreboard);
        this.player = player;
    }
    
    @NotNull
    public CompositeTextInterceptor compositeTextInterceptor() {
        return this.textInterceptor;
    }
    
    public boolean isDisplayed() {
        return this.player != null && this.player.isOnline();
    }
    
    @NotNull
    public Player player() {
        return this.player;
    }
    
    @NotNull
    public Objective showing() {
        return this.showing;
    }
    
    public void setShowing(@NotNull Objective showing) {
        this.showing = showing;
    }
    
    protected void checkIndexRange(int index) {
        if (index < 0 || index >= 15) {
            throw new IllegalArgumentException("Index cannot be less than zero or major than 15. '" + index + "'");
        }
    }
    
    private String interceptText(String title) {
        if (this.player == null) {
            return title;
        }
        title = this.textInterceptor.apply(this.player, title);
        if (title == null) {
            throw new NullPointerException("Interceptor returns null");
        }
        return title;
    }
    
    static {
        INVISIBLE_NAMES = new String[15];
        final ChatColor[] values = ChatColor.values();
        for (int i = 0; i < 15; ++i) {
            DTNBoardImpl.INVISIBLE_NAMES[i] = values[i].toString() + ChatColor.RESET;
        }
    }
}
