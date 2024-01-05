package dev.imlukas.partycrackersplugin.reward;

import org.bukkit.configuration.ConfigurationSection;

public interface PartyCrackerElementRegistry<T extends PartyCrackerElement> {

    void registerDefaults();

    T tryParse(ConfigurationSection section);
}
