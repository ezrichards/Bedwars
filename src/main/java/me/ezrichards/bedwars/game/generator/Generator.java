package me.ezrichards.bedwars.game.generator;

import org.bukkit.Location;

/**
 * Made by Ethan Richards
 * May 29, 2020
 */
public class Generator {

    private GeneratorType type;
    private Location location;

    public Generator(GeneratorType type, Location location) {
        this.type = type;
        this.location = location;
    }

    public GeneratorType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }
}
