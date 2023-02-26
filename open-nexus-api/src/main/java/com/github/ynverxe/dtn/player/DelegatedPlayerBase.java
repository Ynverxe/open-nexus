package com.github.ynverxe.dtn.player;

import com.github.ynverxe.dtn.board.ComplexBoard;
import com.github.ynverxe.dtn.world.NamedPosition;
import com.github.ynverxe.dtn.world.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DelegatedPlayerBase implements PlayerBase {

    private final PlayerBase delegate;

    public DelegatedPlayerBase(PlayerBase delegate) {
        this.delegate = delegate;
    }

    @NotNull @Override
    public UUID uuid() {
        return delegate.uuid();
    }

    @NotNull @Override
    public String name() {
        return delegate.name();
    }

    @NotNull @Override
    public Player bukkitPlayer() {
        return delegate.bukkitPlayer();
    }

    @NotNull @Override
    public ComplexBoard board() {
        return delegate.board();
    }

    @NotNull @Override
    public NamedPosition position() {
        return delegate.position();
    }

    @Override
    public @NotNull World world() {
        return delegate.world();
    }

    @Override
    public boolean isOnline() {
        return delegate.isOnline();
    }

    @Override
    public void setLang(@NotNull String lang) {
        delegate.setLang(lang);
    }

    @Override
    public void teleport(@NotNull Location location) {
        delegate.teleport(location);
    }

    @Override
    public void teleport(@NotNull World aWorld) {
        delegate.teleport(aWorld);
    }

    @Override
    public void teleport(@NotNull Position position) {
        delegate.teleport(position);
    }

    @Override
    public void teleport(@NotNull World aWorld, @NotNull Position position) {
        delegate.teleport(aWorld, position);
    }

    @Override
    public void teleport(@NotNull NamedPosition namedPosition) {
        delegate.teleport(namedPosition);
    }

    @NotNull @Override
    public String lang() {
        return delegate.lang();
    }
}
