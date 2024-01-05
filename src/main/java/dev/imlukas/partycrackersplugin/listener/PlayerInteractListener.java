package dev.imlukas.partycrackersplugin.listener;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.impl.PartyCracker;
import dev.imlukas.partycrackersplugin.registry.PartyCrackerRegistry;
import dev.imlukas.partycrackersplugin.util.pdc.PDCWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private final PartyCrackersPlugin plugin;
    private final PartyCrackerRegistry registry;

    public PlayerInteractListener(PartyCrackersPlugin plugin) {
        this.plugin = plugin;
        this.registry = plugin.getCrackerRegistry();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        PDCWrapper wrapper = new PDCWrapper(plugin, item);

        String crackerId = wrapper.getString("cracker-id");

        if (crackerId == null) {
            return;
        }

        PartyCracker cracker = registry.getCracker(crackerId);
        cracker.crack(plugin, player);
    }
}
