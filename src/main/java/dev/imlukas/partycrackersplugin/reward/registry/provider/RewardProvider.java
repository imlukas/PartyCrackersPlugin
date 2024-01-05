package dev.imlukas.partycrackersplugin.reward.registry.provider;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import org.bukkit.configuration.ConfigurationSection;

public interface RewardProvider {

    PartyCrackerReward provide(PartyCrackersPlugin plugin, ConfigurationSection section);

}
