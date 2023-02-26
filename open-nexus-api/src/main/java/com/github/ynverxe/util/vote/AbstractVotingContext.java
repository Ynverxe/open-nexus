package com.github.ynverxe.util.vote;

import com.github.ynverxe.util.vote.VotingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class AbstractVotingContext<E, K, V> implements VotingContext<E, K, V> {

    private final Map<E, V> backing;
    private final Supplier<Map<K, V>> candidatesSupplier;

    protected AbstractVotingContext(Supplier<Map<K, V>> candidatesSupplier) {
        backing = new HashMap<>();
        this.candidatesSupplier = candidatesSupplier;
    }

    @Override
    public @Nullable V performVote(@NotNull E voter, @NotNull K candidateKey) throws IllegalArgumentException {
        Objects.requireNonNull(voter, "voter is null");
        V candidate = candidates().get(candidateKey);

        if (candidate != null) {
            backing.put(voter, candidate);
        }

        return candidate;
    }

    @Override
    public @Nullable V voteOf(@NotNull E voter) {
        return backing.get(voter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable V candidateWithMostVotes() {
        Map<K, V> candidatesSource = candidates();
        if (backing.isEmpty() && candidatesSource.isEmpty()) {
            return null;
        }

        Map<V, Integer> candidateVotes = new HashMap<>();
        for (V candidate : backing.values()) {
            int votes = candidateVotes.getOrDefault(candidate, 0);
            candidateVotes.put(candidate, ++votes);
        }

        V currentCandidate;
        if (candidateVotes.isEmpty()) {
            currentCandidate = (V) candidatesSource.values().toArray(new Object[0])[0];
        } else {
            currentCandidate = (V) candidateVotes.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).toArray()[0];
        }

        return currentCandidate;
    }

    @NotNull @Override
    public Map<K, V> candidates() {
        return candidatesSupplier.get();
    }

    @NotNull @Override
    public Map<E, V> votes() {
        return Collections.unmodifiableMap(backing);
    }

    @Override
    public void clear() {
        backing.clear();
    }
}
