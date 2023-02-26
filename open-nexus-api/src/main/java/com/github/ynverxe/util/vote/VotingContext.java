package com.github.ynverxe.util.vote;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public interface VotingContext<E, K, V> {
    @Nullable V performVote(@NotNull E voter, @NotNull K candidateKey) throws IllegalArgumentException;

    @Nullable V voteOf(@NotNull E voter);

    @Nullable V candidateWithMostVotes();

    @NotNull Map<K, V> candidates();

    @NotNull Map<E, V> votes();

    void clear();

    static <E, K, V> @NotNull VotingContext<E, K, V> createContext(@NotNull Supplier<Map<K, V>> candidatesSupplier) {
        return new AbstractVotingContext<>(candidatesSupplier);
    }
}
