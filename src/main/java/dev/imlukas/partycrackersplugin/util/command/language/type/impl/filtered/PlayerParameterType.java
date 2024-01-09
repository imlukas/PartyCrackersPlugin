package dev.imlukas.partycrackersplugin.util.command.language.type.impl.filtered;

import dev.imlukas.partycrackersplugin.util.command.language.type.impl.FilteredParameterType;
import dev.imlukas.partycrackersplugin.util.text.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerParameterType implements FilteredParameterType<Player> {
    @Override
    public boolean isType(String input) {
        return Bukkit.getPlayer(input) != null;
    }

    @Override
    public Player parse(String input) {
        return Bukkit.getPlayer(input);
    }

    @Override
    public Player getDefaultValue() {
        return null;
    }

    @Override
    public Collection<String> getSuggestions() {
        Set<String> suggestions = new HashSet<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(player.getName());
        }

        return suggestions;
    }

    @Override
    public @Nullable List<Placeholder<Player>> createPlaceholders(Object value) {
        return null;
    }
}
