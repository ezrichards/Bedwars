package me.erichards.bedwars;

import me.erichards.bedwars.commands.MapCommand;
import me.erichards.bedwars.commands.WorldCommand;
import me.erichards.bedwars.game.Game;
import me.erichards.bedwars.game.generator.Generator;
import me.erichards.bedwars.game.map.GameMap;
import me.erichards.bedwars.game.map.MapManager;
import me.erichards.bedwars.game.shop.Shop;
import me.erichards.bedwars.game.shop.ShopType;
import me.erichards.bedwars.game.team.Team;
import me.erichards.bedwars.game.team.TeamManager;
import me.erichards.bedwars.listeners.NPCListener;
import me.erichards.bedwars.utils.file.FileManager;
import me.erichards.bedwars.utils.gui.GUIListener;
import me.erichards.bedwars.listeners.BlockListener;
import me.erichards.bedwars.listeners.PlayerListener;
import me.erichards.bedwars.utils.world.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made by Ethan Richards
 * May 26, 2020
 */
public class Bedwars extends JavaPlugin {

    private static Bedwars instance;
    private FileConfiguration config;
    private MapManager mapManager;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        mapManager = new MapManager();
        teamManager = new TeamManager();
        teamManager.addTeam(new Team("Red", ChatColor.RED, false, null));
        teamManager.addTeam(new Team("Yellow", ChatColor.YELLOW, false, null));
        teamManager.addTeam(new Team("Green", ChatColor.GREEN, false, null));
        teamManager.addTeam(new Team("Blue", ChatColor.AQUA, false, null));

        FileManager.loadFile("shop.yml");
        FileManager.loadFile("upgrades.yml");

        config.getConfigurationSection("maps").getKeys(false).forEach(map -> {
            List<Shop> itemShops = new ArrayList<>();
            List<Shop> teamUpgrades = new ArrayList<>();
            List<Generator> generators = new ArrayList<>();
            Map<Team, Location> spawnLocations = new HashMap<>();

            config.getStringList("maps." + map + ".itemShops").forEach(shop -> itemShops.add(new Shop(ShopType.SHOP, LocationUtils.parseLocation(shop)))); // type is unnecessary?
            config.getStringList("maps." + map + ".teamUpgrades").forEach(shop -> teamUpgrades.add(new Shop(ShopType.UPGRADES, LocationUtils.parseLocation(shop))));
            config.getStringList("maps." + map + ".generators").forEach(generator -> generators.add(LocationUtils.parseGenerator(generator)));
            config.getStringList("maps." + map + ".spawnpoints").forEach(key -> {
                for(Map.Entry<Team, Location> spawnLocation : LocationUtils.parseSpawnpoint(key).entrySet()) {
                    spawnLocations.put(spawnLocation.getKey(), spawnLocation.getValue());
                }
            });

            mapManager.addMap(new GameMap(map, config.getInt("maps." + map + ".maxPlayers"), spawnLocations, itemShops, teamUpgrades, generators));
        });

        registerCommands();
        registerListeners();
        Game.getInstance().startLobby();
    }

    @Override
    public void onDisable() {
        Game.getInstance().endGame();
    }

    public static Bedwars getInstance() {
        return instance;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    private void registerCommands() {
        getCommand("map").setExecutor(new MapCommand());
        getCommand("world").setExecutor(new WorldCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }
}
