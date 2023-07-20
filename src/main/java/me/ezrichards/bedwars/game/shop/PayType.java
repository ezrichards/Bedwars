package me.ezrichards.bedwars.game.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * Made by Ethan Richards
 * September 07, 2020
 */
public enum PayType {

    IRON(ChatColor.WHITE + "Iron", Material.IRON_INGOT),
    GOLD(ChatColor.GOLD + "Gold", Material.GOLD_INGOT),
    DIAMOND(ChatColor.AQUA + "Diamond", Material.DIAMOND),
    EMERALD(ChatColor.GREEN + "Emerald", Material.EMERALD);

    private String name;
    private Material material;

    PayType(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
