package dev.imlukas.partycrackersplugin.util.command.language.type.impl.filtered;


import dev.imlukas.partycrackersplugin.util.command.language.type.impl.FilteredParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.unit.MinecraftTime;
import dev.imlukas.partycrackersplugin.util.text.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TimeParameterType implements FilteredParameterType<MinecraftTime> {

    @Override
    public boolean isType(String input) {
        try {
            MinecraftTime.valueOf(input.toUpperCase(Locale.ROOT));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public MinecraftTime parse(String input) {
        return MinecraftTime.valueOf(input.toUpperCase(Locale.ROOT));
    }

    @Override
    public MinecraftTime getDefaultValue() {
        return MinecraftTime.TICKS;
    }

    @Override
    public List<MinecraftTime> getAllValues() {
        return List.of(MinecraftTime.values());
    }

    @Override
    public Collection<String> getSuggestions() {
        Set<String> suggestions = new HashSet<>();

        for (MinecraftTime value : MinecraftTime.values()) {
            suggestions.add(value.name());
        }

        return suggestions;
    }

    @Override
    public @Nullable List<Placeholder<Player>> createPlaceholders(Object input) {
        if (!(isType(input.toString()))) {
            return null;
        }

        MinecraftTime value = parse(input.toString());

        return List.of(
                new Placeholder<Player>("name", beautify(value.name()))
        );
    }

    private String beautify(String input) {
        return input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1).toLowerCase(Locale.ROOT);
    }
}
