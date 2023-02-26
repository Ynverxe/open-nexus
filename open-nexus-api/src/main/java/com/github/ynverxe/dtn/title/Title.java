package com.github.ynverxe.dtn.title;

import com.github.ynverxe.util.TextColorizer;
import org.jetbrains.annotations.NotNull;

public final class Title {

    private final String title;
    private final String subTitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public Title(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public Title(String title, int fadeIn, int stay, int fadeOut) {
        this(title, "", fadeIn, stay, fadeOut);
    }

    public Title(String title, String subTitle) {
        this(title, subTitle, 20, 20, 20);
    }

    public Title(String title) {
        this(title, "");
    }

    public String title() {
        return title;
    }

    public String subTitle() {
        return subTitle;
    }

    public int fadeIn() {
        return fadeIn;
    }

    public int stay() {
        return stay;
    }

    public int fadeOut() {
        return fadeOut;
    }

    public @NotNull Title colorize() {
        return new Title(TextColorizer.colorize(title), TextColorizer.colorize(subTitle), fadeIn, stay, fadeOut);
    }
}
