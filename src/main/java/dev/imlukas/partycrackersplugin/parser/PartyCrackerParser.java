package dev.imlukas.partycrackersplugin.parser;

import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.impl.PartyCracker;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerElement;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerElementRegistry;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import dev.imlukas.partycrackersplugin.reward.registry.PartyCrackerRewardRegistry;
import dev.imlukas.partycrackersplugin.util.item.parser.ItemParser;
import dev.imlukas.partycrackersplugin.util.pdc.PDCWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PartyCrackerParser {

    private final PartyCrackersPlugin plugin;
    private final PartyCrackerRewardRegistry rewardRegistry;

    public PartyCrackerParser(PartyCrackersPlugin plugin) {
        this.plugin = plugin;
        rewardRegistry = plugin.getRewardRegistry();
    }

    public PartyCracker parse(FileConfiguration section) {
        if (!section.contains("identifier") || !section.contains("display-name") || !section.contains("display-item")) {
            System.err.println("Cracker is missing fields.");
            return null;
        }

        String id = section.getString("identifier");
        String displayName = section.getString("display-name");
        ItemStack displayItem = ItemParser.fromOrDefault(section.getConfigurationSection("display-item"), Material.PAPER);

        PDCWrapper.modifyItem(plugin, displayItem, pdcWrapper -> pdcWrapper.setString("cracker-id", id));

        int explosionTime = section.getInt("explosion-time", 5);
        int explosionParticleCount = section.getInt("explosion-particle-count", 25);
        List<String> explosionParticles = section.getStringList("explosion-particles");
        List<String> explosionSounds = section.getStringList("explosion-sounds");

        ConfigurationSection rewardsSection = section.getConfigurationSection("rewards");

        if (rewardsSection == null) {
            System.err.println("Cracker is missing rewards section.");
            return null;
        }

        List<PartyCrackerReward> rewards = parseObjectsFor(rewardsSection, rewardRegistry);

        PartyCracker cracker = new PartyCracker(id, displayName, displayItem, explosionParticleCount, explosionTime);

        explosionParticles.forEach(cracker::addExplosionParticle);
        explosionSounds.forEach(cracker::addExplosionSound);
        rewards.forEach(cracker::addReward);

        return cracker;
    }

    public <T extends PartyCrackerElement> List<T> parseObjectsFor(ConfigurationSection mainSection, PartyCrackerElementRegistry<T> registry) {
        List<T> objects = new ArrayList<>();

        for (String object : mainSection.getKeys(false)) {
            ConfigurationSection subSection = mainSection.getConfigurationSection(object);

            if (subSection == null) {
                continue;
            }

            T crackerObject = registry.tryParse(subSection);

            if (crackerObject == null) {
                continue;
            }

            objects.add(crackerObject);
        }

        System.out.println("Parsed " + objects.size() + " objects for the cracker.");
        return objects;
    }
}
