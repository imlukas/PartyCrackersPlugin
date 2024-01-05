package dev.imlukas.partycrackersplugin.util.command.command;

import dev.imlukas.partycrackersplugin.util.collection.Pair;
import dev.imlukas.partycrackersplugin.util.command.language.AbstractObjectiveModel;
import dev.imlukas.partycrackersplugin.util.command.language.CompiledObjective;
import dev.imlukas.partycrackersplugin.util.storage.Messages;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private final List<AbstractObjectiveModel<?>> commands = new ArrayList<>();
    private final Map<String, BukkitBaseCommand> bukkitCommands = new HashMap<>();

    private final Messages messages;
    private final JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin, Messages messages) {
        this.messages = messages;
        this.plugin = plugin;
    }

    public void registerCommand(AbstractObjectiveModel<?> model) {
        commands.add(model);
        attemptCreateCommand(model);
    }

    private void attemptCreateCommand(AbstractObjectiveModel<?> model) {
        String firstWord = model.getSyntax().split(" ")[0];

        if (bukkitCommands.containsKey(firstWord)) {
            return;
        }

        BukkitBaseCommand command = new BukkitBaseCommand(this);
        bukkitCommands.put(firstWord, command);

        CommandUtilities.registerCommand(firstWord, plugin, command);
    }

    public List<String> tabComplete(String line) {
        // remove double spaces and all
        line = line.replaceAll(" +", " ");

        List<String> completions = new ArrayList<>();

        for (AbstractObjectiveModel<?> command : commands) {
            completions.addAll(command.getSuggestions(line));
        }

        return completions;
    }

    public Pair<AbstractObjectiveModel<?>, CompiledObjective> parse(String line) {
        for (AbstractObjectiveModel<?> command : commands) {
            CompiledObjective compiled = command.parse(line);

            if (compiled == null) {
                continue;
            }

            return new Pair<>(command, compiled);
        }

        return null;
    }

    public Messages getMessages() {
        return messages;
    }
}
