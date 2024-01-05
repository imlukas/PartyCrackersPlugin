package dev.imlukas.partycrackersplugin.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.imlukas.partycrackersplugin.PartyCrackersPlugin;
import dev.imlukas.partycrackersplugin.PluginSettings;
import dev.imlukas.partycrackersplugin.reward.PartyCrackerReward;
import dev.imlukas.partycrackersplugin.reward.impl.item.ItemReward;
import dev.imlukas.partycrackersplugin.reward.impl.item.RewardItem;
import dev.imlukas.partycrackersplugin.util.command.ListUtils;
import dev.imlukas.partycrackersplugin.util.http.HTTPUtil;
import dev.imlukas.partycrackersplugin.util.schedulerutil.builders.ScheduleBuilder;
import dev.imlukas.partycrackersplugin.util.time.TimeUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class PartyCracker {

    private final LinkedList<PartyCrackerReward> rewardList = new LinkedList<>();
    private final List<Particle> explosionParticles = new ArrayList<>();
    private final List<Sound> explosionSounds = new ArrayList<>();
    private final String identifier;
    private final String displayName;
    private final ItemStack displayItem;
    private final int explosionParticleCount;
    private final int explosionTime;

    public PartyCracker(String identifier, String displayName, ItemStack displayItem, int explosionParticleCount, int explosionTime) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.displayItem = displayItem;
        this.explosionParticleCount = explosionParticleCount;
        this.explosionTime = explosionTime;
    }

    public ItemStack getDisplayItem() {
        return getDisplayItem(1);
    }

    public ItemStack getDisplayItem(int amount) {
        ItemStack displayItem = this.displayItem.clone();
        displayItem.setAmount(amount);
        return displayItem;
    }

    public void addReward(PartyCrackerReward reward) {
        rewardList.add(reward);
    }

    public void addExplosionParticle(String particle) {
        explosionParticles.add(Particle.valueOf(particle));
    }

    public void addExplosionSound(String sound) {
        explosionSounds.add(Sound.valueOf(sound));
    }

    public void crack(PartyCrackersPlugin plugin, Player player) {
        World world = player.getWorld();
        Item item = world.spawn(player.getLocation(), Item.class);
        item.setVelocity(player.getEyeLocation().getDirection().multiply(0.7));
        item.setCanPlayerPickup(false);

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (itemInMainHand.getAmount() == 1) {
            player.getInventory().setItemInMainHand(null);
        } else {
            itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
        }

        item.setItemStack(getDisplayItem());

        new ScheduleBuilder(plugin).in(getExplosionTime()).ticks().run(() -> {
            Particle particle = ListUtils.getRandom(explosionParticles);
            Sound sound = ListUtils.getRandom(explosionSounds);
            simulateExplosion(item.getLocation(), particle, sound);

            item.remove();

            rewardList.forEach(reward -> {
                if (reward instanceof ItemReward itemReward) { // I do not like this, but it's the best I could have done this.
                    itemReward.reward(player, item.getLocation());
                    return;
                }

                reward.reward(player);
            });

            CompletableFuture.runAsync(() -> {
                PluginSettings settings = plugin.getSettings();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("playerId", player.getUniqueId().toString());
                jsonObject.addProperty("crackerId", identifier);
                jsonObject.addProperty("openedAt", TimeUtil.getTimeFormatted(true));

                HttpResponse<String> response;
                try {
                    response = HTTPUtil.makePostRequest(settings.getRequestUrl(), settings.getRequestBearer(), jsonObject);
                } catch (IOException | InterruptedException e) {
                    System.err.println("Error while making HTTP request: " + e.getMessage());
                    return;
                }

                System.out.println("HTTP Successful: " + (response.statusCode() == 200 ? "Yes" : "No"));
                System.out.println("HTTP Request body: " + response.body());
            });


        }).sync().start();
    }

    private void simulateExplosion(Location location, Particle particle, Sound sound) {
        Random random = ThreadLocalRandom.current();
        World world = location.getWorld();

        for(int index = 0; index < explosionParticleCount; index++) {
            Vector randomVel = new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.3, random.nextDouble() - 0.5);
            Vector relative = randomVel.clone().normalize().multiply(1.1);

            location.getNearbyPlayers(10).forEach(player -> player.playSound(player.getLocation(), sound, 1, 1));
            world.spawnParticle(
                    particle,
                    location.clone().add(randomVel),
                    15,
                    relative.getX(),
                    relative.getY(),
                    relative.getZ());

        }
    }
}
