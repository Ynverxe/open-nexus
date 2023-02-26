package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.board.DTNBoardManager;
import com.github.ynverxe.dtn.board.UpdateMechanism;
import com.github.ynverxe.dtn.game.GameInstance;
import com.github.ynverxe.dtn.game.GameRoom;
import com.github.ynverxe.dtn.match.Match;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.dtn.world.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class APlayerImpl implements APlayer {

    private final @NotNull String name;
    private final @NotNull UUID uuid;
    private @NotNull String lang;
    private volatile @NotNull WeakReference<Player> playerWeakReference;
    private @Nullable GameInstance playingGame;

    public APlayerImpl(Player player) {
        lang = "en";
        name = player.getName();
        uuid = player.getUniqueId();
        playerWeakReference = new WeakReference<>(player);
        board();
    }

    @NotNull @Override
    public String name() {
        return name;
    }

    @NotNull @Override
    public String lang() {
        return lang;
    }

    @Override
    public void setLang(@NotNull String lang) {
        if (lang.isEmpty()) {
            throw new IllegalArgumentException("lang is empty");
        }
        this.lang = Objects.requireNonNull(lang, "lang");
    }

    public void handleGameJoin(@NotNull GameInstance game) {
        if (!game.isPlayerInGame(this)) {
            throw new IllegalStateException("player is not in that room");
        }
        playingGame = game;
    }

    public void handleRoomLeave() {
        if (playingGame == null) {
            return;
        }
        if (playingGame.isPlayerInGame(this)) {
            throw new IllegalStateException("player still is in the room");
        }
        playingGame = null;
    }

    @Override
    public @Nullable GameRoom playingRoom() {
        return (playingGame != null) ? playingGame.room() : null;
    }

    @Override
    public @Nullable GameInstance playingGame() {
        return playingGame;
    }

    @Override
    public @Nullable Match playingMatch() {
        if (playingGame == null) {
            return null;
        }

        return playingGame.runningMatch();
    }

    @Override
    public @NotNull Optional<MatchPlayer> toMatchRepresent() {
        return Optional.ofNullable(MatchPlayer.resolveAPlayer(this));
    }

    @Override
    public @NotNull ComplexBoard board() {
        DTNBoardManager dtnBoardManager = DTNBoardManager.instance();
        ComplexBoard complexBoard = dtnBoardManager.get(uuid);
        if (complexBoard == null) {
            complexBoard = dtnBoardManager.createBoard(uuid, UpdateMechanism.TEAMS);
        }

        return complexBoard;
    }

    @Override
    public @NotNull UUID uuid() {
        return uuid;
    }

    @Override
    public @NotNull NamedPosition position() {
        if (!isOnline()) {
            World world = Bukkit.getWorlds().get(0);
            return Position.ofLocation(new Location(world, 0.0, 0.0, 0.0));
        }

        return Position.ofLocation(bukkitPlayer().getLocation());
    }

    @Override
    public void teleport(@NotNull Location location) {
        teleport(Position.ofLocation(location));
    }

    @Override
    public void teleport(@NotNull World world) {
        bukkitPlayer().teleport(world.getSpawnLocation());
    }

    @Override
    public void teleport(@NotNull World world, @NotNull Position position) {
        teleport(position.toNamedLocation(world));
    }

    @Override
    public void teleport(@NotNull NamedPosition namedPosition) {
        if (!isOnline()) {
            return;
        }

        bukkitPlayer().teleport(namedPosition.toLocation());
    }

    @Override
    public void teleport(@NotNull Position position) {
        teleport(world(), position);
    }

    @Override
    public @NotNull World world() {
        return bukkitPlayer().getWorld();
    }

    @Override
    public boolean isOnline() {
        try {
            bukkitPlayer();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public @NotNull Player bukkitPlayer() {
        Player player = playerWeakReference.get();
        if (player == null || !player.isOnline()) {
            player = Bukkit.getPlayer(uuid);
            if (player == null) {
                throw new RuntimeException("player is offline or was discarded");
            }
            playerWeakReference = new WeakReference<>(player);
        }
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        APlayerImpl aPlayer = (APlayerImpl) o;
        return uuid.equals(aPlayer.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
