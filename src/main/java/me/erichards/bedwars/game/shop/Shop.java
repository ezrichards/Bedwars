package me.erichards.bedwars.game.shop;

import org.bukkit.Location;
import org.bukkit.entity.NPC;

/**
 * Made by Ethan Richards
 * May 29, 2020
 */
public class Shop {

    public ShopType type;
    public Location location;

    public Shop(ShopType type, Location location) {
        this.type = type;
        this.location = location;
    }

    public ShopType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }
}
