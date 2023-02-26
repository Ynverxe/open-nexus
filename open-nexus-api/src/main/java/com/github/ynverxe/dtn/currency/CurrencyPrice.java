package com.github.ynverxe.dtn.currency;

import org.jetbrains.annotations.NotNull;

public interface CurrencyPrice {
    double amount();

    boolean canWithdraw(@NotNull CurrencyAccount p0);

    boolean withdraw(@NotNull CurrencyAccount p0);

    @NotNull default ComposedCurrencyPrice compose() {
        return new ComposedCurrencyPrice(this);
    }

    @NotNull default SimpleCurrencyPrice newSimplePrice(int amount, @NotNull CurrencyHandler currencyHandler) {
        return new SimpleCurrencyPrice(amount, currencyHandler);
    }
}
