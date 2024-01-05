package dev.imlukas.partycrackersplugin.util.time;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private TimeUtil() {}

    public static String getTimeFormatted(boolean includeTimezone) {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter;
        if (includeTimezone) {
            formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        } else {
            formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss");
        }

        return now.format(formatter);
    }
}
