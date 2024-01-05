package dev.imlukas.partycrackersplugin.util.item.parser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullParser {

    private String skullName = null;
    private String skullHash = null;

    private final ItemMeta meta;

    public SkullParser(ItemMeta meta) {
        this.meta = meta;
    }

    public void setSkullHash(String skullHash) {
        this.skullHash = skullHash;
    }

    public void setSkullName(String skullName) {
        this.skullName = skullName;
    }

    public void parse() {
        if (skullName != null) {
            ((SkullMeta) meta).setOwner(skullName);
            return;
        }

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.put("textures", new Property("textures", skullHash));

        SkullMeta skullMeta = (SkullMeta) meta;

        if (skullMeta == null) {
            return;
        }

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
            profileField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
