package com.github.ynverxe.dtn.currency;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ComposedCurrencyPrice implements CurrencyPrice, Iterable<CurrencyPrice> {
    private final List<CurrencyPrice> currencyPrices;

    public ComposedCurrencyPrice() {
        currencyPrices = new ArrayList<>();
    }

    ComposedCurrencyPrice(CurrencyPrice caller) {
        (currencyPrices = new ArrayList<>()).add(caller);
    }

    @Override
    public double amount() {
        return -1.0;
    }

    @Override
    public boolean canWithdraw(@NotNull CurrencyAccount currencyAccount) {
        for (CurrencyPrice currencyPrice : currencyPrices) {
            if (!currencyPrice.canWithdraw(currencyAccount)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean withdraw(@NotNull CurrencyAccount currencyAccount) {
        final boolean canWithdraw = canWithdraw(currencyAccount);
        if (canWithdraw) {
            for (CurrencyPrice currencyPrice : currencyPrices) {
                currencyPrice.withdraw(currencyAccount);
            }
        }
        return canWithdraw;
    }

    public void addPrice(@NotNull CurrencyPrice currencyPrice) {
        currencyPrices.add(currencyPrice);
    }

    public void removePrice(@NotNull CurrencyPrice currencyPrice) {
        currencyPrices.remove(currencyPrice);
    }

    @NotNull @Override
    public Iterator<CurrencyPrice> iterator() {
        return currencyPrices.iterator();
    }

    public @NotNull List<CurrencyPrice> asList() {
        return Collections.unmodifiableList(currencyPrices);
    }
}
