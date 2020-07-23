package me.erichards.bedwars.game;

import me.erichards.bedwars.game.generator.Generator;
import me.erichards.bedwars.game.shop.Shop;
import me.erichards.bedwars.game.team.TeamType;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class GameMap {

    private String name;
    private Map<TeamType, Location> spawnLocations;
    private List<Shop> itemNPCs;
    private List<Shop> teamNPCs;
    private List<Generator> generators;

    public GameMap(String name, Map<TeamType, Location> spawnLocations, List<Shop> itemNPCs, List<Shop> teamNPCs, List<Generator> generators) {
        this.name = name;
        this.spawnLocations = spawnLocations;
        this.itemNPCs = itemNPCs;
        this.teamNPCs = teamNPCs;
        this.generators = generators;
    }

    public String getName() {
        return name;
    }

    public Map<TeamType, Location> getSpawnLocations() {
        return spawnLocations;
    }

    public List<Shop> getItemNPCs() {
        return itemNPCs;
    }

    public List<Shop> getTeamNPCs() {
        return teamNPCs;
    }

    public List<Generator> getGenerators() {
        return generators;
    }
}
