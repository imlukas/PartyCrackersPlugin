package dev.imlukas.partycrackersplugin;

import dev.imlukas.partycrackersplugin.commands.GiveCrackerCommand;
import dev.imlukas.partycrackersplugin.impl.PartyCracker;
import dev.imlukas.partycrackersplugin.listener.PlayerInteractListener;
import dev.imlukas.partycrackersplugin.parser.PartyCrackerParser;
import dev.imlukas.partycrackersplugin.registry.PartyCrackerRegistry;
import dev.imlukas.partycrackersplugin.reward.registry.PartyCrackerRewardRegistry;
import dev.imlukas.partycrackersplugin.util.BetterJavaPlugin;
import dev.imlukas.partycrackersplugin.util.command.command.CommandManager;
import dev.imlukas.partycrackersplugin.util.storage.Messages;
import dev.imlukas.partycrackersplugin.util.storage.YMLBase;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Getter
public final class PartyCrackersPlugin extends BetterJavaPlugin {

    private Economy economy;
    private PluginSettings settings;
    private Messages messages;
    private CommandManager commandManager;
    private PartyCrackerRewardRegistry rewardRegistry;
    private PartyCrackerRegistry crackerRegistry;
    private PartyCrackerParser crackerParser;

    @Override
    public void onEnable() {
        super.onEnable();
        setupEconomy();
        messages = new Messages(this);
        settings = new PluginSettings(this);
        rewardRegistry = new PartyCrackerRewardRegistry(this);
        crackerRegistry = new PartyCrackerRegistry();

        crackerParser = new PartyCrackerParser(this);
        commandManager = new CommandManager(this, messages);

        commandManager.registerCommand(new GiveCrackerCommand(this));

        CompletableFuture.runAsync(() -> {
            File crackersFolder = new File(getDataFolder(), "crackers");

            for (File file : crackersFolder.listFiles()) {
                YMLBase crackerBase = new YMLBase(this, file, true);
                PartyCracker cracker = crackerParser.parse(crackerBase.getConfiguration());
                crackerRegistry.register(cracker);
            }
        }).thenRun(() -> {
            System.out.println("[PartyCrackers] Loaded " + crackerRegistry.getCrackers().size() + " crackers.");
        });
        registerListener(new PlayerInteractListener(this));

        System.out.println("[PartyCrackers] Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }
}
