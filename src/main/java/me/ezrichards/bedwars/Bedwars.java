package me.ezrichards.bedwars;

import me.ezrichards.bedwars.commands.MapCommand;
import me.ezrichards.bedwars.commands.WorldCommand;
import me.ezrichards.bedwars.game.Game;
import me.ezrichards.bedwars.game.generator.Generator;
import me.ezrichards.bedwars.game.map.GameMap;
import me.ezrichards.bedwars.game.map.MapManager;
import me.ezrichards.bedwars.game.team.Team;
import me.ezrichards.bedwars.game.team.TeamManager;
import me.ezrichards.bedwars.listeners.NPCListener;
import me.ezrichards.bedwars.utils.file.FileManager;
import me.ezrichards.bedwars.utils.gui.GUIListener;
import me.ezrichards.bedwars.listeners.BlockListener;
import me.ezrichards.bedwars.listeners.PlayerListener;
import me.ezrichards.bedwars.utils.world.LocationUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
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
        teamManager.addTeam(new Team("Red", NamedTextColor.RED));
        teamManager.addTeam(new Team("Yellow", NamedTextColor.YELLOW));
        teamManager.addTeam(new Team("Green", NamedTextColor.GREEN));
        teamManager.addTeam(new Team("Blue", NamedTextColor.AQUA));

        FileManager.loadFile("shop.yml");
        FileManager.loadFile("upgrades.yml");

        config.getConfigurationSection("maps").getKeys(false).forEach(map -> {
            List<Location> itemShops = new ArrayList<>();
            List<Location> teamUpgrades = new ArrayList<>();
            List<Generator> generators = new ArrayList<>();

            config.getStringList("maps." + map + ".itemShops").forEach(shop -> itemShops.add(LocationUtils.parseLocation(shop)));
            config.getStringList("maps." + map + ".teamUpgrades").forEach(shop -> teamUpgrades.add(LocationUtils.parseLocation(shop)));
            config.getStringList("maps." + map + ".generators").forEach(generator -> generators.add(LocationUtils.parseGenerator(generator)));
            config.getStringList("maps." + map + ".spawnpoints").forEach(key -> {
                for(Map.Entry<String, Location> spawnLocation : LocationUtils.parseSpawnpoint(key).entrySet()) {
                    teamManager.getTeamByName(spawnLocation.getKey()).setSpawnLocation(spawnLocation.getValue());
                }
            });
            mapManager.addMap(new GameMap(map, config.getInt("maps." + map + ".maxPlayers"), itemShops, teamUpgrades, generators));
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
