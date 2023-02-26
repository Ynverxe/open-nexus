package com.github.ynverxe.dtn.board;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class CompositeTextInterceptor implements BiFunction<Player, String, String> {
    private final List<BiFunction<Player, String, String>> interceptors;

    public CompositeTextInterceptor() {
        interceptors = new ArrayList<>();
    }

    @Override
    public String apply(Player player, String s) {
        for (BiFunction<Player, String, String> interceptor : interceptors) {
            s = interceptor.apply(player, s);
        }
        return s;
    }

    public @NotNull CompositeTextInterceptor addInterceptor(BiFunction<Player, String, String> interceptor) {
        interceptors.add(interceptor);
        return this;
    }
}
