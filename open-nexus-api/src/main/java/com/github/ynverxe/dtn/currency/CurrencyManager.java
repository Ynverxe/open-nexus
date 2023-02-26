package com.github.ynverxe.dtn.currency;

import com.github.ynverxe.dtn.DestroyTheNexus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CurrencyManager implements Iterable<CurrencyHandler> {
    private final Map<String, CurrencyHandler> currencyHandlerMap;

    public CurrencyManager() {
        currencyHandlerMap = new HashMap<>();
    }

    @NotNull static CurrencyManager instance() {
        return DestroyTheNexus.instance().currencyManager();
    }

    public @NotNull SimpleCurrencyPrice createNewCurrencyPrice(double amount, @NotNull String currencyTypeName) {
        final CurrencyHandler currencyHandler = currencyHandlerMap.get(currencyTypeName);
        if (currencyHandler == null) {
            throw new IllegalArgumentException("No currency handler found with type name: " + currencyTypeName);
        }
        return new SimpleCurrencyPrice(amount, currencyHandler);
    }

    public void registerCurrencyHandlers(@NotNull CurrencyHandler... currencyHandlers) {
        for (CurrencyHandler currencyHandler : currencyHandlers) {
            currencyHandlerMap.put(currencyHandler.name(), currencyHandler);
        }
    }

    public @NotNull CurrencyHandler findHandler(@NotNull String typeName) {
        return currencyHandlerMap.get(typeName);
    }

    public @NotNull Map<String, CurrencyHandler> currencyHandlers() {
        return currencyHandlerMap;
    }

    @NotNull @Override
    public Iterator<CurrencyHandler> iterator() {
        return currencyHandlerMap.values().iterator();
    }
}
