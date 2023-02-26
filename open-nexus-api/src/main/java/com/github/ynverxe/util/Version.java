package com.github.ynverxe.util;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Version {
    private static final Pattern PATTERN;

    static {
        PATTERN = Pattern.compile("(\\d+)");
    }

    private final String serverPackageVersion;
    private int majorVersionNumber;
    private int minorVersionNumber;

    public Version(String serverPackageVersion) {
        this.serverPackageVersion = serverPackageVersion;
        Matcher matcher = Version.PATTERN.matcher(serverPackageVersion);
        boolean invalidFormat = !matcher.find();
        if (!invalidFormat) {
            majorVersionNumber = Integer.parseInt(matcher.group());
            invalidFormat = !matcher.find();
            if (!invalidFormat) {
                minorVersionNumber = Integer.parseInt(matcher.group());
            }
        }
        if (invalidFormat) {
            throw new RuntimeException("Invalid format: " + serverPackageVersion);
        }
    }

    public static @NotNull Version current() {
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        return new Version(version);
    }

    public @NotNull String minecraftVersion() {
        return serverPackageVersion;
    }

    public int majorVersionNumber() {
        return majorVersionNumber;
    }

    public int minorVersionNumber() {
        return minorVersionNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Version version = (Version) o;
        return majorVersionNumber == version.majorVersionNumber && minorVersionNumber == version.minorVersionNumber && serverPackageVersion.equals(version.serverPackageVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverPackageVersion, majorVersionNumber, minorVersionNumber);
    }
}
