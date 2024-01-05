package dev.imlukas.partycrackersplugin.util.item.trim;

import org.bukkit.inventory.meta.trim.TrimMaterial;

public enum TrimMaterialType {

    QUARTZ(TrimMaterial.QUARTZ),
    IRON(TrimMaterial.IRON),
    GOLD(TrimMaterial.GOLD),
    DIAMOND(TrimMaterial.DIAMOND),
    NETHERITE(TrimMaterial.NETHERITE),
    REDSTONE(TrimMaterial.REDSTONE),
    COPPER(TrimMaterial.COPPER),
    EMERALD(TrimMaterial.EMERALD),
    LAPIS(TrimMaterial.LAPIS),
    AMETHYST(TrimMaterial.AMETHYST);

    private final TrimMaterial trimMaterial;

    TrimMaterialType(TrimMaterial trimMaterial) {
        this.trimMaterial = trimMaterial;
    }

    public TrimMaterial getTrimMaterial() {
        return trimMaterial;
    }
}
