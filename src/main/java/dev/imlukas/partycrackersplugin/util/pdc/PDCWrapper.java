package dev.imlukas.partycrackersplugin.util.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.function.Consumer;

public class PDCWrapper {

    private final JavaPlugin plugin;
    private final ItemStack itemStack;

    public PDCWrapper(JavaPlugin plugin, ItemStack itemStack) {
        this(plugin, itemStack, true);
    }

    public PDCWrapper(JavaPlugin plugin, ItemStack itemStack, boolean clone) {
        this.plugin = plugin;
        this.itemStack = itemStack;
    }

    public static void modifyItem(JavaPlugin plugin, ItemStack item, Consumer<PDCWrapper> modifier) {
        PDCWrapper wrapper = new PDCWrapper(plugin, item, false);
        modifier.accept(wrapper);
    }

    public void setString(String key, String value) {
        set(key, PersistentDataType.STRING, value);
    }

    public void setInteger(String key, int value) {
        set(key, PersistentDataType.INTEGER, value);
    }

    public void setDouble(String key, double value) {
        set(key, PersistentDataType.DOUBLE, value);
    }

    public void setBoolean(String key, boolean value) {
        set(key, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
    }

    public void setUUID(String key, UUID value) {
        set(key, PersistentDataType.STRING, value.toString());
    }

    public String getString(String key) {
        return get(key, PersistentDataType.STRING);
    }

    public int getInteger(String key) {
        return get(key, PersistentDataType.INTEGER);
    }

    public double getDouble(String key) {
        return get(key, PersistentDataType.DOUBLE);
    }

    public boolean getBoolean(String key) {
        return get(key, PersistentDataType.BYTE) == 1;
    }

    public UUID getUUID(String key) {
        return UUID.fromString(get(key, PersistentDataType.STRING));
    }

    public ItemStack getModifiedItem() {
        return itemStack;
    }

    private NamespacedKey createKey(String name) {
        return new NamespacedKey(plugin, name);
    }

    private void modifyMeta(Consumer<ItemMeta> consumer) {
        ItemMeta meta = itemStack.getItemMeta();
        consumer.accept(meta);
        itemStack.setItemMeta(meta);
    }

    private <T> void set(String key, PersistentDataType<T, T> type, T value) {
        modifyMeta(meta -> {
            meta.getPersistentDataContainer().set(createKey(key), type, value);
        });
    }

    private <T> T get(String key, PersistentDataType<T, T> type) {
        return itemStack.getItemMeta().getPersistentDataContainer().get(createKey(key), type);
    }

    public boolean contains(String key) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(createKey(key));
    }
}
