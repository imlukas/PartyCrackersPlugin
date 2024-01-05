package dev.imlukas.partycrackersplugin.reward.impl;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import dev.imlukas.partycrackersplugin.util.text.TextUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageReward implements PartyCrackerReward {

    private final List<String> messages = new ArrayList<>();

    public MessageReward(PartyCrackersPlugin plugin, ConfigurationSection section) {
        messages.addAll(section.getStringList("messages"));
    }

    @Override
    public void reward(Player player) {
        for (String message : messages) {
            player.sendMessage(TextUtils.color(message.replace("%player%", player.getName())));
        }
    }
}
