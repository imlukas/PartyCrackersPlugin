package dev.imlukas.partycrackersplugin.reward.impl;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandReward implements PartyCrackerReward {

    private final Pattern pattern = Pattern.compile("\\[(.*)]");
    private final List<String> commands = new ArrayList<>();

    public CommandReward(PartyCrackersPlugin plugin, ConfigurationSection section) {
        commands.addAll(section.getStringList("commands"));
    }

    @Override
    public void reward(Player player) {
        for (String command : commands) {
            System.out.println(command);
            Matcher matcher = pattern.matcher(command);

            if (!matcher.find()) {
                player.performCommand(command);
                return;
            }

            String commandSender = matcher.group(1);
            System.out.println(commandSender);
            String toExecute = command.replace("[" + commandSender + "] ", "").replace("%player%", player.getName());

            if (commandSender.equalsIgnoreCase("console")) {
                player.getServer().dispatchCommand(Bukkit.getConsoleSender(), toExecute);
            } else {
                player.performCommand(toExecute);
            }

        }
    }
}
