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
    public NPC npc;

    public Shop(ShopType type, Location location, NPC npc) {
        this.type = type;
        this.location = location;
        this.npc = npc;
    }

    public ShopType getType() {
        return type;
    }

    public void setType(ShopType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NPC getNpc() {
        return npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }
}
