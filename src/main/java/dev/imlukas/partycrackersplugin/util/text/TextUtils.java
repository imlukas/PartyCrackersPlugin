package dev.imlukas.partycrackersplugin.util.text;


import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextUtils {

    private static final Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]){6}");

    private TextUtils() {
    }

    public static String color(String toColor) {
        Matcher matcher = hexPattern.matcher(toColor);
        while (matcher.find()) {
            String color = toColor.substring(matcher.start(), matcher.end());
            toColor = toColor.replace(color, ChatColor.of(color) + "");
            matcher = hexPattern.matcher(toColor);
        }

        return ChatColor.translateAlternateColorCodes('&', toColor);
    }

    public static List<String> color(List<String> strings) {
        List<String> colored = new ArrayList<>();

        for (String string : strings) {
            colored.add(color(string));
        }

        return colored;
    }

    public static String enumToText(Enum<?> enumToText) {
        return capitalize(enumToText.toString().replace("_", " "));
    }

    public static String capitalize(String toCapitalize) {
        return toCapitalize.substring(0, 1).toUpperCase() + toCapitalize.substring(1).toLowerCase();
    }

    /**
     * Parses a String to an integer, throwing an IllegalArgumentException if the String is not a valid integer.
     *
     * @param stringToParse The String to parse
     * @param predicate     A Predicate to test the parsed integer against
     * @return The parsed integer
     */
    public static Optional<Integer> parseInt(String stringToParse, Predicate<Integer> predicate) {
        int parsed;
        try {
            parsed = Integer.parseInt(stringToParse);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number: " + stringToParse);
            return Optional.of(1);
        }

        if (!predicate.test(parsed)) {
            System.err.println("Invalid number: " + stringToParse);
            return Optional.of(1);
        }

        return Optional.of(parsed);
    }

    public static Optional<Integer> parseInt(String stringToParse) {
        return parseInt(stringToParse, ignored -> true);
    }

}

