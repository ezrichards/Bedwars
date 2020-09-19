package me.erichards.bedwars;

import me.erichards.bedwars.commands.MapCommand;
import me.erichards.bedwars.commands.WorldCommand;
import me.erichards.bedwars.game.Game;
import me.erichards.bedwars.game.generator.Generator;
import me.erichards.bedwars.game.generator.GeneratorType;
import me.erichards.bedwars.game.map.GameMap;
import me.erichards.bedwars.game.map.MapManager;
import me.erichards.bedwars.listeners.NPCListener;
import me.erichards.bedwars.utils.file.FileManager;
import me.erichards.bedwars.utils.gui.GUIListener;
import me.erichards.bedwars.listeners.BlockListener;
import me.erichards.bedwars.listeners.PlayerListener;
import me.erichards.bedwars.utils.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * May 26, 2020
 */
public class Bedwars extends JavaPlugin {

    private static Bedwars instance;
    private FileConfiguration config;
    private MapManager mapManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        mapManager = new MapManager();

        FileManager.loadFile("shop.yml");
        FileManager.loadFile("upgrades.yml");

        config.getConfigurationSection("maps").getKeys(false).forEach(map -> {
            int maxPlayers = config.getInt("maps." + map + ".maxPlayers");
            List<String> itemShops = config.getStringList("maps." + map + ".itemShops");
            List<String> teamUpgrades = config.getStringList("maps." + map + ".teamUpgrades");
            List<Generator> generators = new ArrayList<>();

            config.getStringList("maps." + map + ".generators").forEach(generator -> generators.add(parseGenerator(generator)));

            mapManager.addMap(new GameMap(map, maxPlayers, null, null, null, generators));
        });

        registerCommands();
        registerListeners();
        Game.getInstance().startLobby();
    }

    @Override
    public void onDisable() {
        Game.getInstance().endGame();
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public static Bedwars getInstance() {
        return instance;
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

    private Generator parseGenerator(String location) {
        String[] data = location.split(";");
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double z = Double.parseDouble(data[3]);
        WorldUtils.generateWorld("Beacon", World.Environment.NORMAL);

        return new Generator(GeneratorType.valueOf(data[0].toUpperCase()), new Location(Bukkit.getWorld("Beacon"), x, y, z));
    }
}
