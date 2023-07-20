package me.ezrichards.bedwars.game.map;

import me.ezrichards.bedwars.game.generator.Generator;
import org.bukkit.Location;

import java.util.List;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class GameMap {

    private String name;
    private int maxPlayers;
    private List<Location> itemShops;
    private List<Location> teamUpgrades;
    private List<Generator> generators;

    public GameMap(String name, int maxPlayers, List<Location> itemShops, List<Location> teamUpgrades, List<Generator> generators) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.itemShops = itemShops;
        this.teamUpgrades = teamUpgrades;
        this.generators = generators;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<Location> getItemShops() {
        return itemShops;
    }

    public List<Location> getTeamUpgrades() {
        return teamUpgrades;
    }

    public List<Generator> getGenerators() {
        return generators;
    }
}
