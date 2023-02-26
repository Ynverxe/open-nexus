package com.github.ynverxe.dtn.kit;

import com.github.ynverxe.translation.resource.ResourceReference;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KitDescription {

    private final ResourceReference<String> kitDescription;
    private final String ownerIdentifier;
    private final String author;

    public KitDescription(ResourceReference<String> kitDescription, String ownerIdentifier, String author) {
        this.kitDescription = Objects.requireNonNull(kitDescription, "kitDescription");
        this.ownerIdentifier = Objects.requireNonNull(ownerIdentifier, "ownerIdentifier");
        this.author = Objects.requireNonNull(author, "author");
    }

    public @NotNull ResourceReference<String> kitDescription() {
        return kitDescription;
    }

    public @NotNull String ownerIdentifier() {
        return ownerIdentifier;
    }

    public @NotNull String author() {
        return author;
    }
}
