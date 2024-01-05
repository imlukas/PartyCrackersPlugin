package dev.imlukas.partycrackersplugin.reward.impl.item;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import dev.imlukas.partycrackersplugin.util.item.parser.ItemParser;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemReward implements PartyCrackerReward {

    private final List<RewardItem> items = new ArrayList<>();

    public ItemReward(PartyCrackersPlugin plugin, ConfigurationSection rewardSection) {
        ItemParser itemParser = new ItemParser();

        for (String key : rewardSection.getKeys(false)) {
            if (key.equals("id")) {
                continue;
            }

            ConfigurationSection itemSection = rewardSection.getConfigurationSection(key);

            if (itemSection == null) {
                System.err.println("Invalid item " + key + " in reward");
                continue;
            }

            ItemStack item = itemParser.parse(itemSection);

            if (item == null) {
                System.err.println("Invalid item " + key + " in reward");
                continue;
            }

            int chance = itemSection.getInt("chance", 100);
            boolean drop = itemSection.getBoolean("drop", false);
            items.add(new RewardItem(item, (double) chance / 100, drop));
        }
    }

    public void reward(Player player, Location itemDropLocation) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (RewardItem item : items) {
            if (random.nextDouble() > item.getChance()) {
                continue;
            }

            if (item.shouldDrop()) {
                itemDropLocation.getWorld().dropItem(itemDropLocation, item.getItemStack());
                continue;
            }

            Collection<ItemStack> items = player.getInventory().addItem(item.getItemStack()).values();

            for (ItemStack overflow : items) {
                itemDropLocation.getWorld().dropItem(itemDropLocation, overflow);
            }
        }
    }

    @Override
    public void reward(Player player) {
        reward(player, player.getLocation());
    }
}
