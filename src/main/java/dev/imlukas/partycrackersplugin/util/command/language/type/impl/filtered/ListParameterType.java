package dev.imlukas.partycrackersplugin.util.command.language.type.impl.filtered;

import dev.imlukas.partycrackersplugin.util.command.language.type.impl.FilteredParameterType;
import dev.imlukas.partycrackersplugin.util.text.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ListParameterType<Type> implements FilteredParameterType<Type> {

    private final List<Type> values;
    private final Function<Type, String> identifierMapper;

    public ListParameterType(List<Type> values, Function<Type, String> identifierMapper) {
        this.values = values;
        this.identifierMapper = identifierMapper;
    }

    @Override
    public boolean isType(String input) {
        for (Type value : values) {
            if (identifierMapper.apply(value).equalsIgnoreCase(input)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Type parse(String input) {
        for (Type value : values) {
            if (identifierMapper.apply(value).equalsIgnoreCase(input)) {
                return value;
            }
        }

        return null;
    }

    @Override
    public Type getDefaultValue() {
        return null;
    }

    @Override
    public List<Type> getAllValues() {
        return values;
    }

    @Override
    public Collection<String> getSuggestions() {
        Set<String> suggestions = new HashSet<>();

        for (Type value : values) {
            suggestions.add(value.toString());
        }

        return suggestions;
    }

    @Override
    public @Nullable List<Placeholder<Player>> createPlaceholders(Object value) {
        return null;
    }
}
