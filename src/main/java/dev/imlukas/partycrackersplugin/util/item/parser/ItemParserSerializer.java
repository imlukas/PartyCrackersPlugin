package dev.imlukas.partycrackersplugin.util.item.parser;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.function.Consumer;

public class ItemParserSerializer {

    private ItemParserSerializer() {
    }

    public static void applySection(ItemParser parser, ConfigurationSection section) {
        applyIfPresent(section, "skull-hash", String.class, parser::skullHash);
        applyIfPresent(section, "skull", String.class, parser::skull);
        applyIfPresent(section, "name", String.class, parser::name);

        applyIfPresent(section, "lore", List.class, parser::lore);
        applyIfPresent(section, "flags", List.class, parser::flags);

        applyIfPresent(section, "unbreakable", Boolean.class, parser::unbreakable);
        applyIfPresent(section, "glow", Boolean.class, parser::glow);

        applyIfPresent(section, "amount", Integer.class, parser::amount);
        applyIfPresent(section, "model-data", Integer.class, parser::modelData);

        applyIfPresent(section, "trim", ConfigurationSection.class, parser::trim);
        applyIfPresent(section, "enchants", ConfigurationSection.class, parser::vanillaEnchantments);
        applyIfPresent(section, "attributes", ConfigurationSection.class, parser::attributes);
    }

    private static <T> void applyIfPresent(ConfigurationSection section, String key, Class<T> clazz, Consumer<T> consumer) {
        if (section.contains(key)) {
            T value = section.getObject(key, clazz);

            if (value == null) {
                return;
            }

            consumer.accept(value);
        }
    }

}
