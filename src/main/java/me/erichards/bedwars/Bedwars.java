package me.erichards.bedwars;

import me.erichards.bedwars.commands.MapCommand;
import me.erichards.bedwars.game.Game;
import me.erichards.bedwars.game.GameMap;
import me.erichards.bedwars.utils.gui.GUIListener;
import me.erichards.bedwars.listeners.BlockListener;
import me.erichards.bedwars.listeners.PlayerListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * May 26, 2020
 */
public class Bedwars extends JavaPlugin {

//    Diamond 2 in six minutes
//    Emerald 2 in six after dia 2

    private static Bedwars instance;
    private FileConfiguration config;
    private List<GameMap> maps;

    @Override
    public void onEnable() {
        instance = this;
        maps = new ArrayList<>();
        saveDefaultConfig();
        config = getConfig();

//        config.getConfigurationSection("maps").getKeys(false).forEach(map -> {
//            int maxPlayers = config.getInt("maps." + map + ".maxPlayers");
//            List<String> itemShops = config.getStringList("maps." + map + ".itemShops"); // loop through this, parse, add to itemShops
//            List<String> teamUpgrades = config.getStringList("maps." + map + ".teamUpgrades");
//
//
//        });

        getCommand("map").setExecutor(new MapCommand(this));
        registerListeners();
        Game.getInstance().startLobby();
    }

    @Override
    public void onDisable() {
        maps.clear();
    }

    public static Bedwars getInstance() {
        return instance;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }
}
