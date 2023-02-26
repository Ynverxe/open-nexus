package com.github.ynverxe.util.time;

import java.util.concurrent.TimeUnit;

public final class TimeFormatter {
    private TimeFormatter() {
    }

    public static String formatTime(TimeUnit baseUnit, long units, char unitSeparator, TimeUnit... timeUnits) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < timeUnits.length; ++i) {
            final TimeUnit timeUnit = timeUnits[i];
            if (units <= 0L) {
                builder.append("00");
            } else {
                final long convertedTimeBase = timeUnit.convert(units, baseUnit);
                units -= baseUnit.convert(convertedTimeBase, timeUnit);
                String convertedTimeText = convertedTimeBase + "";
                if (convertedTimeText.length() == 1) {
                    convertedTimeText = "0" + convertedTimeText;
                }
                builder.append(convertedTimeText);
            }
            if (i < timeUnits.length - 1) {
                builder.append(unitSeparator);
            }
        }
        return builder.toString();
    }
}
