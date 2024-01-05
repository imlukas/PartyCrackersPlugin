package dev.imlukas.partycrackersplugin.util.item.parser;

import dev.imlukas.partycrackersplugin.util.item.trim.TrimMaterialType;
import dev.imlukas.partycrackersplugin.util.item.trim.TrimPatternType;
import dev.imlukas.partycrackersplugin.util.text.TextUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemParser {

    private SkullParser skullParser;

    private ItemStack item;
    private ItemMeta meta;

    public static ItemStack fromOrDefault(ConfigurationSection section, Material defaultItem) {
        ItemStack item = from(section);

        if (item == null) {
            return new ItemStack(defaultItem);
        }

        return item;
    }

    public static ItemStack from(ConfigurationSection section) {
        return new ItemParser().parse(section);
    }

    public ItemStack parse(ConfigurationSection section) {
        if (section == null) {
            return null;
        }

        String itemType = section.getString("item-type");

        if (itemType == null || itemType.equalsIgnoreCase("vanilla")) {
            parseVanillaItem(section);
        }

        if (item == null || meta == null) {
            return null;
        }

        this.skullParser = new SkullParser(meta);
        ItemParserSerializer.applySection(this, section);

        item.setItemMeta(meta);
        return item;
    }


    private void parseVanillaItem(ConfigurationSection itemSection) {
        Material material = Material.getMaterial(itemSection.getString(itemSection.contains("material") ? "material" : "type").toUpperCase());

        if (material == null) {
            throw new IllegalArgumentException("[ItemParser] Material/Type is null");
        }

        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }


    public void name(String name) {
        this.meta.setDisplayName(TextUtils.color(name));
    }

    public void lore(List<String> lore) {
        this.meta.setLore(TextUtils.color(lore));
    }

    public void amount(int amount) {
        this.item.setAmount(amount);
    }

    public void modelData(int modelData) {
        this.meta.setCustomModelData(modelData);
    }

    public void unbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
    }

    public void skullHash(String skullHash) {
        this.skullParser.setSkullHash(skullHash);
        this.skullParser.parse();
    }

    public void skull(String skull) {
        this.skullParser.setSkullName(skull);
        this.skullParser.parse();
    }

    public void glow(boolean glow) {
        if (glow) {
            this.meta.addEnchant(Enchantment.PIERCING, 1, true);
            return;
        }

        this.meta.removeEnchant(Enchantment.PIERCING);
    }

    public void trim(ConfigurationSection section) {
        if (!(meta instanceof ArmorMeta armorMeta)) {
            return;
        }

        TrimMaterial trimMaterial = TrimMaterialType.valueOf(section.getString("material").toUpperCase()).getTrimMaterial();
        TrimPattern trimPattern = TrimPatternType.valueOf(section.getString("pattern").toUpperCase()).getPattern();
        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);

        armorMeta.setTrim(armorTrim);
    }

    public void vanillaEnchantments(ConfigurationSection section) {
        for (String enchantmentName : section.getKeys(false)) {
            int level = section.getInt(enchantmentName);
            Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));

            if (enchant == null) {
                throw new IllegalArgumentException("[ItemParser] Enchantment " + enchantmentName + " is null");
            }

            meta.addEnchant(enchant, level, true);
        }
    }

    public void attributes(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection attributeSection = section.getConfigurationSection(key);

            if (attributeSection == null) {
                continue;
            }

            Attribute attribute = Attribute.valueOf(key.toUpperCase().replace("-", "_"));
            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(attributeSection.getString("operation").toUpperCase());
            double amount = attributeSection.getDouble("value");


            EquipmentSlot slot = null;
            if (attributeSection.contains("slot")) {
                slot = EquipmentSlot.valueOf(attributeSection.getString("slot").toUpperCase());
            }

            AttributeModifier modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "item-modifier-" + attribute.name().toLowerCase(),
                    amount,
                    operation,
                    slot
            );

            meta.addAttributeModifier(attribute, modifier);
        }
    }

    public void flags(List<String> flags) {
        List<ItemFlag> itemFlags = new ArrayList<>();

        for (String flag : flags) {
            itemFlags.add(ItemFlag.valueOf(flag.toUpperCase()));
        }

        meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
    }


}
