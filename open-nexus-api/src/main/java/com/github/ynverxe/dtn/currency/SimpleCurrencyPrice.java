package com.github.ynverxe.dtn.currency;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class SimpleCurrencyPrice implements CurrencyPrice {

    private final double amount;
    private final CurrencyHandler currencyHandler;

    SimpleCurrencyPrice(double amount, CurrencyHandler currencyHandler) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount cannot be less than zero");
        }
        this.amount = amount;
        this.currencyHandler = Objects.requireNonNull(currencyHandler, "currencyHandler");
    }

    public @NotNull CurrencyHandler currencyHandler() {
        return currencyHandler;
    }

    @Override
    public double amount() {
        return amount;
    }

    @Override
    public boolean canWithdraw(@NotNull CurrencyAccount currencyAccount) {
        return currencyHandler.hasAmount(amount, currencyAccount);
    }

    @Override
    public boolean withdraw(@NotNull CurrencyAccount currencyAccount) {
        final boolean canWithdraw = canWithdraw(currencyAccount);
        if (canWithdraw) {
            currencyHandler.subtract(amount, currencyAccount);
        }
        return canWithdraw;
    }
}
