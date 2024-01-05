package dev.imlukas.partycrackersplugin.reward.registry;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerElementRegistry;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import dev.imlukas.partycrackersplugin.reward.impl.CommandReward;
import dev.imlukas.partycrackersplugin.reward.impl.item.ItemReward;
import dev.imlukas.partycrackersplugin.reward.impl.MessageReward;
import dev.imlukas.partycrackersplugin.reward.impl.MoneyReward;
import dev.imlukas.partycrackersplugin.reward.registry.provider.RewardProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PartyCrackerRewardRegistry implements PartyCrackerElementRegistry<PartyCrackerReward> {

    private final PartyCrackersPlugin plugin;

    private final Map<String, RewardProvider> registeredProviders = new ConcurrentHashMap<>();

    public PartyCrackerRewardRegistry(PartyCrackersPlugin plugin) {
        this.plugin = plugin;
        registerDefaults();
    }

    public void register(String id, RewardProvider provider) {
        registeredProviders.put(id, provider);
    }

    @Override
    public void registerDefaults() {
        register("money", MoneyReward::new);
        register("items", ItemReward::new);
        register("message", MessageReward::new);
        register("command", CommandReward::new);
    }

    @Override
    public PartyCrackerReward tryParse(ConfigurationSection rewardsSection) {
        String id = rewardsSection.contains("id") ? rewardsSection.getString("id") : rewardsSection.getName();
        RewardProvider provider = registeredProviders.get(id);

        if (provider == null) {
            return null;
        }

        return provider.provide(plugin, rewardsSection);
    }

}