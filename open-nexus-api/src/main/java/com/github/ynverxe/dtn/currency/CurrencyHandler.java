package com.github.ynverxe.dtn.currency;

import org.jetbrains.annotations.NotNull;

public interface CurrencyHandler {
    @NotNull String name();

    double get(@NotNull CurrencyAccount p0);

    boolean hasAmount(double p0, @NotNull CurrencyAccount p1);

    default void subtract(double amount, @NotNull CurrencyAccount currencyHolder) {
    }

    default void add(double amount, @NotNull CurrencyAccount currencyHolder) {
    }
}
