package com.github.ynverxe.dtn.currency;

import org.jetbrains.annotations.NotNull;

public interface CurrencyAccount {
    double getCurrency(@NotNull String p0);

    double subtractCurrency(@NotNull String p0, int p1);

    double addCurrency(@NotNull String p0, int p1);

    boolean hasCurrency(@NotNull String p0);

    boolean hasCurrency(@NotNull String p0, int p1);
}
