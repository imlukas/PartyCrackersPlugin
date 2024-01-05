package dev.imlukas.partycrackersplugin.reward.impl;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MoneyReward implements PartyCrackerReward {

    private final Economy economy;
    private final int amount;

    public MoneyReward(PartyCrackersPlugin plugin, ConfigurationSection rewardSection) {
        this.economy = plugin.getEconomy();
        this.amount = rewardSection.getInt("value");
    }

    @Override
    public void reward(Player player) {
        economy.depositPlayer(player, amount);
    }
}
