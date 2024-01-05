package dev.imlukas.partycrackersplugin.commands;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.impl.PartyCracker;
import dev.imlukas.partycrackersplugin.registry.PartyCrackerRegistry;
import dev.imlukas.partycrackersplugin.util.command.command.impl.AdvancedCommand;
import dev.imlukas.partycrackersplugin.util.command.command.impl.ExecutionContext;
import dev.imlukas.partycrackersplugin.util.command.language.type.Parameter;
import dev.imlukas.partycrackersplugin.util.command.language.type.impl.FilteredParameterType;
import dev.imlukas.partycrackersplugin.util.command.language.type.impl.IntegerParameterType;
import dev.imlukas.partycrackersplugin.util.storage.Messages;
import dev.imlukas.partycrackersplugin.util.text.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GiveCrackerCommand extends AdvancedCommand {

    private final Messages messages;
    public GiveCrackerCommand(PartyCrackersPlugin plugin) {
        super("cracker give <cracker> <amount>");

        this.messages = plugin.getMessages();
        registerParameter(new Parameter<>("cracker", new CrackerParameterType(plugin), false));
        registerParameter(new Parameter<>("amount", new IntegerParameterType(), true));
    }

    @Override
    public String getPermission() {
        return "crackers.give";
    }

    @Override
    public void execute(CommandSender sender, ExecutionContext context) {
        Player player = (Player) sender;
        PartyCracker cracker = context.getParameter("cracker");

        if (cracker == null) {
            messages.sendMessage(player, "cracker.not-found");
            return;
        }

        int amount = context.getParameter("amount");

        if (amount <= 0) {
            return;
        }

        player.getInventory().addItem(cracker.getDisplayItem(amount));
        messages.sendMessage(player, "cracker.gave-item", new Placeholder<>("amount", amount), new Placeholder<>("cracker", cracker.getDisplayName()));
    }

    public static class CrackerParameterType implements FilteredParameterType<PartyCracker> {

        private final PartyCrackerRegistry registry;

        public CrackerParameterType(PartyCrackersPlugin plugin) {
            this.registry = plugin.getCrackerRegistry();
        }

        @Override
        public boolean isType(String input) {
            return registry.getCracker(input) != null;
        }

        @Override
        public PartyCracker parse(String input) {
            return registry.getCracker(input);
        }

        @Override
        public PartyCracker getDefaultValue() {
            return null;
        }

        @Override
        public List<PartyCracker> getAllValues() {
            return registry.getCrackers();
        }

        @Override
        public Set<String> getSuggestions() {
            return registry.getCrackerIdentifiers();
        }

        @Override
        public @Nullable List<Placeholder<Player>> createPlaceholders(Object value) {
            return null;
        }
    }
}
