package dev.imlukas.partycrackersplugin.util.command.language.type.impl;

import dev.imlukas.partycrackersplugin.util.command.language.type.ParameterType;
import dev.imlukas.partycrackersplugin.util.text.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface FilteredParameterType<Type> extends ParameterType<Type> {

    Collection<String> getSuggestions();

    @Nullable
    List<Placeholder<Player>> createPlaceholders(Object value);

}
