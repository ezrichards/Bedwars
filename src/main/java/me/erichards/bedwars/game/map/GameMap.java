package me.erichards.bedwars.game.map;

import me.erichards.bedwars.game.generator.Generator;
import me.erichards.bedwars.game.shop.Shop;
import me.erichards.bedwars.game.team.Team;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class GameMap {

    private String name;
    private int maxPlayers;
    private Map<Team, Location> spawnLocations; // move to Team as singular location?
    private List<Shop> itemShops;
    private List<Shop> teamUpgrades;
    private List<Generator> generators;

    public GameMap(String name, int maxPlayers, Map<Team, Location> spawnLocations, List<Shop> itemShops, List<Shop> teamUpgrades, List<Generator> generators) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.spawnLocations = spawnLocations;
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

    public Map<Team, Location> getSpawnLocations() {
        return spawnLocations;
    }

    public List<Shop> getItemShops() {
        return itemShops;
    }

    public List<Shop> getTeamUpgrades() {
        return teamUpgrades;
    }

    public List<Generator> getGenerators() {
        return generators;
    }
}
