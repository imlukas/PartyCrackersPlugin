package dev.imlukas.partycrackersplugin.util.item.legacy;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.imlukas.partycrackersplugin.util.item.trim.TrimMaterialType;
import dev.imlukas.partycrackersplugin.util.item.trim.TrimPatternType;
import dev.imlukas.partycrackersplugin.util.text.TextUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.lang.reflect.Field;
import java.util.*;

/**
 * The ItemBuilder class is a utility class for creating {@link ItemStack}s.
 *
 * @author Illusion
 */
public class ItemBuilder {

    private final Material material;
    private final List<String> lore = new ArrayList<>();
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount = 1;
    private String name = "";
    private List<ItemFlag> itemFlags = new ArrayList<>();
    private short data = -1;
    private boolean glowing;
    private boolean unbreakable;
    private ArmorTrim trim;

    private int modelData = -1;

    private String skullName = null;
    private String skullHash = null;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public static ItemBuilder fromItem(ItemStack original) {
        Material material = original.getType();
        ItemBuilder builder = new ItemBuilder(material);

        ItemMeta meta = original.getItemMeta();

        builder.amount(original.getAmount());

        if (meta instanceof Damageable damageable) {
            builder.data(damageable.getDamage());
        }

        if (meta.hasDisplayName()) {
            builder.name(meta.getDisplayName());
        }

        if (meta.hasLore()) {
            builder.lore(meta.getLore());
        }

        if (meta.hasEnchants()) {
            builder.enchantments.putAll(meta.getEnchants());
        }

        builder.itemFlags.addAll(meta.getItemFlags());

        if (meta.isUnbreakable()) {
            builder.unbreakable(true);
        }

        if (meta instanceof SkullMeta skullMeta) {
            if (skullMeta.hasOwner()) {
                builder.skull(skullMeta.getOwner());
            } else {
                builder.skullHash(getSkullHash(original));
            }
        }

        if (meta.hasCustomModelData()) {
            builder.modelData(meta.getCustomModelData());
        }

        return builder;
    }

    private static String getSkullHash(ItemStack item) {
        try {
            Field profileField = item.getItemMeta().getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            GameProfile profile = (GameProfile) profileField.get(item.getItemMeta());
            PropertyMap propertyMap = profile.getProperties();
            if (propertyMap.containsKey("textures")) {
                for (Property property : propertyMap.get("textures")) {
                    return property.getValue();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemBuilder modelData(int modelData) {
        this.modelData = modelData;
        return this;
    }

    public ItemBuilder glowing(boolean glowing) {
        this.glowing = glowing;
        itemFlags.add(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(int num) {
        this.data = (short) num;
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        for (String s : lore) {
            this.lore.add(TextUtils.color(s));
        }
        return this;
    }

    public ItemBuilder lore(String... lore) {
        for (String s : lore) {
            this.lore.add(TextUtils.color(s));
        }
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = TextUtils.color(name);
        return this;
    }


    public ItemBuilder enchants(String enchantKey, int level) {
        Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantKey));
        enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchants(ConfigurationSection section) {
        for (String enchantmentName : section.getKeys(false)) {
            int level = section.getInt(enchantmentName);
            Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
            enchantments.put(enchant, level);
        }
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.itemFlags.addAll(Arrays.asList(flags));
        return this;
    }

    public ItemBuilder flags(List<String> flags) {
        ItemFlag[] array = new ItemFlag[flags.size()];
        for (int index = 0; index < array.length; index++) {
            array[index] = ItemFlag.valueOf(flags.get(index));
        }

        return flags(array);
    }

    public ItemBuilder skull(String name) {
        Validate.isTrue(material.name().contains("SKULL") || material.name().contains("HEAD"),
                "Attempt to set skull data on non skull item");
        this.skullName = name;
        data(3);
        return this;
    }

    public ItemBuilder skullHash(String hash) {
        Validate.isTrue(material.name().contains("SKULL") || material.name().contains("HEAD"),
                "Attempt to set skull data on non skull item");
        this.skullHash = hash;
        data(3);
        return this;
    }

    public ItemBuilder trim(ConfigurationSection section) {
        TrimMaterial trimMaterial = TrimMaterialType.valueOf(section.getString("material").toUpperCase()).getTrimMaterial();
        TrimPattern trimPattern = TrimPatternType.valueOf(section.getString("pattern").toUpperCase()).getPattern();

        this.trim = new ArmorTrim(trimMaterial, trimPattern);
        return this;
    }


    public ItemBuilder clone() {
        ItemBuilder newBuilder = new ItemBuilder(material);
        newBuilder.data = data;
        newBuilder.lore.clear();
        newBuilder.lore.addAll(lore);
        newBuilder.name = name;
        newBuilder.skullName = skullName;
        newBuilder.skullHash = skullHash;
        newBuilder.itemFlags = itemFlags;
        return newBuilder;

    }

    public ItemStack build() {
        if (amount > 64) {
            amount = 64;
        }

        ItemStack item = new ItemStack(material, amount);

        if (data != -1) {
            item.setDurability(data);
        }

        ItemMeta meta = item.getItemMeta();

        if (name != null && !name.isEmpty()) {
            meta.setDisplayName(name);
        }

        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }

        if (itemFlags != null) {
            meta.addItemFlags(itemFlags.toArray(new ItemFlag[]{}));
        }

        if (skullName != null) {
            ((SkullMeta) meta).setOwner(skullName);
        }

        if (glowing && enchantments.isEmpty()) {
            meta.addEnchant(Enchantment.LUCK, 123, true);
        }

        if (modelData != -1) {
            meta.setCustomModelData(modelData);
        }

        if (trim != null) {
            if (meta instanceof ArmorMeta armorMeta) {
                armorMeta.setTrim(trim);
            }
        }

        meta.setUnbreakable(unbreakable);

        if (!enchantments.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }

        if (skullHash != null) {
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            PropertyMap propertyMap = gameProfile.getProperties();
            propertyMap.put("textures", new Property("textures", skullHash));

            SkullMeta skullMeta = (SkullMeta) meta;

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
                profileField.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        item.setItemMeta(meta);
        return item;
    }

}
